(ns crawling.main
  (:gen-class)
  (:require [clojure.tools.cli :refer [cli]])
  (:require [crawling.core :as core])
  (:require [crawling.util.stringUtil :as stringUtil])
)

(defn -main [& args]
  (let [
        [opts args usage]
        (cli args
          ["-f" "--file" "Urls list (in file)"]
          ["-d" "--depth" "Depth of crawling"]
          )
        ]
    (when (and (not (:file opts)) (not (:depth opts)))
      (println usage)
      (System/exit 0)
    )
    (when (not (:file opts))
      (println "Please provide path to file (-f or --file)")
      (System/exit 0)
    )
    ;(def objects (readObjects (:file opts)))
    (if (:depth opts)
      (let [root (core/crawl (:file opts) (:depth opts))]
        (stringUtil/printTree root)
      )
      (println "Please provide crawling depth value (-d, --depth)")
    )
  )
)
