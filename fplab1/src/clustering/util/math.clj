(ns clustering.util.math
  (:gen-class)
  (:require [clojure.tools.cli :refer [cli]])
  (:require [clojure.math.numeric-tower :as math])
  (import java.lang.Math)
 )
 
(defn countEuclideanDistance [objectA objectB]
  (math/sqrt (reduce + (map #(math/expt % 2) (map - objectA objectB))))
)
 
(defn countHammingDistance [objectA objectB]
  (reduce + (map #(if % 0 1) (map = objectA objectB)))
)

(defn countPotential [objects alpha distanceCounter]
  (let 
    [objectPotential (fn [processedObject]
       (list 
         (reduce + 
                 (map
                   #(Math/exp (- (* alpha (distanceCounter (last %) (last processedObject)))))
                   objects
                 )
         )
         processedObject
       )
     )]
    (map objectPotential objects)
  )
)

(defn recountPotential [potential beta distanceCounter maxPotentialObject]
  (cons 
    (- (first potential)
       (* (first maxPotentialObject) 
          (Math/exp (- (* beta (distanceCounter (last (last potential)) (last (last maxPotentialObject))))))
          )
       ) 
    (rest potential)
  )
)