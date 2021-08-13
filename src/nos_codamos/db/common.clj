(ns nos-codamos.db.common
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [schema.core :as s]
            [nos-codamos.models.customer :as m.customer]
            [nos-codamos.models.credit-card :as m.credit-card]
            [nos-codamos.models.purchase :as m.purchase]))

(def db-uri "datomic:dev://localhost:4334/noscodamos")

(def schema [
             ; Customer
             {:db/ident       :customer/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity
              :db/doc         "User id (uuid)"}
             {:db/ident       :customer/name
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "User name"}
             {:db/ident       :customer/cpf
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "User individual registry identification"
              }
             {:db/ident       :customer/email
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "User email"
              }
             {:db/ident       :customer/credit-cards
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/many
              :db/doc         "User's credit cards"}

             ; Credit card
             {:db/ident       :credit-card/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity
              :db/doc         "Credit card id (uuid)"}
             {:db/ident       :credit-card/number
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Credit card number"}
             {:db/ident       :credit-card/csc
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Credit card security number"}
             {:db/ident       :credit-card/expiration-date
              ;:db/valueType   :db.type/instant
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Credit card expiration date"}
             {:db/ident       :credit-card/limit
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one
              :db/doc         "Credit card limit"}
             {:db/ident       :credit-card/purchases
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/many
              :db/doc         "Credit card purchases"}

             ; Purchase
             {:db/ident       :purchase/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity
              :db/doc         "Purchase id (uuid)"}
             {:db/ident       :purchase/date
              :db/valueType   :db.type/string
              ;:db/valueType   :db.type/instant
              :db/cardinality :db.cardinality/one
              :db/doc         "Purchase date"}
             {:db/ident       :purchase/amount
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one
              :db/doc         "Purchase amount"}
             {:db/ident       :purchase/merchant
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Purchase merchant"}
             {:db/ident       :purchase/category
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Purchase category"}
             ])


(defn start-conn!
  []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn delete-db!
  []
  (d/delete-database db-uri))


(defn create-schema!
  [conn]
  (d/transact conn schema))


; Adds
(s/defn add-customers!
  "Adds a list of customers to datomic db"
  [conn, customers :- m.customer/Customers]
  (d/transact conn customers))

(s/defn add-credit-cards!
  "Adds a list of credit cards to datomic db"
  [conn, credit-cards :- m.credit-card/CreditCards]
  (d/transact conn credit-cards))

(s/defn add-purchases!
  "Adds a list of purchases to datomic db"
  [conn, purchases :- m.purchase/Purchases]
  (d/transact conn purchases))


(s/defn db-adds-for-assign-credit-cards
  [customer :- m.customer/Customer, credit-cards :- m.credit-card/CreditCards]
  (reduce (fn [db-adds credit-card]                         ; accumulator is the FIRST argument to function
            (conj db-adds [:db/add                          ; function definition
                           [:customer/id (:customer/id customer)] ;entity-id
                           :customer/credit-cards           ;attribute
                           [:credit-card/id (:credit-card/id credit-card)]])) ;value
          []                                                ; initial value for accumulator
          credit-cards))                                    ; collection to operate on

(s/defn db-adds-for-assign-purchases
  [credit-card :- m.credit-card/CreditCard, purchases :- m.purchase/Purchases]
  (reduce (fn [accumulator current-purchase]                ; accumulator is the FIRST argument to function
            (conj accumulator [:db/add                      ; function definition
                               [:credit-card/id (:credit-card/id credit-card)] ;entity-id
                               :credit-card/purchases       ;attribute
                               [:purchase/id (:purchase/id current-purchase)] ;value
                               ]))
          []                                                ; initial value for accumulator
          purchases))                                       ; collection to operate on


(s/defn assign-credit-cards!
  "Assign multiples credit cards to a single customer"
  [conn, customer :- m.customer/Customer, credit-cards :- m.credit-card/CreditCards]
  (let [to-transact (db-adds-for-assign-credit-cards customer credit-cards)]
    (d/transact conn to-transact)))


(s/defn assign-credit-card-simple!
  "A simple function to add a credit card to a customer"
  [conn, customer :- m.customer/Customer, credit-cards :- m.credit-card/CreditCards]
  (d/transact conn [[:db/add
                     [:customer/id (:customer/id customer)]
                     :customer/credit-cards
                     [:credit-card/id (:credit-card/id (first credit-cards))]]]))



(s/defn assign-purchases!
  "Assign multiples purhcases to a single credit card"
  [conn, credit-card :- m.credit-card/CreditCard, purchases :- m.purchase/Purchases]
  (d/transact conn (db-adds-for-assign-purchases credit-card purchases)))

; Queries
(s/defn get-all-customers
  "Fetch all customers from database"
  [db]
  (d/q '[:find (pull ?entity [*])
         :where [?entity :customer/id]] db))

(s/defn get-all-credit-cards
  "Fetch all credit cards from database"
  [db]
  (d/q '[:find (pull ?entity [*])
         :where [?entity :credit-card/id]] db))

(s/defn get-all-purchases
  "Fetch all purchases from database"
  [db]
  (d/q '[:find (pull ?entity [*])
         :where [?entity :purchase/id]] db))

(s/defn get-all-purchases-from-credit-card
  "Get all purchases from a credit card (Forward Navigation)"
  [db, credit-card :- m.credit-card/CreditCard]
  (d/q '[:find (pull ?entity [{:credit-card/purchases [*]}])
         :in $ ?queried-credit-card
         :where [?entity :credit-card/id ?queried-credit-card]
         ] db credit-card))

;TODO Fix function below
(s/defn get-all-purchases-from-credit-card-backward
  [db, credit-card :- m.credit-card/CreditCard]
  (d/q '[:find (pull ?entity [{:credit-card/_purchases [*]}])
         :in $ ?queried-credit-card
         :where [?entity :purchase/id ?purchase]]
       db credit-card))

; TODO Criar funções para gerar relatórios
;  TODO Cliente que realizou o maior número de compras
;  TODO Cliente que realizou a compra de maior valor
;  TODO Clientes que nunca realizaram compras

;TODO Implementar funcão abaixo
(s/defn customer-who-bought-the-most
  "Fetch customer who made the most purchases"
  [db]
  (d/q '[:find (count ?purchase)
         :keys purchase
         :where [?credit-card :credit-card/id]
                [?credit-card :credit-card/purchases ?purchase]
         ] db))

(s/defn customer-who-made-the-most-expensive-purchase
  "Fetch customer who made the most expensive purchases"
  [db]
  (d/q '[:find (pull ?customer [*])
         :in $ ?amount
         :where [?customer :customer/amount ?amount]] db))

(s/defn customer-who-made-the-most-expensive-purchase-backward
  "Fetch customer who made the most expensive purchases"
  [db uuid]
  (d/q '[:find (pull ?purchase [{:credit-card/_purchases [{:customer/_credit-cards [:customer/name]}]}])
         ;:with ?purchase
         :in $ ?uuid
         :keys customer
         :where [?purchase :purchase/id ?uuid]] db uuid))

(s/defn customer-who-made-the-most-expensive-purchase-nested
  "Fetch customer who made the most expensive purchases"
  [db]
  (d/q '[:find [(pull ?purchase [{:credit-card/_purchases [{:customer/_credit-cards [:customer/name]}]}])]
         ;:with ?purchase
         :in $
         :keys customer
         :where [(q '[:find (max ?amount)
                      :where [_ :purchase/amount ?amount]] $) [[?amount]]]
         [?purchase :purchase/amount ?amount]] db))



;[#datom[13194139534321 50 #inst "2021-08-12T18:13:46.841-00:00" 13194139534321 true]
; #datom[17592186045426 83 #uuid "4d1ad239-2788-4d84-854f-b0af1d5ac1e9" 13194139534321 true]
; #datom[17592186045426 84 "2021-10-20" 13194139534321 true]
; #datom[17592186045426 85 1000M 13194139534321 true]
; #datom[17592186045426 86 "Adidas" 13194139534321 true]
; #datom[17592186045426 87 "Vestuário" 13194139534321 true]
; #datom[17592186045427 83 #uuid "233ccb61-51d9-4bef-bcdc-43fdd6c0344e" 13194139534321 true]
; #datom[17592186045427 84 "2021-03-27" 13194139534321 true]
; #datom[17592186045427 85 40M 13194139534321 true]
; #datom[17592186045427 86 "Burger King" 13194139534321 true]
; #datom[17592186045427 87 "Restaurante" 13194139534321 true]
; #datom[17592186045428 83 #uuid "e6bb2c93-1953-4e01-9a2b-8c4504a1f8cf" 13194139534321 true]
; #datom[17592186045428 84 "2021-05-29" 13194139534321 true] #datom[17592186045428 85 300M 13194139534321 true]
; #datom[17592186045428 86 "Nike" 13194139534321 true] #datom[17592186045428 87 "Vestuário" 13194139534321 true]
; #datom[17592186045429 83 #uuid "1f1f4144-0290-4a91-8aea-f94240802e4c" 13194139534321 true]
; #datom[17592186045429 84 "2021-05-29" 13194139534321 true] #datom[17592186045429 85 25M 13194139534321 true]
; #datom[17592186045429 86 "Mc Donalds" 13194139534321 true] #datom[17592186045429 87 "Restaurante" 13194139534321 true]
; #datom[17592186045430 83 #uuid "0467f09a-4b44-46b7-83d5-e9829ce2ca25" 13194139534321 true]
; #datom[17592186045430 84 "2021-05-29" 13194139534321 true] #datom[17592186045430 85 200M 13194139534321 true]
; #datom[17592186045430 86 "Lupo" 13194139534321 true] #datom[17592186045430 87 "Vestuário" 13194139534321 true]],
