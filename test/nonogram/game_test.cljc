(ns nonogram.game-test
  (:require [clojure.spec.test.alpha :as stest]
            [clojure.test :refer [deftest is]]))

(deftest generative
  (is
   (let [{:keys [total check-passed]} (->
                                       (stest/enumerate-namespace 'nonogram.game)
                                       (stest/check)
                                       (stest/summarize-results))]
     (and (pos? total) 
          (= total check-passed)))))
