(ns nonogram.tools)

(defn split-by
  ([f]
   (comp (partition-by f) (take-nth 2)))
  ([f coll]
   (sequence (split-by f) coll)))

(defn max-count
  [xs]
  (apply max (map count xs)))
