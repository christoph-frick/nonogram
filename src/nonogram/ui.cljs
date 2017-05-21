(ns nonogram.ui
  (:require [rum.core :as rum]
            [nonogram.tools :as t]))

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
  [state]
  [:.cell {:class (name state)}])

(rum/defc row
  [state]
  [:.row (for [c state]
              (cell c))])

(rum/defc game
  [{:board/keys [col-hints row-hints] :game-board/keys [rows] :as board}]
  (let [max-col-hints (t/max-count col-hints)
        max-row-hints (t/max-count row-hints)]
    [:.game
     [:.col-hint-container
      (for [_ (range max-row-hints)]
        (row-hint [] max-col-hints))
      (for [ch col-hints]
        (col-hint ch max-col-hints))]
     [:.rows
      (for [[rh r] (map vector row-hints rows)]
        [:div
         (row-hint rh max-row-hints)
         (row r)])]]))
