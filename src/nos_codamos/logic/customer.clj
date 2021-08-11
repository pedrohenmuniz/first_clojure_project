(ns nos-codamos.logic.customer
  (:require [schema.core :as s]
            [nos-codamos.models.customer :as m.customer]
            [nos-codamos.models.credit-card :as m.credit-card]))

(s/defn add-customer :- m.customer/Customers
  "Append a new customer in an array of customers"
  [customers :- m.customer/Customers, customer :- m.customer/Customer]
  (conj customers customer))

(s/defn new-customer :- m.customer/Customer
  "Return a map with new customer data"
  ([id :- java.util.UUID
    name :- s/Str
    cpf :- s/Str
    email :- s/Str
    credit-cards :- m.credit-card/CreditCards]

   {:customer/id           id
    :customer/name         name
    :customer/cpf          cpf
    :customer/email        email
    :customer/credit-cards credit-cards})
  ([id :- java.util.UUID
    name :- s/Str
    cpf :- s/Str
    email :- s/Str]

   {:customer/id    id
    :customer/name  name
    :customer/cpf   cpf
    :customer/email email}))
