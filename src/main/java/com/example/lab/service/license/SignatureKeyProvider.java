package com.example.lab.service.license;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class SignatureKeyProvider {

    @Value("${license.keystore.path}")
    private String keystorePath;

    @Value("${license.keystore.password}")
    private String keystorePassword;

    @Value("${license.key.alias}")
    private String keyAlias;

    private final AtomicReference<PrivateKey> cachedPrivateKey = new AtomicReference<>();
    private final AtomicReference<PublicKey> cachedPublicKey = new AtomicReference<>();

    private void loadKeys() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(
                    getClass().getClassLoader().getResourceAsStream(keystorePath),
                    keystorePassword.toCharArray()
            );

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(
                    keyAlias,
                    keystorePassword.toCharArray()
            );
            PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();

            if (privateKey == null) throw new IllegalStateException(
                    "Private key not found for alias: " + keyAlias
            );

            cachedPrivateKey.set(privateKey);
            cachedPublicKey.set(publicKey);

        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load keystore: " + e.getMessage(), e);
        }
    }

    public PrivateKey getPrivateKey() {
        if (cachedPrivateKey.get() == null) {
            synchronized (this) {
                if (cachedPrivateKey.get() == null) {
                    loadKeys();
                }
            }
        }
        return cachedPrivateKey.get();
    }

    public PublicKey getPublicKey() {
        if (cachedPublicKey.get() == null) {
            synchronized (this) {
                if (cachedPublicKey.get() == null) {
                    loadKeys();
                }
            }
        }
        return cachedPublicKey.get();
    }
}