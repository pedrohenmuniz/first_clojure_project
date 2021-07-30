(ns nos_codamos.db)

(use 'java-time)

(def customer {:name "John Doe", :cpf "12345678910", :email "jhon.doe@email.com"})
(def card {:number "123456", :csc "123", :expiration (zoned-date-time 2023 10 20), :limit 10000})
(def purchases [{:date (zoned-date-time 2021 10 20), :amount 1000, :merchant "Adidas", :category "Vestuário"}
                {:date (zoned-date-time 2021 03 13), :amount 40, :merchant "Burger King", :category "Restaurante"}
                {:date (zoned-date-time 2021 05 29), :amount 300, :merchant "Nike", :category "Vestuário"}])
