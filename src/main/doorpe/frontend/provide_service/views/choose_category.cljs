(ns doorpe.frontend.provide-service.views.choose-category
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.components.no-data-found :refer [no-data-found]]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def categories (reagent/atom {:categories nil}))

(defn render-categories
 [{:keys [_id name description img]}]
  [:> Card {:variant :outlined
            :style {:max-width :350px
                    :margin "10px"}}
   [:> CardContent
    [:img {:src img
           :height :200px
           :style {:width "100%"}}]
    [:> Typography
     name]

    [:br]
    [:> Typography {:variant :caption}
     description]

    [:br]

    [:> Button {:variant :contained
                :color :primary
                :on-click #(swap! db/app-db update-in [:provide-service] assoc :category-id _id)}
     "Select"]]])

(defn fetch-categories
  []
  (go
    (let [url (str backend-domain "/all-categories")
          res (<! (http/get url {:with-credentials? false}))
          _ (swap! categories assoc :categories (:body res))])))

(defn choose-category
  []
  (let [_ (fetch-categories)]
    (fn []
      (let [categories (:categories @categories)]
        [:div {:style {:display :flex}}
         (if (pos? (count categories))
           `[:<> ~@(map render-categories categories)]
           [no-data-found])]))))