(ns nos-codamos.logic.purchase
  (:require [schema.core :as s]
            [nos-codamos.models.purchase :as m.purchase]
            [nos-codamos.models.customer :as m.customer]
            [nos-codamos.logic.validations :as l.validations]
            [nos-codamos.models.common :as m.common]))

(s/defn add-purchase :- m.purchase/Purchases
  "Append a new purchase in an array of purchases"
  [purchases :- m.purchase/Purchases, purchase :- m.purchase/Purchase]
  (conj purchases purchase))

(s/defn new-purchase :- m.purchase/Purchase
  [id :- java.util.UUID
   date :- m.common/LocalDate
   amount :- java.math.BigDecimal
   merchant :- s/Str
   category :- s/Str]
  {:purchase/id id
   :purchase/date date
   :purchase/amount amount
   :purchase/merchant merchant
   :purchase/category category})

(s/defn get-all-purchases-from-a-customer :- m.purchase/Purchases
  "Return all purchases from a card of a customer"
  [customer :- m.customer/Customer, card-idx :- s/Int]
  ;; (-> customer
  ;;     :cards
  ;;     card-idx
  ;;     :purchases
  ;;     ))
  (get (get (get customer :credit-cards) card-idx) :purchases))

(s/defn get-purchase-by-category :- m.purchase/Purchases
  "Recebe um coleção de itens e uma categoria e retorna os itens que pertencem a essa categoria"
  [purchases :- m.purchase/Purchases, category :- s/Str]
  (->> purchases
       (filter #(= category (-> % :category))))
  )

(s/defn get-purchase-by-amount :- m.purchase/Purchases
  [purchase :- m.purchase/Purchases, amount :- s/Int]
  (->> purchase
       (filter #(= (% :amount) amount))))

(s/defn get-purchase-by-merchant :- m.purchase/Purchases
  [purchase :- m.purchase/Purchases, merchant :- s/Str]
  (->> purchase
       (filter #(clojure.string/includes? (clojure.string/lower-case (% :merchant)) merchant))))

(s/defn sum-purchases :- s/Int
  ([purchases :- m.purchase/Purchases]
   (->> purchases
        (map :amount)
        (reduce +)))
  ([purchases :- m.purchase/Purchases, category :- s/Str]
   (-> purchases
       (get-purchase-by-category category)
       (->> (map :amount)
            (reduce +)))))
