(ns kerberos.core
  (:import (javax.security.auth.login Configuration AppConfigurationEntry AppConfigurationEntry$LoginModuleControlFlag)))

(defn- stringify-config [config]
  (into {} (map (fn [[k v]] [(name k) (str v)]) config)))

(def ^:private control-flags {::REQUIRED AppConfigurationEntry$LoginModuleControlFlag/REQUIRED
                              ::REQUISITE AppConfigurationEntry$LoginModuleControlFlag/REQUISITE
                              ::SUFFICIENT AppConfigurationEntry$LoginModuleControlFlag/SUFFICIENT
                              ::OPTIONAL AppConfigurationEntry$LoginModuleControlFlag/OPTIONAL})

(defn create-config [jaas-config]
  (let [config (stringify-config (dissoc jaas-config :controlFlag))
        control-flag (if (:controlFlag jaas-config) (:controlFlag jaas-config) ::REQUIRED)]
    (proxy [Configuration] []
      (getAppConfigurationEntry [_]
        (into-array AppConfigurationEntry
                    [(AppConfigurationEntry. "com.sun.security.auth.module.Krb5LoginModule"
                                             (get control-flags control-flag)
                                             config)])))))

