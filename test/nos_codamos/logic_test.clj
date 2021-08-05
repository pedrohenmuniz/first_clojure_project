(ns nos-codamos.logic-test
  (:use clojure.pprint)
  (:require [schema.core :as s]
            [clojure.test :refer :all]
            [nos-codamos.logic.purchase :as l.purchase]
            [nos-codamos.logic.customer :as l.customer]
            [nos-codamos.logic.credit-card :as l.credit-card]
            [nos-codamos.logic.common :as l.common]
            ;; [clojure.test.check.clojure-test :refer (defspec)]
            ;; [clojure.test.check.generateors :as gen]
            ;; [clojure.test.check.properties :as prop]
            ;; [schema-generators.generators :as g]
            ))



;; Teste da função que adiciona uma compra na lista de compras realizadas;
(deftest add-purchase-test
  (testing "Added a purchase into a empty purchases list"
    (let [purchases (l.purchase/add-purchase [] {:date (java.time.LocalDate/parse "2021-10-20"), :amount 1000, :merchant "Adidas", :category "Vestuário"})]
      (is (= 1 (count purchases)))))
  (testing "Added 2 purchases into a empty purchases list"
    (let [purchases (l.purchase/add-purchase [] {:date (java.time.LocalDate/parse "2021-10-20"), :amount 1000, :merchant "Adidas", :category "Vestuário"})
          purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-10-20"), :amount 1000, :merchant "Adidas", :category "Vestuário"})]
      (is (= 2 (count purchases))))))


(defn generate-customer-assoc
  []
  (let [customers []
        customers (l.customer/add-customer customers {:name "John Doe", :cpf "847.101.200-64", :email "jhon.doe@email.com"})
        customers (l.customer/add-customer customers {:name "Janie Doe", :cpf "647.651.790-28", :email "janie.doe@email.com"})
        credit-cards []
        credit-cards (l.credit-card/add-credit-card credit-cards {:number "5183 7662 5914 6292", :csc "392", :expiration-date (java.time.YearMonth/parse "2022-08"), :limit 15000})
        credit-cards (l.credit-card/add-credit-card credit-cards {:number "4716 0015 2964 1339", :csc "432", :expiration-date (java.time.YearMonth/parse "2023-04"), :limit 1000})
        purchases []
        purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-10-20"), :amount 1000, :merchant "Adidas", :category "Vestuário"})
        purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-03-27"), :amount 40, :merchant "Burger King", :category "Restaurante"})
        purchases (l.purchase/add-purchase purchases {:date (java.time.LocalDate/parse "2021-05-29"), :amount 300, :merchant "Nike", :category "Vestuário"})
        customerAssoc (l.common/associates-purchase-card-and-customer (customers 0) credit-cards 0 purchases)]
    customerAssoc))

;; Teste da função que lista as compras realizadas;
(deftest get-all-purchases-from-a-customer-test
  (testing "Getting a purchase from a valid customer and valid credit-card"
    (let [customerAssoc (generate-customer-assoc)]
      ;; (pprint (l.purchase/get-all-purchases-from-a-customer customerAssoc 0))
      (is (count (l.purchase/get-all-purchases-from-a-customer customerAssoc 0)))))
  (testing "Getting a purchase from a valid credit-card without purchases"
    (let [customerAssoc (generate-customer-assoc)
          purchases (l.purchase/get-all-purchases-from-a-customer customerAssoc 2)]
      (pprint customerAssoc)
      (pprint purchases)
      (is (= nil (l.purchase/get-all-purchases-from-a-customer customerAssoc 1)))))
  (testing "Getting a purchase from a invalid credit-card"
    (let [customerAssoc (generate-customer-assoc)
          purchases (l.purchase/get-all-purchases-from-a-customer customerAssoc 2)]
      (is (= nil (l.purchase/get-all-purchases-from-a-customer customerAssoc 2)))))
  ;; TODO: Use a trycatch?
  ;; (testing "Getting a purchase from a invalid credit-card"
  ;;   (let [customerAssoc (generate-customer-assoc)
  ;;         purchases (l.purchase/get-all-purchases-from-a-customer customerAssoc 2)]
  ;;     (is (= nil (l.purchase/get-all-purchases-from-a-customer {} 2)))
  ;;     )
  ;;   )
  )



;; Teste da função que realiza o cálculo dos gastos agrupados por categoria.
(deftest sum-purchases-test
  (testing "Associative property of addition"
    (let [customerAssoc (generate-customer-assoc)
          customerPurchases (l.purchase/get-all-purchases-from-a-customer customerAssoc 0)]
      (is (= (l.purchase/sum-purchases customerPurchases "Vestuário") (l.purchase/sum-purchases (reverse customerPurchases) "Vestuário")))))
  (testing "Category doesn't exist"
    (let [customerAssoc (generate-customer-assoc)
          customerPurchases (l.purchase/get-all-purchases-from-a-customer customerAssoc 0)]
      (is (= 0 (l.purchase/sum-purchases customerPurchases "XPTO"))))))

