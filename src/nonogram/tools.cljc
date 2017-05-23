(ns nonogram.tools)

(defn split-by
  ([pred]
   (comp (partition-by pred)
         (drop-while (comp pred first))
         (take-nth 2)))
  ([pred coll]
   (sequence (split-by pred) coll)))

(defn max-count
  [xs]
  (apply max (map count xs)))
