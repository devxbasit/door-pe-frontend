(ns doorpe.frontend.book-service.views.choose-service-provider
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def service-providers (reagent/atom {:service-providers nil}))

(defn render-service-providers
  [{:keys [_id name district service-charges service-intro charges experience professional-degree-holder img]}]
  [:> Card {:variant :outlined
            :style {:max-width :400px
                    :margin "30px"}}
   [:> CardContent
    [:> Typography {:variant :button}
     name]
    [:br]
    [:div {:style {:text-align :center}}
     [:img {:src img
            :style {:height :100px}}]]

    [:br]
    [:> Typography {:variant :caption}
     service-intro]

    [:br]

    (if  (= "true" (str professional-degree-holder))
      [:> Typography {:variant :button
                      :style {:color "forestgreen"}}
       "Professional Degree Holder"])

    [:> Typography
     (str "Service Charges : " service-charges)]

    (if charges
      [:> Typography
       (str "Charges : " charges)]

      [:> Typography {:color :secondary
                      :variant :button}
       (str "Charges : On Inspection ")])

    [:> Typography
     (str "Experience : " experience)]

    [:> Typography
     (str "From : " district)]

    [:> Button {:variant :contained
                :color :primary
                :on-click #(do
                             (swap! db/app-db update-in [:book-service] assoc :service-provider-id _id)
                             (swap! db/app-db update-in [:book-service] assoc :service-charges service-charges)
                             (swap! db/app-db update-in [:book-service] assoc :charges charges))}
     "Select"]]])

(defn fetch-service-providers
  []
  (go
    (let [service-id (get-in @db/app-db [:book-service :service-id])
          url (str backend-domain "/all-service-providers-by-service-id/" service-id)
          res (<! (http/get url {:with-credentials? false}))
          _ (swap! service-providers assoc :service-providers (:body res))])))

(defn choose-service-provider
  []
  (let [_ (fetch-service-providers)]
    (fn []
      (let [service-providers (:service-providers @service-providers)]
        [:<>
         [:> Button {:variant :contained
                     :color :secondary
                     :on-click #(swap! db/app-db update-in [:book-service] dissoc :service-id)}
          "Go Back"]
         [:div {:style {:display :flex}}
          `[:<> ~@(map render-service-providers service-providers)]]]))))
