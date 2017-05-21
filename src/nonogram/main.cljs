(ns ^:figwheel-always nonogram.main
  (:require [nonogram.game :as game]
            [nonogram.ui :as ui]
            [rum.core :as rum]))

(enable-console-print!)

(defn mount
  [board]
  (rum/mount
   (ui/game)
   (js/document.getElementById "nonogram")))

(add-watch ui/STATE :mount (fn [_ _ _ board] (mount board)))

(mount @ui/STATE)
