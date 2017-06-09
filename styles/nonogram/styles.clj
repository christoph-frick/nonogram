(ns nonogram.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.selectors :refer [selector div last-child nth-child]]
            [garden.units :refer [px em]]))

(defstyles screen
  (let [cell-size (em 2)
        grid-line [[(px 1) :solid "#ddd"]]
        grid-line-seperator [[(px 1) :solid "#999"]]]
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
       [:.cell.yes {:background-color "rgb(128,255,128)"}]
       [:.cell.no {:background-color "rgb(255,128,128)"}]]
      ; thicker line every fifth row/col
      [(div (nth-child 5))
       [:.row [:.cell
               {:border-bottom grid-line-seperator}]]]
      [:.row
       [(div ".cell" (nth-child 5))
        {:border-right grid-line-seperator}]]]]))

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
