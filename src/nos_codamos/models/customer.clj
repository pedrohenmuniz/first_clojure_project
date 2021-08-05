(ns nos-codamos.models.customer
  (:require [schema.core :as s]
            [nos-codamos.models.credit-card :as m.credit-card]))

(s/def Customer {:name s/Str, :cpf s/Str, :email s/Str, (s/optional-key :credit-cards) m.credit-card/CreditCards})
(s/def Customers [Customer])
