(defproject banking-on-clojure "0.1.0-SNAPSHOT"
  :description "Banking on Clojure"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 ;; basic Ring and web server:
                 [ring/ring-core "1.13.0"]
                 [io.pedestal/pedestal.service "0.7.2"]
                 [io.pedestal/pedestal.jetty "0.7.2"]
                 ;;JWT
                 [com.nimbusds/nimbus-jose-jwt "5.4"]
                 ;; routing:
                 [metosin/reitit "0.8.0-alpha1"]
                 [metosin/reitit-pedestal "0.8.0-alpha1"]
                 ;; logging
                 [org.slf4j/slf4j-simple "2.0.16"]
                 ;; for the database:
                 [com.github.seancorfield/next.jdbc "1.3.981"]
                 [com.h2database/h2 "2.3.232"]
                 [environ/environ "1.2.0"]]
  :min-lein-version "2.11.0"
  :main banking-on-clojure.core
  :repl-options {:init-ns banking-on-clojure.core}
  :plugins [[:lein-cloverage "1.2.4"]])
