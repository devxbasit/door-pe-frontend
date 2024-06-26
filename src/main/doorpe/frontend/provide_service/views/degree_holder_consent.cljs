(ns doorpe.frontend.provide-service.views.degree-holder-consent
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Paper Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn degree-holder-consent
  []
  [:> Container {:maxWidth :sm}
   [:> Button {:variant :contained
               :color :secondary
               :on-click #(swap! db/app-db update-in [:provide-service] dissoc :service-id)}
    "Go Back"]
   [:> Paper {:square true
              :style {:padding "20px"}}
    [:> Grid {:container true
              :spacing 10}
     [:> Grid {:item true
               :xs 12}
      [:> Typography "Degree Holder Consent"]]]

    [:> Grid {:container true
              :spacing 5}
     [:> Grid {:item true
               :xs 6}
      [:> Typography {:variant :caption}
       "Do you posses a valid professional degree?"]]
     [:> Grid {:item true
               :xs 6}

      [:> Grid {:container true}
       [:> Grid {:item true
                 :xs 6}
        [:> Button {:variant :outlined
                    :on-click #(swap! db/app-db update-in [:provide-service] assoc :degree-holder-consent? true)}
         "YES"]]
       [:> Grid {:item true
                 :xs 6}
        [:> Button {:variant :outlined
                    :on-click #(swap! db/app-db update-in [:provide-service] assoc :degree-holder-consent? false)}
         "NO"]]]]]]])
