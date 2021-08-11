(ns nos-codamos.main
  (:refer-clojure :exclude [range iterate format max min])
  (:use clojure.pprint)
  (:require [schema.core :as s]
            [nos-codamos.logic.purchase :as l.purchase]
            [nos-codamos.logic.credit-card :as l.credit-card]
            [nos-codamos.logic.customer :as l.customer]
            [nos-codamos.logic.common :as l.common]
            [nos-codamos.db.common :as db.common]
            [nos-codamos.models.customer :as m.customer]
            [nos-codamos.models.credit-card :as m.credit-card]
            [nos-codamos.models.purchase :as m.purchase]
            [java-time :as java.time]
            [datomic.api :as d]))

(s/set-fn-validation! false)



;(def customers [])
;(def customers (l.customer/add-customer customers {:name "John Doe", :cpf "847.101.200-64", :email "jhon.doe@email.com"}))
;(def customers (l.customer/add-customer customers {:name "Janie Doe", :cpf "647.651.790-28", :email "janie.doe@email.com"}))
;(println "Customers:")
;(pprint customers)
;
;(def credit-cards [])
;(def credit-cards (l.credit-card/add-credit-card credit-cards {:number "5183 7662 5914 6292", :csc "392", :expiration-date (java.time.YearMonth/parse "2022-08"), :limit 15000}))
;(def credit-cards (l.credit-card/add-credit-card credit-cards {:number "4716 0015 2964 1339", :csc "432", :expiration-date (java.time.YearMonth/parse "2023-04"), :limit 1000}))
;(println "Credit Cards:")
;(pprint credit-cards)
;
;(def purchases [])
;(def purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-10-20"), :amount 1000, :merchant "Adidas", :category "Vestuário"}))
;(def purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-03-27"), :amount 40, :merchant "Burger King", :category "Restaurante"}))
;(def purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-05-29"), :amount 300, :merchant "Nike", :category "Vestuário"}))
;(println "Purchases:")
;(pprint purchases)
;
;(def customerAssoc (l.common/associates-purchase-card-and-customer (customers 0) credit-cards 0 purchases))
;(println "Customers Associated:")
;(pprint customerAssoc)
;
;
;(def customerPurchases (l.purchase/get-all-purchases-from-a-customer customerAssoc 0))
;(println "Purchases from Customer 0:")
;(pprint customerPurchases)
;
;
;(def sumCustomerPurchases (l.purchase/sum-purchases customerPurchases))
;(println "Sum of customer purchases:")
;(pprint sumCustomerPurchases)
;
;(def sumCustomerPurchasesByCategory (l.purchase/sum-purchases customerPurchases "Vestuário"))
;(println "Sum of customer purchases from category 'Vestuário':")
;(pprint sumCustomerPurchasesByCategory)
;
;(pprint (l.purchase/get-purchase-by-amount customerPurchases 300))
;;; (pprint (l.purchase/get-purchase-by-amount customerPurchases "300"))
;(pprint (l.purchase/get-purchase-by-merchant customerPurchases "adidas"))

(println "\n\n\n************Starting using datomic db************\n\n\n")
(db.common/delete-db!)
(def conn (db.common/start-conn!))

(pprint (db.common/create-schema! conn))

;(def uuid (l.common/generate-uuid))
;(println "UUID Gerado: " uuid)

;(def customer1 (l.customer/new-customer ((l.common/generate-uuid) "John Doe" "847.101.200-64" "jhon.doe@email.com")))
(def customer0 (l.customer/new-customer (l.common/generate-uuid) "John Doe" "847.101.200-64" "jhon.doe@email.com"))
(def customer1 (l.customer/new-customer (l.common/generate-uuid) "Janie Doe" "647.651.790-28" "janie.doe@email.com"))
(pprint (s/validate m.customer/Customer customer1))

;(println "Java Local Date: " (class (java.time.LocalDate/parse "2022-08-31")))
;(def credit-card1 (l.credit-card/new-credit-card (l.common/generate-uuid) "5183 7662 5914 6292" "392" (java.time.LocalDate/parse "2022-08-31") 15000M))
(def credit-card0 (l.credit-card/new-credit-card (l.common/generate-uuid) "5183 7662 5914 6292" "392" "2022-08-31" 15000M))
(def credit-card1 (l.credit-card/new-credit-card (l.common/generate-uuid) "4716 0015 2964 1339" "432" "2023-04-31" 1000M))
;(pprint (s/validate m.credit-card/CreditCard credit-card1))

(def purchase1 (l.purchase/new-purchase (l.common/generate-uuid) (java.time.LocalDate/parse "2021-10-20") 1000M "Adidas" "Vestuário"))
(def purchase2 (l.purchase/new-purchase (l.common/generate-uuid) (java.time.LocalDate/parse "2021-03-27") 40 "Burger King" "Restaurante"))
(def purchase3 (l.purchase/new-purchase (l.common/generate-uuid) (java.time.LocalDate/parse "2021-05-29") 300 "Nike" "Vestuário"))
;(pprint (s/validate m.purchase/Purchase purchase1))

(def customers [customer0, customer1])
(def credit-cards [credit-card0, credit-card1])


; Add customers and credit-cards into database
(pprint @(db.common/add-customers! conn customers))
(pprint @(db.common/add-credit-cards! conn credit-cards))



(println "Customers before assign credit cards")
(def db-customers (db.common/get-all-customers (d/db conn)))
(pprint db-customers)

;(def db-credit-cards (db.common/get-all-credit-cards (d/db conn)))
;(pprint db-credit-cards)

;(pprint @(db.common/assign-credit-cards! conn customer0 [credit-card0, credit-card1]))


(println "Customers after assign credit cards")
(def db-customers (db.common/get-all-customers (d/db conn)))
(pprint db-customers)

(def db-customers (db.common/get-all-customers-another-way (d/db conn)))
(pprint db-customers)

;(db.common/delete-db!)





(reduce (fn [db-adds credit-cards] (conj db-adds [:db/add
                                              [:customer/id (:customer/id customer0)]
                                              :customer/credit-cards
                                              [:credit-card/id (:credit-card/id credit-cards)]]))
        []
        [credit-card0, credit-card1])




;1 Você precisa escrever uma função clojure que salve os dados do cartão de crédito e de seu cliente no banco de dados Datomic.
;2 Você precisa escrever uma função clojure que salve no banco de dados Datomic as compras realizadas no cartão de crédito de um cliente.
;3 Você precisa escrever uma função em clojure que recupere do banco de dados Datomic as compras realizadas em um cartão de crédito.
;4 Criar funções para gerar relatórios
;  Cliente que realizou o maior número de compras
;  Cliente que realizou a compra de maior valor
;  Clientes que nunca realizaram compras