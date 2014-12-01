(ns crawling.util.stringUtil
  (:gen-class)
  (:require [clojure.string :as string])
  (:require [clojure.java.io :as io])
)

(defn makeStatusMessage [node]
  (if (= (:status node) 404)
    " is unavailable (404)"
    (let 
      [message (str " has " (count (:childUrls node)) " child link(s)")]
      (when ((complement nil?) (:location node))
        (str message " redirects to " (:location node))
      )
      message
    )
  )
)

(defn printNodeRecursive [node level]
  (let 
    [formattedPrint (fn [node level]
        (println (str (apply str (repeat level ".")) (:curUrl node) (makeStatusMessage node)))
      )
     ]
    (formattedPrint node level)
    (doseq [child @(:children node)] 
      (printNodeRecursive child (inc level))
    )
  )
)

(defn printTree [root]
  (printNodeRecursive root 0)
)

(defn readUrls [fileName]
  (def urls (atom []))
  (with-open [rdr (io/reader fileName)]
    (doseq [readLine (line-seq rdr)]
       (when (not= readLine "")
         (swap! urls conj readLine)
       )
    )
  )
  @urls
)
