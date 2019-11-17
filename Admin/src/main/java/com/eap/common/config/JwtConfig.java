package com.eap.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "environments.jwt")
public class JwtConfig {
    private String header;
    private String secert;
    private int expiration;
    private int outExpiration;
    private String keyUsername;
    private String keyCreated;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSecert() {
        return secert;
    }

    public void setSecert(String secert) {
        this.secert = secert;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public int getOutExpiration() {
        return outExpiration;
    }

    public void setOutExpiration(int outExpiration) {
        this.outExpiration = outExpiration;
    }

    public String getKeyUsername() {
        return keyUsername;
    }

    public void setKeyUsername(String keyUsername) {
        this.keyUsername = keyUsername;
    }

    public String getKeyCreated() {
        return keyCreated;
    }

    public void setKeyCreated(String keyCreated) {
        this.keyCreated = keyCreated;
    }
}
