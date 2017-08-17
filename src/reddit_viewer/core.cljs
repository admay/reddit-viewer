(ns reddit-viewer.core
    (:require
     [ajax.core :as ajax]
     [reagent.core :as r]
     [reddit-viewer.chart :as chart]
     [reddit-viewer.controllers]
     [re-frame.core :as rf]))

;; -------------------------
;; Views

(defn sort-posts [title sort-key]
  [:button.btn.btn-secondary
   {:on-click #(rf/dispatch [:sort-posts sort-key])}
   (str "sort posts by " title)])

(defn pick-sub []
  [:input {:type "text"
           :value sub
           :on-change #(rf/dispatch [:select-sub sub])}])

(defn home-page []
  (let [view @(rf/subscribe [:view])
        sub @(rf/subscribe [:sub])]
    [:div
     [navbar view]
     [:div.card>div.card-block
      [:div.btn-group
       [sort-posts "score" score]
       [sort-posts "comments" :num_comments]]
      (case view
        :chart [chart/chart-posts-by-votes]
        :posts [display-posts @(rf/subscribe [:posts])])]]))











;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:intialize-db])
  (rf/dispatch [:load-posts "pictures"])
  (mount-root))
