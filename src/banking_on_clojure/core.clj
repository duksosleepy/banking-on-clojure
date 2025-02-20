(ns banking-on-clojure.core
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [reitit.core :as r]
            [reitit.dev.pretty :as pretty]
            [environ.core :refer [env]]))

;; our first handler
(defn hello
  [req]
  {:body "Hello world!"
   :status 200})

(def routes
  (r/router
   [["/api/ping" ::ping]
    ["/api/orders/:id" ::order]]))

(defonce http-state (atom nil))
(defn dev-start
  [& _]
  (swap! http-state (fn [st]
                      ;; if there is something running, stop it
                      (some-> st http/stop)
                      (-> {::http/routes routes
                           ::http/host   "0.0.0.0"
                           ::http/port   (Integer. (or (env :port) 5000))
                           ::http/join?  false
                           ::http/type   :jetty}
                          http/default-interceptors
                          http/dev-interceptors
                          http/create-server
                          http/start))))
