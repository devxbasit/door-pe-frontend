(ns doorpe.frontend.app
  (:require [reagent.dom :as reagent-dom]
            [secretary.core :as secretary]
            [accountant.core :as accountant]
            [reagent.core :as reagent]

            [doorpe.frontend.nav.nav :refer [nav]]
            [doorpe.frontend.footer.footer :refer [footer]]

            [doorpe.frontend.home-page.home-page :refer [home-page]]
            [doorpe.frontend.register.views.customer :as register-customer]
            [doorpe.frontend.book-complaint.book-complaint :refer [book-complaint]]
            [doorpe.frontend.feedback.feedback :refer [feedback]]
            [doorpe.frontend.about-us.about-us :refer [about-us]]
            [doorpe.frontend.contact-us.contact-us :refer [contact-us]]
            [doorpe.frontend.auth.login :as login]

            [doorpe.frontend.dashboard.dashboard :refer [dashboard]]
            [doorpe.frontend.my-profile.my-profile :refer [my-profile]]
            [doorpe.frontend.my-bookings.my-bookings :refer [my-bookings]]
            [doorpe.frontend.auth.logout :refer [logout]]))

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

  (secretary/defroute "/register/as-customer" []
    (reset! page #'register-customer/customer))

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

  (secretary/defroute "/my-profile" []
    (reset! page #'my-profile))

  (secretary/defroute "/logout" []
    (reset! page #'logout)))

(defn ^:dev/after-load start
  []
  (app-routes)
  (accountant/configure-navigation!
   {:nav-handler (fn [path]
                   (secretary/dispatch! path))
    :path-exists? (fn [path]
                    (secretary/locate-route path))
    :reload-same-path? true})

  (reagent-dom/render [current-page]
                      (.getElementById js/document "application")))

(defn ^:export init
  []
  (start))