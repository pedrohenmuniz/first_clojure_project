(ns nos-codamos.logic.credit-card
  (:require [schema.core :as s]
            [nos-codamos.models.credit-card :as m.credit-card]
            [nos-codamos.models.common :as m.common]))

(s/defn add-credit-card :- m.credit-card/CreditCards
  "Append a new card in an array of cards"
  [cards :- m.credit-card/CreditCards, card :- m.credit-card/CreditCard]
  (conj cards card))

(s/defn new-credit-card :- m.credit-card/CreditCard
  [id :- java.util.UUID
   number :- s/Str
   csc :- s/Str
   expiration-date :- java.time.LocalDate
   limit :- java.math.BigDecimal
   ]
  {:credit-card/id              id
    :credit-card/number          number
    :credit-card/csc             csc
    :credit-card/expiration-date expiration-date
    :credit-card/limit           limit
    })
