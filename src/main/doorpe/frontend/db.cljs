(ns doorpe.frontend.db
  (:require [reagent.core :as reagent]))

(def app-db (reagent/atom {:my-profile nil
                           :my-bookings nil
                           :book-service nil
                           :provide-service nil
                           :admin-add ""
                           :admin-edit ""}))