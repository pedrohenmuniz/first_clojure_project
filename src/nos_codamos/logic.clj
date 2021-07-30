(ns nos_codamos.logic)

(use 'java-time)


(defn format-date
  "Recebe um campo do tipo 'date' e o retorna no formato 'dd/MM/YYYY'"
  [date]
  (format "dd/MM/YYYY" date))

(defn return-categories
  "Recebe uma coleção de compras, e retorna uma coleção com todas as categorias existentes (distintas entre si)"
  [purchases]
  (->> purchases (map :category) distinct))

(defn get-purchase-by-category
  "Recebe um coleção de itens e uma categoria e retorna os itens que pertencem a essa categoria"
  [purchases category]
  (->> purchases
       (filter #(= category (-> % :category)))))

(defn sum-purchases
  ([purchases]
   (->> purchases
        (map :amount)
        (reduce +)))
  ([purchases category]
   (-> purchases
       (get-purchase-by-category category)
       (->> (map :amount)
            (reduce +)))))

(defn associates
  [customer, card, purchases]
  (assoc-in customer [:card] (assoc-in card [:purchases] purchases)))

(defn get-purchase-by-amount
  [purchase amount]
  (->> purchase
       (filter #(= (% :amount) amount))))

(defn get-purchase-by-merchant
  [purchase merchant]
  (->> purchase
       (filter #(clojure.string/includes? (clojure.string/lower-case (% :merchant)) merchant))))
