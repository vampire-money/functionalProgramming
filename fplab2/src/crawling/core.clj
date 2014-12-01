(ns crawling.core
  (:gen-class)
  (:require [crawling.util.stringUtil :as stringUtil])
  (:require [crawling.util.url :as urlUtil])
  (import java.util.UUID)
)

(defn newNode [parent childUrls depth curUrl status location]
  {:id (str (UUID/randomUUID)) 
   :parent parent 
   :childUrls childUrls 
   :depth depth 
   :curUrl curUrl 
   :status status 
   :children (atom []) 
   :location location}
)

(defn getParentId [parent]
  (if (nil? parent)
    nil
    (:id parent)
  )
)

(defn findAllUrls [parent url depth]
  (println (str "Searching for links at " url))
  (let [response (urlUtil/loadPage url)
        page (urlUtil/formatResponse response)
        child (if ((complement nil?) page)
                (newNode
                  (getParentId parent) 
                  (urlUtil/findUrls page) 
                  depth 
                  url 
                  (:status response) 
                  (urlUtil/getLocationHeader response)
                )
                (newNode 
                  (getParentId parent) 
                  '()
                  depth
                  url
                  (:status response)
                  (urlUtil/getLocationHeader response)
                )
              )
        ]
        (swap! (:children parent) conj child)
        child
  )
)

(defn crawlToNode [node urls depth]
  (if (< depth 1)
    node
    (doseq [child (pmap #(findAllUrls node % depth) urls)]
      (crawlToNode child (:childUrls child) (dec depth))
    )
  ) 
)

(defn crawl [fileName depth]
  (let 
    [urls (stringUtil/readUrls fileName)
     parent (newNode nil urls (read-string depth) "root" nil nil)
    ]
    (crawlToNode parent urls (read-string depth))
    parent
  )
)
