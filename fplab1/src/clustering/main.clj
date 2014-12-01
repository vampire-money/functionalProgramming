(ns clustering.main
  (:gen-class)
  (:require [clojure.tools.cli :refer [cli]])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clustering.core.clustering :as clust])
  (:require [clustering.util.math :as mathUtil])
  (:require [clustering.util.stringUtil :as stringUtil])
 )

(defn readObjects [fileName]
  (def objects (atom {}))
  (def counter 0)
  (with-open [rdr (io/reader fileName)]
    (doseq [readLine (line-seq rdr)]
       (when (not= readLine "")
         ;adding array with attributes for object from line
         (swap! objects assoc counter (stringUtil/makeAttributesArray readLine))
       )
       (def counter (inc counter))
    )
  )
  @objects
)

(defn -main [& args] 
  (let [
        [opts args usage]
        (cli args
          ["-f" "--file" "REQUIRED: File with data"]
          ["-h" "--hamming" "Use Hamming distance counter"]
          ["-e" "--euclidean" "Use Euclidean distance counter"]
          )
        ]
    (when (and (not (:file opts)) (not (:hamming opts)) (not (:euclidean opts)))
      (println usage)
      (System/exit 0)
    )
    (when (not (:file opts))
      (println "Please provide path to file (-f or --file)")
      (System/exit 0)
    )
    (def objects (readObjects (:file opts)))
    (if (or (:hamming opts) (:euclidean opts))
      (do
        (if (:hamming opts)
          (def result (clust/clusterize objects mathUtil/countHammingDistance))
          (def result (clust/clusterize objects mathUtil/countEuclideanDistance))
        )
        (println 
          (stringUtil/prettyPrint result)
        )
      )
      (println "Please choose some distance counter - Hamming (-h, --hamming) or Euclidean (-e, --euclidean)")
    )
  )
)
   
