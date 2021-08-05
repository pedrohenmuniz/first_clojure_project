(ns nos-codamos.logic.validations
  (:require [schema.core :as s]
            [nos-codamos.models.common :as m.common]))

(s/defn is-purchase-date-valid? :- s/Bool
  [expiration-date :- m.common/YearMonth, purchase-date :- m.common/LocalDate]
  (let [purchase-date-year-and-month (java.time.YearMonth/from purchase-date)
        chrono-months (java.time.temporal.ChronoUnit/MONTHS)
        diff-in-months (.until purchase-date-year-and-month expiration-date chrono-months)]
    (>= diff-in-months 0)))

(s/defn is-credit-card-limit-valid? :- s/Bool
  [credit-card-limit :- m.common/NumGreaterOrEqualThanZero, purchase-amount :- m.common/NumGreaterOrEqualThanZero]
  (>= credit-card-limit purchase-amount))

(s/defn is-purchase-valid? :- s/Bool
  [limit? :- s/Bool, is-purchase-date-valid? :- s/Bool]
  (and (limit? is-purchase-date-valid?)))
