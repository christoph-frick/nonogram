(ns nonogram.ui
  (:require [rum.core :as rum]
            [clojure.string :as str]
            [nonogram.game :as game]
            [nonogram.tools :as t]))

(defonce STATE
  (atom
   (-> (game/new-board-setup 5 5 0.5)
       #_(game/new-board-setup (+ 3 (rand-int 17)) (+ 3 (rand-int 17)) (rand))
       (game/new-board)
       (game/new-game-board))))

(defn simple-key [args]
  (str/join "-" (map str args)))

(rum/defc hint
  [nr]
  [:.hint nr])

(rum/defc hints
  [root state max]
  (into root
        (concat (for [_ (range (- max (count state)))]
                  (hint ""))
                (for [h state]
                  (hint h)))))

(rum/defc row-hint
  [state max]
  (hints [:.row-hints] state max))

(rum/defc col-hint
  [state max]
  (hints [:.col-hints] state max))

(rum/defc cell
  [state row-index cell-index]
  [:.cell {:key (simple-key [:cell row-index cell-index])
           :class (name state)
           :on-click (fn [e]
                       (swap! STATE game/toggle row-index cell-index))}])

(rum/defc row
  [state row-index]
  [:.row {:key (simple-key [:row row-index])}
   (for [[ci c] (map vector (range) state)]
     (cell c row-index ci))])

(rum/defc game
  []
  (let [{:board/keys [col-hints row-hints] :game-board/keys [rows] :as board} @STATE
        max-col-hints (t/max-count col-hints)
        max-row-hints (t/max-count row-hints)]
    [:div
     (when (game/won? board)
       [:h1 "You won!"])
     [:.game
      [:.col-hint-container
       (for [_ (range max-row-hints)]
         (row-hint [] max-col-hints))
       (for [ch col-hints]
         (col-hint ch max-col-hints))]
      [:.rows
       (for [[rh r ri] (map vector row-hints rows (range))]
         [:div
          (row-hint rh max-row-hints)
          (row r ri)])]]]))
