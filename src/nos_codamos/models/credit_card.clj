(ns nos-codamos.models.credit-card
  (:require [schema.core :as s]
            [nos-codamos.models.purchase :as m.purchase]
            [nos-codamos.models.common :as m.common]))

(s/def CreditCard {:credit-card/id                         java.util.UUID
                   :credit-card/number                     s/Str
                   :credit-card/csc                        s/Str
                   :credit-card/expiration-date            java.time.LocalDate
                   :credit-card/limit                      java.math.BigDecimal
                   (s/optional-key :credit-card/purchases) m.purchase/Purchases})

(s/def CreditCards [CreditCard])
