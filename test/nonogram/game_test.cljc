(ns nonogram.game-test
  (:require [nonogram.game :as stu]
            [clojure.test :refer [deftest is]]
            [clojure.spec :as s]  
            [clojure.spec.gen :as sg]  
            [clojure.spec.test :as st]))


(deftest generative
  (is (let  [{:keys [total check-passed]} (->
                                           (st/enumerate-namespace 'nonogram.game)
                                           st/check
                                           st/summarize-results)]
        (= total check-passed))))
