(ns banking-on-clojure.core
  (:gen-class)
  (:require [io.pedestal.http :as server]
            [reitit.pedestal :as pedestal]
            [reitit.http :as http]
            [reitit.ring :as ring]
            [reitit.dev.pretty :as pretty]
            [environ.core :refer [env]]))

;; our first handler
(defn interceptor [number]
  {:enter (fn [ctx] (update-in ctx [:request :number] (fnil + 0) number))})

(defn hello
  [req]
  {:body "Hello world!"
   :status 200})

(def routes
  ["/api"
   {:interceptors [(interceptor 1)]}
   ["/api/ping" ::ping]
   ["/api/orders/:id" ::order]
   ["/number"
    {:interceptors [(interceptor 10)]
     :get {:interceptors [(interceptor 100)]
           :handler (fn [req]
                      {:status 200
                       :body (select-keys req [:number])})}}]])

(defonce http-state (atom nil))
(defn dev-start
  [& _]
  (swap! http-state (fn [st]
                      ;; if there is something running, stop it
                      (some-> st server/stop)
                      (-> {::server/host   "0.0.0.0"
                           ::server/port   (Integer. (or (env :port) 5000))
                           ::server/join?  false
                           ::server/type   :jetty
                           ::server/routes []}
                          (server/default-interceptors)
                              ;; swap the reitit router
                          (pedestal/replace-last-interceptor
                           (pedestal/routing-interceptor
                            (http/router routes)))
                          (server/dev-interceptors)
                          (server/create-server)
                          (server/start)))))
