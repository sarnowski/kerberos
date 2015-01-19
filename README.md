# kerberos

A Clojure library for Kerberos authentication. Also includes ring middleware
for SPNEGO authentication.

## Usage

An example with Ring and Compojure:

    (ns kerberos-spnego.core.handler
      (:require [clojure.pprint :refer [pprint]]
                [compojure.core :refer :all]
                [compojure.route :as route]
                [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
                [sarnowski.kerberos :as kerberos]
                [sarnowski.kerberos.spnego :as spnego]))

    (def service-subject (kerberos/create-service-subject
                           "HTTP/local.example.com@EXAMPLE.COM"
                           (kerberos/create-config
                             {:useKeyTab true
                              :keyTab "resources/local.keytab"
                              :principal "HTTP/local.example.com@EXAMPLE.COM"
                              :storeKey true
                              :doNotPrompt true
                              :debug true
                              :isInitiator false})))

    (defroutes app-routes
      (GET "/" r (with-out-str (pprint r)))
      (route/not-found "Not Found"))

    (def app
      (-> app-routes
          (wrap-defaults site-defaults)
          (spnego/authenticate service-subject)))


## Run Tests

To run tests, you need to provide a keytab and credentials for your test KDC.

    $ JVM_OPTS="-Dkerberos.principal=HTTP/local.example.com@EXAMPLE.COM
                -Dkerberos.keytab=../local.keytab
                -Dkerberos.username=myuser@EXAMPLE.COM
                -Dkerberos.password=test123" \
            lein test

## License

Copyright (c) 2015, Tobias Sarnowski

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
PERFORMANCE OF THIS SOFTWARE.
