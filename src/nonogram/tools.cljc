(ns nonogram.tools)

(defn split-by
  ([pred]
   (comp (partition-by pred)
         (remove (comp pred first))))
  ([pred coll]
   (sequence (split-by pred) coll)))

(defn max-count
  [xs]
  (apply max (map count xs)))
