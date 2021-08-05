(ns nos-codamos.logic.credit-card
  (:require [schema.core :as s]
            [nos-codamos.models.credit-card :as m.credit-card]))

(s/defn add-credit-card :- m.credit-card/CreditCards
  "Append a new card in an array of cards"
  [cards :- m.credit-card/CreditCards, card :- m.credit-card/CreditCard]
  (conj cards card))
