(ns banking-on-clojure.core
  (:gen-class))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn -main []
  (println "Server starting...")
  (foo "Song Khoi say: "))
