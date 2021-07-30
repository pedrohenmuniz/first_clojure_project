(ns nos_codamos.core)



(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))


;; (use 'java-time)


;; (defn obter-mes-compra
;;   [compra]
;;   ((as (:data compra) :year :month-of-year)1))


;; (println "Obter-mes-compra:" (obter-mes-compra (compras 0)))

;; (def prop (property (local-date 2015 2 28) :month-of-year))
;; (println "year: " (as (local-date 2015 2 28) :year))
;; (println "month: " ((as (local-date 2015 2 28) :year :month-of-year) 1))
;; (println "month-of-year: " (as (local-date 2015 2 28) :month-of-year))
;; (println (as (local-date 2015 9 28) :year :month-of-year :day-of-month))
