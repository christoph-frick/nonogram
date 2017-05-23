(ns nonogram.tools-test
  (:require [clojure.test :refer [are deftest]]
            [nonogram.tools :refer [split-by]]))

(deftest split-by-tests
  (are [coll result] (= result (split-by false? coll))
    [true] [[true]]
    [true false true] [[true] [true]]
    [false] []
    [false true false] [[true]]))
