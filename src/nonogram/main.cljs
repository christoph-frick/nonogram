(ns ^:figwheel-always nonogram.main
  (:require [nonogram.game :as game]
            [nonogram.ui :as ui]
            [rum.core :as rum]))

(enable-console-print!)

(rum/mount
 (ui/game
  (-> (game/new-board-setup (+ 3 (rand-int 17)) (+ 3 (rand-int 17)) (rand))
      game/new-board
      game/new-game-board))
 (js/document.getElementById "nonogram"))
