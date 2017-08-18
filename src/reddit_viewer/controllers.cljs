(ns reddit-viewer.controllers
  (:require
    [ajax.core :as ajax]
    [re-frame.core :as rf]))

(rf/reg-event-db
  :initialize-db
  (fn [_ _]
    {:view     :posts
     :sort-key :score
     :sub "pictures"}))

(defn find-posts-with-preview [posts]
  (filter #(= (:post_hint %) "image") posts))

(rf/reg-event-db
  :set-posts
  (fn [db [_ posts]]
    (assoc db :posts
              (->> (get-in posts [:data :children])
                   (map :data)
                   (find-posts-with-preview)))))

(rf/reg-event-db
  :load-posts
  (fn [db [_ sub]]
    (ajax/GET (str "http://www.reddit.com/r/" sub ".json?count=500")
              {:handler         #(rf/dispatch [:set-posts %])
               :error-handler   #(.log js/console (str "The sub: " sub " could not be reached"))
               :response-format :json
               :keywords?       true})
    (assoc db :sub sub)))

(rf/reg-event-db
  :sort-posts
  (fn [db [_ sort-key]]
    (update db :posts (partial sort-by sort-key >))))

(rf/reg-event-db
  :select-view
  (fn [db [_ view]]
    (assoc db :view view)))

(rf/reg-event-db
 :select-sub
 (fn [db [_ sub]]
   (do
     (rf/dispatch [:load-posts sub])
     (assoc db :sub sub))))

(rf/reg-sub
  :view
  (fn [db _]
    (:view db)))

(rf/reg-sub
  :posts
  (fn [db _]
    (:posts db)))

(rf/reg-sub
 :sub
 (fn [db _]
   (:sub db)))
