(ns nos-codamos.logic.common
  (:require [schema.core :as s]
            [nos-codamos.models.purchase :as m.purchase]
            [nos-codamos.models.credit-card :as m.credit-card]
            [nos-codamos.models.customer :as m.customer]))

(s/defn associates-purchase-card-and-customer :- m.customer/Customer
  "Associates a array of purchases in a card of a customer"
  [customer :- m.customer/Customer, cards :- m.credit-card/CreditCards, card-idx :- s/Int, purchases :- m.purchase/Purchases]
  (assoc-in customer [:credit-cards] (assoc-in cards [card-idx :purchases] purchases)))

(defn generate-uuid [] (java.util.UUID/randomUUID))
