(ns nos-codamos.core
  (:use clojure.pprint)
  (:require [schema.core :as s]
            [clojure.test.check.generators :as gen]
            [schema-generators.generators :as g]
            [nos-codamos.models.purchase :as m.purchase]
            [nos-codamos.models.common :as m.common]))



;; (defn foo
;;   "I don't do a whole lot."
;;   [x]
;;   (println x "Hello, World!"))


;(use 'java-time)


;; (defn obter-mes-compra
;;   [compra]
;;   ((as (:data compra) :year :month-of-year)1))


;; (println "Obter-mes-compra:" (obter-mes-compra (compras 0)))

;; (def prop (property (local-date 2015 2 28) :month-of-year))
;; (println "year: " (as (local-date 2015 2 28) :year))
;; (println "month: " ((as (local-date 2015 2 28) :year :month-of-year) 1))
;; (println "month-of-year: " (as (local-date 2015 2 28) :month-of-year))
;; (println (as (local-date 2015 9 28) :year :month-of-year :day-of-month))


;(def customer {:name "John Doe", :cpf "12345678910", :email "jhon.doe@email.com"})
;(def card {:number "123456", :csc "123", :expiration "2023/10/20", :limit 10000})
;;; (def card {:number "123456", :csc "123", :expiration (zoned-date-time 2023 10 20), :limit 10000})
;;; (def purchases [{:date (zoned-date-time 2021 10 20), :amount 1000, :merchant "Adidas", :category "Vestuário"}
;;;                 {:date (zoned-date-time 2021 03 13), :amount 40, :merchant "Burger King", :category "Restaurante"}
;;;                 {:date (zoned-date-time 2021 05 29), :amount 300, :merchant "Nike", :category "Vestuário"}])
;(def purchases [{:date "2021/10/20", :amount 1000, :merchant "Adidas", :category "Vestuário"}
;                {:date "2021/03/13", :amount 40, :merchant "Burger King", :category "Restaurante"}
;                {:date "2021/05/29", :amount 300, :merchant "Nike", :category "Vestuário"}])
;
;
;;; models.clj
;(def Purchase {:date s/Str :amount s/Int :merchant s/Str :category s/Str})
;(def Purchases [Purchase])
;(def Card {:number s/Str, :csc s/Str, :expiration s/Str, :limit s/Int, (s/optional-key :purchases) Purchases})
;(def Cards [Card])
;(def Customer {:name s/Str, :cpf s/Str, :email s/Str, (s/optional-key :cards) Cards})
;(def Customers [Customer])
;
;
;    ;; tests
;;; (pprint (s/validate Customer {:name "John Doe", :cpf "12345678910", :email "jhon.doe@email.com"}))
;;; (pprint (s/validate Card {:number "123456", :csc "123", :expiration "2023/10/20", :limit 10000}))
;;; (pprint (s/validate Purchase {:date "2021/05/29", :amount 300, :merchant "Nike", :category "Vestuário"}))
;;; (pprint (s/validate Customers [{:name "John Doe", :cpf "12345678910", :email "jhon.doe@email.com"}]))
;;; (s/explain Card)
;;; (s/explain Customer)
;;; db.clj
;
;
;;; logic.clj
;(s/set-fn-validation! true)
;
;(s/defn add-customer :- Customers
;  "Append a new customer in an array of customers"
;  [customers :- Customers, customer :- Customer]
;  (conj customers customer))
;
;(s/defn add-card :- Cards
;  "Append a new card in an array of cards"
;  [cards :- Cards, card :- Card]
;  (conj cards card))
;
;(s/defn add-purchase :- Purchases
;  "Append a new purchase in an array of purchases"
;  [purchases :- Purchases, purchase :- Purchase]
;  (conj purchases purchase))
;
;(s/defn associates-purchase-card-and-customer :- Customer
;  "Associates a array of purchases in a card of a customer"
;  [customer :- Customer, cards :- Cards, card-idx :- s/Int, purchases :- Purchases]
;  (assoc-in customer [:cards] (assoc-in cards [card-idx :purchases] purchases)))
;
;(s/defn get-all-purchases-from-a-customer :- Purchases
;  "Return all purchases from a card of a customer"
;  [customer :- Customer, card-idx :- s/Int]
;  ;; (-> customer
;  ;;     :cards
;  ;;     card-idx
;  ;;     :purchases
;  ;;     ))
;  (get (get (get customer :cards) card-idx) :purchases))
;
;(s/defn get-purchase-by-category :- Purchases
;  "Recebe um coleção de itens e uma categoria e retorna os itens que pertencem a essa categoria"
;  [purchases :- Purchases, category :- s/Str]
;  (->> purchases
;       (filter #(= category (-> % :category)))))
;
;(s/defn sum-purchases :- s/Int
;  ([purchases :- Purchases]
;   (->> purchases
;        (map :amount)
;        (reduce +)))
;  ([purchases :- Purchases, category :- s/Str]
;   (-> purchases
;       (get-purchase-by-category category)
;       (->> (map :amount)
;            (reduce +)))))
;
;(defn format-date
;  "Recebe um campo do tipo 'date' e o retorna no formato 'dd/MM/YYYY'"
;  [date]
;  (format "dd/MM/YYYY" date))
;
;(defn return-categories
;  "Recebe uma coleção de compras, e retorna uma coleção com todas as categorias existentes (distintas entre si)"
;  [purchases]
;  (->> purchases (map :category) distinct))
;
;
;
;(defn associates-deprecated
;  [customer, card, purchases]
;  (assoc-in customer [:cards] (assoc-in card [:purchases] purchases)))
;
;(defn get-purchase-by-amount
;  [purchase amount]
;  (->> purchase
;       (filter #(= (% :amount) amount))))
;
;(defn get-purchase-by-merchant
;  [purchase merchant]
;  (->> purchase
;       (filter #(clojure.string/includes? (clojure.string/lower-case (% :merchant)) merchant))))
;
;
;;; main.clj
;;; (def customers (add-customer [] {:name "John Doe", :cpf "12345678910", :email "jhon.doe@email.com"}))
;(def customers (add-customer [] {:name "John Doe", :cpf "12345678910", :email "jhon.doe@email.com"}))
;(def customers (add-customer customers {:name "Janie Doe", :cpf "98765432100", :email "janie.doe@email.com"}))
;(println "Customers:")
;(pprint customers)
;(def cards (add-card [] {:number "1123 2222 3333 4444", :csc "665", :expiration "2025/05/20", :limit 15000}))
;(def cards (add-card cards {:number "9949 2212 2341 3349", :csc "123", :expiration "2023/10/20", :limit 1000}))
;(println "Cards:")
;(pprint cards)
;(def purchases (add-purchase [] {:date "2021/10/20", :amount 1000, :merchant "Adidas", :category "Vestuário"}))
;(def purchases (add-purchase purchases {:date "2021/03/27", :amount 40, :merchant "Burger King", :category "Restaurante"}))
;(def purchases (add-purchase purchases {:date "2021/05/29", :amount 300, :merchant "Nike", :category "Vestuário"}))
;(println "Purchases:")
;(pprint purchases)
;
;(def customerAssoc (associates-purchase-card-and-customer (customers 0) cards 0 purchases))
;(println "Customers Associated:")
;(pprint customerAssoc)
;
;
;(def customerPurchases (get-all-purchases-from-a-customer customerAssoc 0))
;(println "Purchases from Customer 0:")
;(pprint customerPurchases)
;
;
;(def sumCustomerPurchases (sum-purchases customerPurchases))
;(println "Sum of customer purchases:")
;(pprint sumCustomerPurchases)
;
;(def sumCustomerPurchasesByCategory (sum-purchases customerPurchases "Vestuário"))
;(println "Sum of customer purchases from category 'Vestuário':")
;(pprint sumCustomerPurchasesByCategory)

;; O que esse código precisa fazer?

;; Teste da função que adiciona uma compra na lista de compras realizadas;
;; Teste da função que lista as compras realizadas;
;; Teste da função que realiza o cálculo dos gastos agrupados por categoria.
;; Bom projeto!


;(pprint (gen/sample gen/int 5))
;(pprint (gen/sample gen/string 5))
;(pprint (gen/sample gen/string-alphanumeric 5))
;(pprint (gen/sample (gen/vector gen/int 5 15) 5))
;
;(pprint (g/sample 10 m.purchase/Teste))
;(pprint (g/sample 10 m.purchase/TesteObj))
;(pprint (g/sample 10 m.purchase/Pegadinha ))
;;; (pprint (g/sample 10 m.purchase/Purchase ))
;;; (pprint (g/sample 10 m.purchase/Purchase {java.time.LocalDate (clojure.test.check.generators/fmap #(java.time.LocalDate. (str %)) clojure.test.check.generators/string-alphanumeric)}))
;(pprint (g/sample 10 m.purchase/Purchase {java.time.LocalDate (clojure.test.check.generators/fmap #(java.time.LocalDate. (str %)) clojure.test.check.generators/string-alphanumeric)}))
;;; (pprint (g/sample 10 m.purchase/TestBigDecimal ))
;(pprint (g/sample 10 m.purchase/TestBigDecimal {BigDecimal (clojure.test.check.generators/fmap #(BigDecimal. (str %)) clojure.test.check.generators/double)}))
;;; (pprint (g/generate m.purchase/Purchase))
;
;;;