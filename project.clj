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
                 [com.nimbusds/nimbus-jose-jwt "5.4" :exclusions [net.minidev/json-smart]]
                 [net.minidev/json-smart "2.3"]
                 ;; routing:
                 [metosin/reitit "0.8.0-alpha1"]
                 [metosin/reitit-pedestal "0.8.0-alpha1"]
                 ;; for the database:
                 [com.github.seancorfield/next.jdbc "1.3.981"]
                 [com.h2database/h2 "2.3.232"]
                 [environ/environ "1.2.0"]
                 ;;logger
                 [ch.qos.logback/logback-classic "1.5.17"
                  :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "2.0.16"]
                 [org.slf4j/jcl-over-slf4j "2.0.16"]
                 [org.slf4j/log4j-over-slf4j "2.0.16"]]
  :min-lein-version "2.11.0"
  :main ^:skip-aot banking-on-clojure.core
  :target-path "target/%s"
  :repl-options {:init-ns banking-on-clojure.core}
  :plugins [[:lein-cloverage "1.2.4"]]
  :test2junit-output-dir "target/test-reports"
  :profiles
  {:dev  [:project/dev :profiles/dev]
   :repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user
                         :host "0.0.0.0"
                         :port 47480}}
   :uberjar {:aot :all}
   :debug {:jvm-opts
           ["-server" (str "-agentlib:jdwp=transport=dt_socket,"
                           "server=y,address=8000,suspend=n")]}
   :profiles/dev {}
   :project/dev  {:source-paths   ["src"]
                  :resource-paths ["config", "resources"]
                  :dependencies   [[io.pedestal/pedestal.service-tools "0.7.2"]]}})
