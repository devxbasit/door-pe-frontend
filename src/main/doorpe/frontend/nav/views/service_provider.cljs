(ns doorpe.frontend.nav.views.service-provider
  (:require ["@material-ui/core" :refer [CssBaseline Box Typography Paper Tabs Container Button AppBar Link Menu MenuItem]]
            [doorpe.frontend.nav.nav-links :refer [nav-link]]
            [accountant.core :as accountant]
            [doorpe.frontend.auth.auth :as auth]))

(defn service-provider
  []
  [:<>
   [:> CssBaseline
    [:> Box {:style {:background-color :royalblue}}
     [:> Container
      [nav-link {:text "Dashboard"
                 :on-click #(accountant/navigate! "/dashboard")}]
      [nav-link {:text "My Bookings"
                 :on-click #(accountant/navigate! "/my-bookings")}]
      [nav-link {:text "My Profile"
                 :on-click #(accountant/navigate! "/my-profile")}]
      [nav-link {:text "Logout"
                 :on-click #(accountant/navigate! "/logout")}]]]]])