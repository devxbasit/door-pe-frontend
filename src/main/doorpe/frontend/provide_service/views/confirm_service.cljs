(ns doorpe.frontend.provide-service.views.confirm-service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [reagent.core :as reagent]
            [doorpe.frontend.components.util :refer [two-br]]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.auth.auth :as auth]
            ["@material-ui/core" :refer [Grid Container Paper Typography Label Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn dispatch-provide-service
  [{:keys [service-charges charges experience service-intro degree-title]}]
  (go (let [url (str backend-domain "/provide-service")
            provide-service (:provide-service @db/app-db)
            by-default-critical-service? (:by-default-critical-service? provide-service)
            degree-holder-consent? (:degree-holder-consent? provide-service)
            charge-type (:charge-type provide-service)
            service-id (:service-id provide-service)
            base-doc {:service-id service-id
                      :service-charges service-charges
                      :experience experience
                      :service-intro service-intro}
            degree  (if (or by-default-critical-service? degree-holder-consent?)
                      (let [my-file  (-> (.getElementById js/document "my-file")
                                         .-files first)]
                        (merge base-doc {:professional-degree-holder true
                                         :degree-title degree-title
                                         :my-file my-file}))
                      (merge base-doc {:professional-degree-holder false}))
            params (if (= "fixed" charge-type)
                     (merge degree {:charges charges})
                     degree)

            res (<! (http/post url {:with-credentials? false
                                    :headers {"Authorization" (auth/set-authorization)}
                                    :multipart-params (seq params)}))]
        (accountant/navigate! "/dashboard"))))

(defn confirm-service
  []
  (let [provide-service (:provide-service @db/app-db)
        by-default-critical-service? (:by-default-critical-service? provide-service)
        degree-holder-consent? (:degree-holder-consent? provide-service)
        charge-type (:charge-type provide-service)
        initial-vaules {:service-charges "" :charges "" :experience "" :service-intro "" :degree-tittle ""}
        values (reagent/atom initial-vaules)]
    [:> Container {:maxWidth "sm"}
     [:> Paper {:variant :outlined
                :square true}
      [:> Typography
       "provide service"]

      [:br]

      [:> TextField {:variant :outlined
                     :label "Service Carges"
                     :type :number
                     :on-change #(swap! values assoc :service-charges (.. % -target -value))}]
      [two-br]

      (if (= "fixed" charge-type)
        [:> TextField {:variant :outlined
                       :label "Charges"
                       :type :number
                       :on-change #(swap! values assoc :charges (.. % -target -value))}]

        [:> TextField {:variant :outlined
                       :label "Charges on Inspection"
                       :type :number
                       :disabled true
                       :error true}])

      [two-br]

      [:> TextField {:variant :outlined
                     :label "Experience"
                     :type :number
                     :on-change #(swap! values assoc :experience (.. % -target -value))
                     :helper-text "*Experience in years"}]
      [two-br]

      [:> TextField {:variant :outlined
                     :label "Service Introduction"
                     :type :text
                     :on-change #(swap! values assoc :service-intro (.. % -target -value))
                     :placeholder " e.g Hi! my name is Asif, I'm expert ..."
                     :multiline true
                     :rows 10
                     :fullWidth true
                     :helperText "Please be introduce yourself to your audience"}]
      [two-br]

      (if (or by-default-critical-service? degree-holder-consent?)
        [:<>
         [:> TextField {:variant :outlined
                        :label "Degree/course Title"
                        :type :text
                        :on-change #(swap! values assoc :degree-title (.. % -target -value))
                        :helper-text "e.g 2 years Diploma in / 3 month training in  - "}]
         [two-br]

         [:input {:type :file
                  :id :my-file
                  :required true}]

         [two-br]])

      [:> Button {:variant :contained
                  :color :primary
                  :on-click #(dispatch-provide-service @values)}
       "Register"]]]))
