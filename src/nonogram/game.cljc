(ns nonogram.game
  (:require [nonogram.tools :refer [split-by]]
            [clojure.spec :as spec]
            [clojure.spec.gen :as sgen]))

(def ^:const max-board-width 50)
(def ^:const max-board-height max-board-width)

(spec/def :board-setup/width 
  (spec/and (spec/int-in 1 max-board-width)))

(spec/def :board-setup/height 
  (spec/and (spec/int-in 1 max-board-height)))

(spec/def :board-setup/probability 
  (spec/and number? #(< 0 % 1))) ; FIXME: use double-in

(spec/def :board-setup/common 
  (spec/keys :req [:board-setup/width :board-setup/height :board-setup/probability]))

(defn new-board-setup
  [width height probability]
  #:board-setup{:width width
                :height height
                :probability probability})

(defn rand-bool
  [probability]
  (< probability (rand)))

(spec/fdef rand-bool
        :args (spec/cat :probability :board-setup/probability)
        :ret boolean?)

(spec/def :board-row/cell 
  boolean?)

(spec/def :board/row 
  (spec/coll-of :board-row/cell :min-count 1 :max-count max-board-width))

(def row-gen
  (sgen/bind
   (spec/gen (spec/tuple :board-setup/height :board-setup/width))
   (fn [[width height]]
     (spec/gen (spec/coll-of
                (spec/coll-of :board-row/cell :count width)
                :count height)))))

(spec/def :board/rows
  (spec/with-gen
    (spec/and (spec/coll-of :board/row :min-count 1 :max-count max-board-height)
              #(apply = (map count %)))
    (fn [] row-gen)))

(defn size 
  [{:board-setup/keys [width height] :as board}]
  (* width height))

(defn roll-rows 
  [{:board-setup/keys [width height probability] :as board}]
  (partition width 
             (repeatedly 
               (size board)
               #(rand-bool probability))))

(spec/fdef roll-rows
           :args (spec/cat :board-setup :board-setup/common)
           :ret :board/rows)

(defn rows-to-cols 
  [rows]
  (apply mapv vector rows))

(spec/fdef rows-to-cols
           :args (spec/cat :rows :board/rows)
           :ret :board/rows
           :fn #(= (rows-to-cols (:ret %)) (-> % :args :rows)))

(spec/def :board/hint 
  pos-int?)

(spec/def :board/hints-coll 
  (spec/coll-of :board/hint))

(spec/def :board/hints 
  (spec/coll-of :board/hints-coll))

(spec/def :board/row-hints 
  :board/hints)

(spec/def :board/col-hints 
  :board/hints)

(defn row-hints
  [row]
  (into []
        (comp (split-by false?)
              (map (comp inc count)))
        row))

(spec/fdef row-hints
           :args (spec/cat :row :board/row)
           :ret :board/hints-coll)

(defn hints
  [grid]
  (mapv row-hints grid))

(spec/fdef hints
           :args (spec/cat :rows :board/rows)
           :ret :board/hints)

(spec/def :board/common
  (spec/keys :req [:board/rows :board/row-hints :board/col-hints]))

(defn new-board [{:board-setup/keys [width height probability] :as board}]
  (let [rows (roll-rows board)
        cols (rows-to-cols rows)
        row-hints (hints rows)
        col-hints (hints cols)]
    (assoc board
           :board/rows rows
           :board/row-hints row-hints
           :board/col-hints col-hints)))

(spec/fdef new-board
           :args (spec/cat :board-setup :board-setup/common)
           :ret :board/common)

(def cell-states
  [:none :yes :no])

(spec/def :game-board/cell
  (into #{} cell-states))

(def next-cell
  (into {} (map vector cell-states (rest (cycle cell-states)))))

(spec/def :game-board/row
  (spec/coll-of :game-board/cell :min-count 1 :max-count max-board-width))

(spec/def :game-board/rows
  (spec/coll-of :game-board/row :min-count 1 :max-count max-board-height))

(spec/def :game-board/common
  (spec/keys :req [:game-board/rows]))

(defn new-game-board
  [{:board-setup/keys [width height] :as board}]
  (assoc board
         :game-board/rows (into [] (repeat height
                                           (into [] (repeat width :none))))))

(spec/fdef new-game-board
           :args (spec/cat :board-setup :board-setup/common)
           :ret :game-board/common)

(spec/def :game/common
  (spec/keys :req [:board/common :game-board/common]))

(defn toggle
  [board row-index col-index]
  (update-in board [:game-board/rows row-index col-index] next-cell))
