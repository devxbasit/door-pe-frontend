(ns doorpe.frontend.app
  (:require [reagent.dom :as reagent-dom]
            [secretary.core :as secretary]
            [accountant.core :as accountant]
            [reagent.core :as reagent]
            [clerk.core :as clerk]

            [doorpe.frontend.nav.nav :refer [nav]]
            [doorpe.frontend.footer.footer :refer [footer]]

            [doorpe.frontend.home-page.home-page :refer [home-page]]
            [doorpe.frontend.register.register :refer [register]]
            [doorpe.frontend.register-customer.register-customer :refer [register-customer]]
            [doorpe.frontend.register-service-provider.register-service-provider :refer [register-service-provider]]
            [doorpe.frontend.book-complaint.book-complaint :refer [book-complaint]]
            [doorpe.frontend.check-complaints.check-complaints :refer [check-complaints]]
            [doorpe.frontend.feedback.feedback :refer [feedback]]
            [doorpe.frontend.about-us.about-us :refer [about-us]]
            [doorpe.frontend.contact-us.contact-us :refer [contact-us]]
            [doorpe.frontend.auth.login :as login]

            [doorpe.frontend.dashboard.dashboard :refer [dashboard]]
            [doorpe.frontend.my-profile.my-profile :refer [my-profile]]
            [doorpe.frontend.my-bookings.my-bookings :refer [my-bookings]]
            [doorpe.frontend.book-service.book-service :refer [book-service]]
            [doorpe.frontend.provide-service.provide-service :refer [provide-service]]
            [doorpe.frontend.pending-dues.pending-dues :refer [pending-dues]]
            [doorpe.frontend.pay-dues.pay-dues :refer [pay-dues]]
            [doorpe.frontend.auth.logout :refer [logout]]

            [doorpe.frontend.admin-add.admin-add :refer [admin-add]]
            [doorpe.frontend.admin-edit.admin-edit :refer [admin-edit]]
            [doorpe.frontend.service-requests.service-requests :refer [service-requests]]
            [doorpe.frontend.revenue-generated.revenue-generated :refer [revenue-generated]]))

(defonce page (reagent/atom #'home-page))

(defn current-page
  []
  [:div
   [nav]
   [@page]
   [footer]])

(defn app-routes
  []
  (secretary/defroute "/" []
    (reset! page #'home-page))

  (secretary/defroute "/register" []
    (reset! page #'register))

  (secretary/defroute "/register/customer" []
    (reset! page #'register-customer))

  (secretary/defroute "/register/service-provider" []
    (reset! page #'register-service-provider))

  (secretary/defroute "/login" []
    (reset! page #'login/login))

  (secretary/defroute "/book-complaint" []
    (reset! page #'book-complaint))

  (secretary/defroute "/about-us" []
    (reset! page #'about-us))

  (secretary/defroute "/contact-us" []
    (reset! page #'contact-us))

  (secretary/defroute "/feedback" []
    (reset! page #'feedback))

  (secretary/defroute "/dashboard" []
    (reset! page #'dashboard))

  (secretary/defroute "/my-bookings" []
    (reset! page #'my-bookings))

  (secretary/defroute "/book-service" []
    (reset! page #'book-service))

  (secretary/defroute "/provide-service" []
    (reset! page #'provide-service))

  (secretary/defroute "/pending-dues" []
    (reset! page #'pending-dues))

  (secretary/defroute "/pay-dues" []
    (reset! page #'pay-dues))

  (secretary/defroute "/my-profile" []
    (reset! page #'my-profile))

  (secretary/defroute "/logout" []
    (reset! page #'logout))

  (secretary/defroute "/admin-add" []
    (reset! page #'admin-add))

  (secretary/defroute "/admin-edit" []
    (reset! page #'admin-edit))

  (secretary/defroute "/service-requests" []
    (reset! page #'service-requests))

  (secretary/defroute "/check-complaints" []
    (reset! page #'check-complaints))

  (secretary/defroute "/revenue-generated" []
    (reset! page #'revenue-generated)))

(defn ^:dev/after-load start
  []
  (app-routes)
  (clerk/initialize!)
  (accountant/configure-navigation!
   {:nav-handler (fn [path]
                   (reagent/after-render clerk/after-render!)
                   (secretary/dispatch! path)
                   (clerk/navigate-page! path))
    :path-exists? (fn [path]
                    (secretary/locate-route path))
    :reload-same-path? true})

  (reagent-dom/render [current-page]
                      (.getElementById js/document "application")))

(defn ^:export init
  []
  (start))