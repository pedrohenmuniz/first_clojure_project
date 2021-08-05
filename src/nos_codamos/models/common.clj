(ns nos-codamos.models.common
  (:require [schema.core :as s]))

(s/def LocalDate java.time.LocalDate)
(s/def YearMonth java.time.YearMonth)

(s/defn greater-or-equal-than-zero
  [x]
  (>= x 0))

(s/def NumGreaterOrEqualThanZero
  (s/constrained s/Num greater-or-equal-than-zero 'greater-or-equal-than-zero))
