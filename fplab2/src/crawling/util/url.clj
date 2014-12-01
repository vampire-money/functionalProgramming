(ns crawling.util.url
  (:gen-class)
  (:require [clj-http.client :as client])
  (:require [net.cgrand.enlive-html :as ehtml])
  (:use [slingshot.slingshot :only [try+]])
  (:require [crawling.util.stringUtil :as stringUtil])
  (import java.util.UUID)
)

(defn loadPage [url]
  (try+
    (client/get url {:throw-exceptions false})
    (catch Object _ {:status 404 :headers nil})
  )
)

(defn formatResponse [response]
  (let 
    [content-type (:content-type (:headers response))]
    (if (and (not= 404 (:status response)) (boolean (re-find #"text/html" content-type)))
      (ehtml/html-snippet (:body response))
      nil
    )
  )
)

(defn getLocationHeader [response]
  (let [status (:status response)]
    (if (boolean (some #(= status %) '(300 301 302 303 307)))
      (:location (:headers response))
      nil
    )
  )
)

(defn findUrls [page]
  (let 
    [filterNotNilUrls  (fn [urlList]
      (filter (complement nil?) urlList)
     )
    ]
    (->> 
      (ehtml/select page #{[:a]})
      (map #(:href (:attrs %1)))
      (filterNotNilUrls)
    )
  )
)
