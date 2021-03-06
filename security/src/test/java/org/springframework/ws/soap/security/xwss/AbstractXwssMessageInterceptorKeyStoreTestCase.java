/*
 * Copyright 2005-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ws.soap.security.xwss;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public abstract class AbstractXwssMessageInterceptorKeyStoreTestCase extends AbstractXwssMessageInterceptorTestCase {

    protected X509Certificate certificate;

    protected PrivateKey privateKey;

    @Override
    protected void onSetup() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream("test-keystore.jks");
            keyStore.load(is, "password".toCharArray());
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
        certificate = (X509Certificate) keyStore.getCertificate("alias");
        privateKey = (PrivateKey) keyStore.getKey("alias", "password".toCharArray());

    }

}
