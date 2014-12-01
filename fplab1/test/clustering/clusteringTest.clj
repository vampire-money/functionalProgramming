(ns clustering.clusteringTest
  (:require [clojure.test :refer :all])
  (:require [clustering.util.stringUtil :as stringUtil])
  (:require [clustering.util.math :as mathUtil])
)

(defn mockPotentialObject [potential line attributes]
  (list potential (vector line attributes) )
)

(deftest output
  (testing "Check corectness of output"
    (let 
      [prettyString (stringUtil/prettyPrint (list 
                                              (mockPotentialObject 1.0066944472732215 142 (vector 5.8 2.7 5.1 1.9)) 
                                              (mockPotentialObject 2.0326699436627385 86 (vector 6.7 3.1 4.7 1.5)) 
                                              (mockPotentialObject 3.894562476508305 103 (vector 6.3 2.9 5.6 1.8)) 
                                              (mockPotentialObject 7.88474134014649 31 (vector 5.4 3.4 1.5 0.4)) 
                                              (mockPotentialObject 14.967756574051576 55 (vector 5.7 2.8 4.5 1.3)) 
                                              (mockPotentialObject 33.49195887104348 1 (vector 4.9 3.0 1.4 0.2)) 
                                            )
                    )
      ]
      (is (= prettyString 
"[142] - {5.8 2.7 5.1 1.9} (potential is 1.0066944472732215)
[86] - {6.7 3.1 4.7 1.5} (potential is 2.0326699436627385)
[103] - {6.3 2.9 5.6 1.8} (potential is 3.894562476508305)
[31] - {5.4 3.4 1.5 0.4} (potential is 7.88474134014649)
[55] - {5.7 2.8 4.5 1.3} (potential is 14.967756574051576)
[1] - {4.9 3.0 1.4 0.2} (potential is 33.49195887104348)"))
    )
  )
)

(deftest testEuclideanDistanceCounter
  (testing "Check Euclidean distance counter"
    (let 
      [euclideanDistance (mathUtil/countEuclideanDistance (vector 5.8 2.7 5.1 1.9) (vector 6.7 3.1 4.7 1.5))]
      (is (= euclideanDistance 1.1357816691600546))
    )
  )
)

(deftest testHammingDistanceCounter
  (testing "Check Hamming distance counter"
    (let 
      [euclideanDistance (mathUtil/countHammingDistance (vector 5.8 2.7 5.1 1.9) (vector 6.7 3.1 4.7 1.5))]
      (is (= euclideanDistance 4))
    )
  )
)

(deftest testRecountPotential
  (testing "Check recounting of potential"
    (let 
      [newPotential 
       (mathUtil/recountPotential
         (mockPotentialObject 1.0066944472732215 142 (vector 5.8 2.7 5.1 1.9))
         5.0625
         mathUtil/countHammingDistance
         (mockPotentialObject 14.967756574051576 55 (vector 5.7 2.8 4.5 1.3)) 
       )
      ]
      (is (= newPotential (mockPotentialObject 1.0066944232465587 142 (vector 5.8 2.7 5.1 1.9))))
    )
  )
)
