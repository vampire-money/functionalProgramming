(defproject fplab2 "0.0.1-SNAPSHOT"
  :description "Web crawler to predefined depth"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]
                 [enlive "1.1.5"]
                 [clj-http "1.0.1"]
                 [slingshot "0.12.1"]]
  :main ^:skip-aot crawling.main
  :target-path "target/%s"
  :source-path "src"
  :profiles {:uberjar {:aot :all}}
)
