package org.wso2.carbon.identity.extension.authz.rest.otac.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.sql.Timestamp;

/**
 * Row of data from the database to create One-Time-Authorization-Code
 */
public class OneTimeAuthzCode {

    private String id;
    private String secret;
    private Timestamp expiry;
    private String secretStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Timestamp getExpiry() {
        return expiry;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setExpiry(Timestamp expiry) {
        this.expiry = expiry;
    }

    public String getSecretStatus() {
        return secretStatus;
    }

    public void setSecretStatus(String secretStatus) {
        this.secretStatus = secretStatus;
    }

}
