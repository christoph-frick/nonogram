(ns nonogram.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px em]]))

(defstyles screen
  (let [base-size (px 16)
        cell-size (em 2)
        background-color "#ddd"
        grid-line [[(px 1) :solid "#333"]]]
    [[:* :body {:font-family "Arial, sans-serif"
                :font-size base-size
                :background-color background-color}]
     [:div
      [:.hint :.cell {:width cell-size
                      :height cell-size
                      :text-align :center
                      :vertical-align :middle
                      :line-height cell-size}]
      [:.col-hint-container {:float :left
                             :border-bottom grid-line}
       [:.row-hints {:float :left
                     :border :none}]
       [:.col-hints {:float :left
                     :position :relative
                     :width cell-size
                     :border-left grid-line}
        [:.hint {:border-right grid-line}]]]
      [:.rows
       [:.row-hints {:clear :left}
        [:.hint {:float :left
                 :position :relative
                 :border-bottom grid-line}]]
       [:.row {:border-left grid-line
               :float :left}
        [:.cell {:float :left
                 :position :relative
                 :border-bottom grid-line
                 :border-right grid-line}]
        [:.cell.none {:background-color :inherit}]
        [:.cell.yes {:background-color "green"}]
        [:.cell.no {:background-color "red"}]]]]]))

;; div.hint {
;; 	width: 2em;
;; 	height: 2em;
;; 	position: relative;
;; }
;; div.hint.ver {
;; }
;; div.hint.hor {
;; 	float: left;
;; }
