;src/app/core.clj
(ns banking-on-clojure.core
  (:gen-class)
  (:require [io.pedestal.http :as server]
            [reitit.pedestal :as pedestal]
            [reitit.http :as http]
            [reitit.ring :as ring]
            [reitit.dev.pretty :as pretty]
            [reitit.coercion.spec]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.http.coercion :as coercion]
            [reitit.http.interceptors.parameters :as parameters]
            [reitit.http.interceptors.muuntaja :as muuntaja]
            [reitit.http.interceptors.exception :as exception]
            [reitit.http.interceptors.multipart :as multipart]
            ;; Uncomment to use
            ; [reitit.http.interceptors.dev :as dev]
            ; [reitit.http.spec :as spec]
            ; [spec-tools.spell :as spell]
            [clojure.core.async :as a]
            ;; Async IO
            ; [clojure.java.io :as io]
            [muuntaja.core :as m]
            [environ.core :refer [env]]))

;; our first handler
(defn interceptor [number]
  {:enter (fn [ctx] (a/go (update-in ctx [:request :number] (fnil + 0) number)))})

(def routes
  (pedestal/routing-interceptor
   (http/router
    [["/" {:name :home :get (fn [_] {:status 200
                                     :body "Hello world"})}]
     ["/api"
      {:interceptors [(interceptor 1)]}
      ["/api/ping" ::ping]
      ["/api/orders/:id" ::order]
      ["/number"
       {:interceptors [(interceptor 10)]
        :get {:interceptors [(interceptor 100)]
              :handler (fn [req]
                         {:status 200
                          :body (select-keys req [:number])})}}]]
     ["/swagger.json"
      {:get {:no-doc true
             :swagger {:info {:title "my-api"
                              :description "with pedestal & reitit-http"}}
             :handler (swagger/create-swagger-handler)}}]
     ["/interceptors"
      {:swagger {:tags ["interceptors"]}
       :interceptors [(interceptor 1)]}
      ["/number"
       {:interceptors [(interceptor 10)]
        :get {:interceptors [(interceptor 100)]
              :handler (fn [req]
                         {:status 200
                          :body (select-keys req [:number])})}}]]]

    {;:reitit.interceptor/transform dev/print-context-diffs ;; pretty context diffs
       ;;:validate spec/validate ;; enable spec validation for route data
       ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
     :exception pretty/exception
     :data {:coercion reitit.coercion.spec/coercion
            :muuntaja m/instance
            :interceptors [;; swagger feature
                           swagger/swagger-feature
                             ;; query-params & form-params
                           (parameters/parameters-interceptor)
                             ;; content-negotiation
                           (muuntaja/format-negotiate-interceptor)
                             ;; encoding response body
                           (muuntaja/format-response-interceptor)
                             ;; exception handling
                           (exception/exception-interceptor)
                             ;; decoding request body
                           (muuntaja/format-request-interceptor)
                             ;; coercing response bodys
                           (coercion/coerce-response-interceptor)
                             ;; coercing request parameters
                           (coercion/coerce-request-interceptor)
                             ;; multipart
                           (multipart/multipart-interceptor)]}})
   (ring/routes
    (swagger-ui/create-swagger-ui-handler
     {:path "/"
      :config {:validatorUrl nil
               :operationsSorter "alpha"}})
    (ring/create-resource-handler)
    (ring/create-default-handler))))

(defonce http-state (atom nil))
(defn start
  [& _]
  (swap! http-state (fn [st]
                      ;; if there is something running, stop it
                      (let [port (Integer. (or (env :port) 5000))]
                      (some-> st server/stop)
                      (-> {::server/host   "0.0.0.0"
                           ::server/port   port
                           ::server/join?  false
                           ::server/type   :jetty
                           ::server/routes []
                           ::server/secure-headers {:content-security-policy-settings
                                                    {:default-src "'self'"
                                                     :style-src "'self' 'unsafe-inline'"
                                                     :script-src "'self' 'unsafe-inline'"}}}
                          (server/default-interceptors)
                              ;; swap the reitit router
                          (pedestal/replace-last-interceptor routes)
                          (server/dev-interceptors)
                          (server/create-server)
                          (server/start))
                      (println "Server running in port" port)))))

(defn -main [& _args]
  (start))
