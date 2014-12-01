(ns clustering.core.clustering
  (:gen-class)
  (:require [clojure.tools.cli :refer [cli]])
  (:require [clojure.java.io :as io])
  (:require [clojure.math.numeric-tower :as math])
  (:require [clustering.util.math :as mathUtil])
 )

(defn clusterize [objects distanceCounter]
  (let 
    [maxVal 0.5 minVal 0.15                       
     Ra 3 Rb (* Ra 1.5)                            
     alpha (/ 4 (math/expt Ra 2)) beta  (/ 4 (math/expt Rb 2))               
     potentials (mathUtil/countPotential objects alpha distanceCounter) ; calculated potentials
    ]
    (loop [potentials potentials
           clusters ()]
      (let 
        [lastAddedPotential  (or (first (first clusters)) 0)          
         maxPotentialObject (apply max-key first potentials)  
         curPotential  (first maxPotentialObject)
         curObject (last (last maxPotentialObject))    
         recountedPotentials (map #(mathUtil/recountPotential % beta distanceCounter maxPotentialObject)
                                  potentials)
        ]
        (cond
         (> curPotential (* maxVal lastAddedPotential)) 
           (recur recountedPotentials (conj clusters maxPotentialObject)) ; maxPotentialObject as a new cluster, continue
         (< curPotential (* minVal lastAddedPotential))
           clusters                               ; reject last cluster and end process...
         :else 
           (let 
             ;find minimal distance between current object and the cluster center
             [minDistance (apply min (map #(distanceCounter curObject (last (last %))) clusters))]  
             (if (>= (+ (/ minDistance Ra) (/ curPotential lastAddedPotential)) 1)
               ;curObject is new cluster center
               (recur recountedPotentials (conj clusters maxPotentialObject))                  
               ;curObject isn't cluster center, set it's potential to 0 and retest clusters 
               (recur (map 
                        #(if (= curObject (last %)) (list 0 curObject) %) 
                        potentials) 
                      clusters)
             )
           )
        )
      )
    )
  )
)