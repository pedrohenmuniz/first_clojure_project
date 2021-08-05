(ns nos-codamos.models.purchase
  (:require [schema.core :as s]
            [nos-codamos.models.common :as m.common]))

(s/def Purchase {:date m.common/LocalDate :amount s/Int :merchant s/Str :category s/Str})
(s/def Purchases [Purchase])

;; (s/def TestBigDecimal {:date BigDecimal :amount s/Int :merchant s/Str :category s/Str})
;(def Pants
;  {:length         long
;   :color          s/Str
;   :price          BigDecimal})
;(s/validate Pants (c/complete {:color "green"} Pants))

;; (s/validate Pants (c/complete {:color "green"} Pants {} {BigDecimal (clojure.test.check.generators/fmap #(BigDecimal. (str %)) clojure.test.check.generators/double)}))
