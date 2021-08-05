(ns nos-codamos.main
  (:use clojure.pprint)
  (:require [schema.core :as s]
            [nos-codamos.logic.purchase :as l.purchase]
            [nos-codamos.logic.credit-card :as l.credit-card]
            [nos-codamos.logic.customer :as l.customer]
            [nos-codamos.logic.common :as l.common]))

(s/set-fn-validation! true)

(def customers [])
(def customers (l.customer/add-customer customers {:name "John Doe", :cpf "847.101.200-64", :email "jhon.doe@email.com"}))
(def customers (l.customer/add-customer customers {:name "Janie Doe", :cpf "647.651.790-28", :email "janie.doe@email.com"}))
(println "Customers:")
(pprint customers)

(def credit-cards [])
(def credit-cards (l.credit-card/add-credit-card credit-cards {:number "5183 7662 5914 6292", :csc "392", :expiration-date (java.time.YearMonth/parse "2022-08"), :limit 15000}))
(def credit-cards (l.credit-card/add-credit-card credit-cards {:number "4716 0015 2964 1339", :csc "432", :expiration-date (java.time.YearMonth/parse "2023-04"), :limit 1000}))
(println "Credit Cards:")
(pprint credit-cards)

(def purchases [])
(def purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-10-20"), :amount 1000, :merchant "Adidas", :category "Vestuário"}))
(def purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-03-27"), :amount 40, :merchant "Burger King", :category "Restaurante"}))
(def purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-05-29"), :amount 300, :merchant "Nike", :category "Vestuário"}))
(println "Purchases:")
(pprint purchases)

(def customerAssoc (l.common/associates-purchase-card-and-customer (customers 0) credit-cards 0 purchases))
(println "Customers Associated:")
(pprint customerAssoc)


(def customerPurchases (l.purchase/get-all-purchases-from-a-customer customerAssoc 0))
(println "Purchases from Customer 0:")
(pprint customerPurchases)


(def sumCustomerPurchases (l.purchase/sum-purchases customerPurchases))
(println "Sum of customer purchases:")
(pprint sumCustomerPurchases)

(def sumCustomerPurchasesByCategory (l.purchase/sum-purchases customerPurchases "Vestuário"))
(println "Sum of customer purchases from category 'Vestuário':")
(pprint sumCustomerPurchasesByCategory)

(pprint (l.purchase/get-purchase-by-amount customerPurchases 300))
;; (pprint (l.purchase/get-purchase-by-amount customerPurchases "300"))
(pprint (l.purchase/get-purchase-by-merchant customerPurchases "adidas"))
