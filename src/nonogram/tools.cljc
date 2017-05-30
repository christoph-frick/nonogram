(ns nonogram.tools)

(defn split-by
  ([pred]
   (comp (drop-while pred)
         (partition-by pred)
         (take-nth 2)))
  ([pred coll]
   (sequence (split-by pred) coll)))

(defn max-count
  [xs]
  (apply max (map count xs)))
