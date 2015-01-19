(ns sarnowski.kerberos-test
  (:require [clojure.test :refer :all]
            [sarnowski.kerberos :refer :all]))

(deftest configuration-test
  (testing "defaults"
    (let [config (create-config {})
          entry (first (.getAppConfigurationEntry config "foo"))]
      (is (= (.getLoginModuleName entry) "com.sun.security.auth.module.Krb5LoginModule"))
      (is (= (str (.getControlFlag entry)) "LoginModuleControlFlag: required"))))

  (testing "custom"
    (let [config (create-config {:controlFlag :sarnowski.kerberos/OPTIONAL})
          entry (first (.getAppConfigurationEntry config "foo"))]
      (is (= (.getLoginModuleName entry) "com.sun.security.auth.module.Krb5LoginModule"))
      (is (= (str (.getControlFlag entry)) "LoginModuleControlFlag: optional")))))

(deftest login-service-test
  (let [config (create-config {:useKeyTab true
                               :keyTab (System/getProperty "kerberos.keytab")
                               :principal (System/getProperty "kerberos.principal")
                               :doNotPrompt true
                               :debug true
                               :isInitiator true})]
    (create-service-subject (System/getProperty "kerberos.principal") config)))

(deftest login-user-test
  (let [config (create-config {:debug true
                               :isInitiator true})]
    (login-user (System/getProperty "kerberos.username")
                (System/getProperty "kerberos.password")
                config)))
