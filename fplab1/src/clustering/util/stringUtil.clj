(ns clustering.util.stringUtil
  (:gen-class)
  (:require [clojure.tools.cli :refer [cli]])
  (:require [clojure.string :as string])
 )

(defn makeAttributesArray [string]
  (into 
    [] (map read-string (drop-last (string/split string #",")))
  )
)

(defn prettyPrint [toPrint]
  (string/join "\n" 
               (map #(str "[" (first (last %)) "] - "
                          "{" (string/join " " (map str (last (last %)))) "} " 
                          "(potential is " (first %) ")") toPrint )
  )
)
