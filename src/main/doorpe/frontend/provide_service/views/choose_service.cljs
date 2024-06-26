(ns doorpe.frontend.provide-service.views.choose-service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.components.no-data-found :refer [no-data-found]]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def services (reagent/atom {:services nil}))

(defn render-services
  [{:keys [_id name description critical-service charge-type img]}]
  [:> Card {:variant :outlined
            :style {:max-width :350px
                    :margin "30px"}}
   [:> CardContent
    [:img {:src img
           :style {:height :250px}}]
    [:> Typography {:variant :button}
     name]
    [:br]

    [:> Typography {:variant :caption}
     description]

    [:br]

    [:> Button {:variant :contained
                :color :primary
                :on-click #(do
                             (swap! db/app-db update-in [:provide-service] assoc :service-id _id)
                             (swap! db/app-db update-in [:provide-service] assoc :by-default-critical-service? critical-service)
                             (swap! db/app-db update-in [:provide-service] assoc :charge-type charge-type))}
     "Select"]]])

(defn fetch-services
  []
  (go
    (let [category-id (get-in @db/app-db [:provide-service :category-id])
          url (str backend-domain "/provide-service-by-category-id/" category-id)
          res (<! (http/get url {:with-credentials? false
                                 :headers {"Authorization" (auth/set-authorization)}}))
          _ (swap! services assoc :services (:body res))])))

(defn choose-service
  []
  (let [_ (fetch-services)]
    (fn []
      (let [services (:services @services)]
        [:<>
         [:> Button {:variant :contained
                     :color :secondary
                     :on-click #(swap! db/app-db update-in [:provide-service] dissoc :category-id)}
          "Go Back"]
         [:div {:style {:display :flex}}
          (if (pos? (count services))
            `[:<> ~@(map render-services services)]
            [no-data-found])]]))))