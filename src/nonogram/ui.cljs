(ns nonogram.ui
  (:require [rum.core :as rum]
            [rum.mdl :as mdl]
            [clojure.string :as str]
            [nonogram.game :as game]
            [nonogram.tools :as t]))

(defn new-game
  [setup]
  (-> setup
      #_(game/new-board-setup (+ 3 (rand-int 17)) (+ 3 (rand-int 17)) (rand))
      (game/new-board)
      (game/new-game-board)))

; TODO move all the defn over to game ns
(defonce STATE
  (let [setup (game/new-board-setup 5 5 0.5)]
    (atom {:setup setup
           :game (new-game setup)})))

(defn new-game!
  []
  (swap! STATE (fn [{:keys [setup] :as state}]
                 (assoc state :game (new-game setup)))))

(defn restart-game!
  []
  (swap! STATE update :game game/new-game-board))

(defn value
  [e]
  (-> e .-target .-value))

(defn update-setup!
  [key value]
  (swap! STATE update :setup assoc key value))

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
           :on-mouse-up (fn [e]
                          (let [mapping {0 :yes 2 :no}
                                button (-> e .-button)]
                            (when-let [cell-state (mapping button)]
                              (swap! STATE update :game game/toggle row-index cell-index cell-state))))}])

(rum/defc row
  [state row-index]
  [:.row {:key (simple-key [:row row-index])}
   (for [[ci c] (map vector (range) state)]
     (cell c row-index ci))])

(rum/defc board
  [{:board/keys [col-hints row-hints] :game-board/keys [rows] :as state}]
  (let [max-col-hints (t/max-count col-hints)
        max-row-hints (t/max-count row-hints)]
    [:div
     (when (game/won? state)
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

(rum/defc manual
  []
  (mdl/card
   {:mdl [:shadow--2dp]}
   (mdl/card-title "How to play")
   (mdl/card-text
    [:span "See " [:a {:target "_blank" :href "https://en.wikipedia.org/wiki/Nonogram"} "Wikipedia"] " for instructions."
     "Uee " [:i "left mouse button"] " to mark blocks and " [:i "right mouse button"] " to mark locations without a block"])))

(rum/defc game
  []
  (let [{{:board-setup/keys [width height probability]} :setup, state :game} @STATE]
    (mdl/layout
     (mdl/header
      (mdl/header-row
       (mdl/nav [:h1 "Nonogram"])))
     (mdl/main-content
      (mdl/grid
       (mdl/cell {:mdl [:8]}
                 (mdl/grid
                  (mdl/cell {:mdl [:12]}
                            (board state))))
       (mdl/cell {:mdl [:4]}
                 (manual)
                 [:p]
                 (mdl/card {:mdl [:shadow--2dp]}
                           (mdl/card-title "Setup")
                           (mdl/card-action
                            [:div
                             [:p
                              (str "Width (" width ")")
                              (mdl/slider {:value width
                                           :min 1
                                           :max game/max-board-width
                                           :on-change #(update-setup! :board-setup/width ((comp js/parseInt value) %))})]
                             [:p
                              (str "Height (" height ")")
                              (mdl/slider {:value height
                                           :min 1
                                           :max game/max-board-height
                                           :on-change #(update-setup! :board-setup/height ((comp js/parseInt value) %))})]
                             [:p
                              (str "Probability of a block (" (* 100 probability) "%)")
                              (mdl/slider {:value (* 100 probability)
                                           :min 0
                                           :max 100
                                           :on-change #(update-setup! :board-setup/probability (/ ((comp js/parseInt value) %) 100.0))})]
                             [:p
                              (mdl/button {:mdl [:colored :raised :ripple]
                                           :on-click new-game!}
                                          "New game")
                              (mdl/button {:mdl [:colored :raised :ripple]
                                           :on-click restart-game!}
                                          "Reset")]]))))))))
