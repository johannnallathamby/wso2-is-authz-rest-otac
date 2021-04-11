package org.wso2.carbon.identity.extension.authz.rest.otac.exception;

import org.wso2.carbon.identity.auth.service.exception.AuthenticationFailException;

/**
 * Create exceptions for client side errors
 */
public class OneTimeAuthzCodeRestAuthFailException extends AuthenticationFailException {

    private String errorCode;
    private String description;

    public OneTimeAuthzCodeRestAuthFailException(String errorCode, String message, String description) {
        super(message);
        this.errorCode = errorCode;
        this.description = description;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
