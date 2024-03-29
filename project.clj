(defproject nos-codamos "0.1.0-SNAPSHOT"
  :description "Treinamento Nós Codamos"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [clojure.java-time "0.3.2"]
                 [prismatic/schema "1.1.12"]
                 [prismatic/schema-generators "0.1.3"]
                 [org.clojure/test.check "1.1.0"]
                 [com.datomic/datomic-pro "1.0.6269"]]
  :repl-options {:init-ns nos-codamos.core})
