(ns nos-codamos.logic.customer
  (:require [schema.core :as s]
            [nos-codamos.models.customer :as m.customer]))

(s/defn add-customer :- m.customer/Customers
  "Append a new customer in an array of customers"
  [customers :- m.customer/Customers, customer :- m.customer/Customer]
  (conj customers customer))
