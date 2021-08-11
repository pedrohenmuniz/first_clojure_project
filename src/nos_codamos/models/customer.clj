(ns nos-codamos.models.customer
  (:require [schema.core :as s]
            [nos-codamos.models.credit-card :as m.credit-card]))

(s/def Customer {:customer/id                            java.util.UUID
                 :customer/name                          s/Str
                 :customer/cpf                           s/Str
                 :customer/email                         s/Str
                 (s/optional-key :customer/credit-cards) m.credit-card/CreditCards})

(s/def Customers [Customer])
