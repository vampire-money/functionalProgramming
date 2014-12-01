(ns crawling.crawlingTest
  (:require [clojure.test :refer :all])
  (:require [net.cgrand.enlive-html :as ehtml])
  (:require [crawling.util.url :as urlUtil])
  (:require [clojure.java.io :as io])
)

(defn readHtml [fileName]
  (def html (atom ""))
  (with-open [rdr (io/reader fileName)]
    (doseq [readLine (line-seq rdr)]
       (when (not= readLine "")
         (swap! html str readLine)
       )
    )
  )
  (ehtml/html-snippet @html)
)

(deftest testLinksSearch
  (testing "Count links on page"
    (let [page (readHtml "test/leinTest.html")
          childUrls (urlUtil/findUrls page)]
      (is (= (count childUrls) 28)))
  )
)

(deftest test404Processing
  (testing "Check if 404 is processed correctly"
    (let 
      [response (urlUtil/loadPage "http://someurlthatdoesntexist.org")]
      (is (= (:status response) 404))
    )
  )
)
