(ns kerberos.core-test
  (:require [clojure.test :refer :all]
            [kerberos.core :refer :all]))

(deftest configuration-test
  (let [config (create-config {})
        entry (first (.getAppConfigurationEntry config "foo"))]
    (is (= (.getLoginModuleName entry) "com.sun.security.auth.module.Krb5LoginModule"))
    (is (= (str (.getControlFlag entry)) "LoginModuleControlFlag: required"))))
