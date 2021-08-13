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
(def customer2 (l.customer/new-customer (l.common/generate-uuid) "Richard Doe" "063.092.700-60" "richard.doe@email.com"))
(pprint (s/validate m.customer/Customer customer1))

;(println "Java Local Date: " (class (java.time.LocalDate/parse "2022-08-31")))
;(def credit-card1 (l.credit-card/new-credit-card (l.common/generate-uuid) "5183 7662 5914 6292" "392" (java.time.LocalDate/parse "2022-08-31") 15000M))
(def credit-card0 (l.credit-card/new-credit-card (l.common/generate-uuid) "5183 7662 5914 6292" "392" "2022-08-31" 15000M))
(def credit-card1 (l.credit-card/new-credit-card (l.common/generate-uuid) "4716 0015 2964 1339" "432" "2023-04-31" 1000M))
(def credit-card2 (l.credit-card/new-credit-card (l.common/generate-uuid) "1111 2222 3333 4444" "123" "2024-07-31" 500M))
;(pprint (s/validate m.credit-card/CreditCard credit-card1))

(def purchase0 (l.purchase/new-purchase (l.common/generate-uuid) "2021-10-20" 1000M "Adidas" "Vestuário"))
(def purchase1 (l.purchase/new-purchase (l.common/generate-uuid) "2021-03-27" 40M "Burger King" "Restaurante"))
(def purchase2 (l.purchase/new-purchase (l.common/generate-uuid) "2021-05-29" 300M "Nike" "Vestuário"))
(def purchase3 (l.purchase/new-purchase (l.common/generate-uuid) "2021-05-29" 25M "Mc Donalds" "Restaurante"))
(def purchase4 (l.purchase/new-purchase (l.common/generate-uuid) "2021-05-29" 200M "Lupo" "Vestuário"))
(def purchase5 (l.purchase/new-purchase (l.common/generate-uuid) "2021-05-29" 666M "Lupo" "Vestuário"))
;(pprint (s/validate m.purchase/Purchase purchase1))

(def customers [customer0, customer1, customer2])
(def credit-cards [credit-card0, credit-card1, credit-card2])
(def purchases [purchase0, purchase1, purchase2, purchase3, purchase4, purchase5])


; Add customers and credit-cards into database
(pprint @(db.common/add-customers! conn customers))
(pprint @(db.common/add-credit-cards! conn credit-cards))
(pprint @(db.common/add-purchases! conn purchases))



(println "\nCustomers before assign credit card:")
(pprint (db.common/get-all-customers (d/db conn)))


(pprint @(db.common/assign-credit-cards! conn customer0 [credit-card0, credit-card1]))
(pprint @(db.common/assign-credit-cards! conn customer1 [credit-card2]))
(println "\nCustomers after assign credit card:")
(pprint (db.common/get-all-customers (d/db conn)))

;(println "db-adds for assign credit-cards")
;(pprint (db.common/db-adds-for-assign-credit-cards customer0 credit-cards))
;(println "db-adds for assign purchases")
;(pprint (db.common/db-adds-for-assign-purchases credit-card0 purchases))

(pprint @(db.common/assign-purchases! conn credit-card0 [purchase0, purchase1, purchase2]))
(pprint @(db.common/assign-purchases! conn credit-card2 [purchase3, purchase4]))

(println "\nCustomers after assign purchases to credit-card:")
(pprint (db.common/get-all-customers (d/db conn)))

(println "\nCredit cards after assign purchases to it:")
(pprint (db.common/get-all-credit-cards (d/db conn)))

(println "\n All purchases from database:")
(pprint (db.common/get-all-purchases (d/db conn)))


(println "\nPurchases from credit-card 0:")
(pprint (db.common/get-all-purchases-from-credit-card (d/db conn) (:credit-card/id credit-card0)))
(println "\nPurchases from credit-card 1:")
(pprint (db.common/get-all-purchases-from-credit-card (d/db conn) (:credit-card/id credit-card1)))
(println "\nPurchases from credit-card 2:")
(pprint (db.common/get-all-purchases-from-credit-card (d/db conn) (:credit-card/id credit-card2)))

(println "\nCustomer who bought the most:")
(pprint (db.common/customer-who-bought-the-most (d/db conn)))

(println "\nCustomer who made the most expensive purchase")
(def customer-most-expensive-purchase (db.common/customer-who-made-the-most-expensive-purchase-nested (d/db conn)))

(pprint (-> customer-most-expensive-purchase
            :customer
            ))
(pprint (db.common/customer-who-made-the-most-expensive-purchase-nested (d/db conn)))

;(db.common/delete-db!)




; TODO Você precisa escrever uma função clojure que salve os dados do cartão de crédito e de seu cliente no banco de dados Datomic. (OK)
; TODO Você precisa escrever uma função clojure que salve no banco de dados Datomic as compras realizadas no cartão de crédito de um cliente. (OK)
; TODO Você precisa escrever uma função em clojure que recupere do banco de dados Datomic as compras realizadas em um cartão de crédito. (OK)
; TODO Criar funções para gerar relatórios
;  TODO Cliente que realizou o maior número de compras
;  TODO Cliente que realizou a compra de maior valor
;  TODO Clientes que nunca realizaram compras