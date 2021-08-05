(ns nos-codamos.models.credit-card
  (:require [schema.core :as s]
            [nos-codamos.models.purchase :as m.purchase]
            [nos-codamos.models.common :as m.common]))

(s/def CreditCard {:number s/Str, :csc s/Str, :expiration-date m.common/YearMonth, :limit s/Int, (s/optional-key :purchases) m.purchase/Purchases})
(s/def CreditCards [CreditCard])
