(ns nos_codamos.main
  (:require [nos_codamos.db :as nc.db]
            [nos_codamos.logic :as nc.logic]))

(println "Custo total com 'Vestuário':" (nc.logic/sum-purchases nc.db/purchases "Vestuário"))
(println "Custo total com 'Restaurante':" (nc.logic/sum-purchases nc.db/purchases "Restaurante"))
(println "Custo total:" (nc.logic/sum-purchases nc.db/purchases))

(println (nc.logic/associates nc.db/customer nc.db/card nc.db/purchases))


(println "Compra com o valor de 40:"(nc.logic/get-purchase-by-amount nc.db/purchases 40))
(println "Compra com o valor de 1000:" (nc.logic/get-purchase-by-amount nc.db/purchases 1000))
(println "Compra com o valor de 555:" (nc.logic/get-purchase-by-amount nc.db/purchases 555))
(println "Compra no estabelecimento 'burger':" (nc.logic/get-purchase-by-merchant nc.db/purchases "burger"))
(println "Compra no estabelecimento 'XPTO':" (nc.logic/get-purchase-by-merchant nc.db/purchases "XPTO"))
