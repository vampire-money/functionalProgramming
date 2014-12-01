(defproject fplab1 "0.0.1-SNAPSHOT"
  :description "FP lab 1"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/tools.cli "0.3.1"]]
  :main ^:skip-aot clustering.main
  :target-path "target/%s"
  :source-path "src"
  :profiles {:uberjar {:aot :all}})
