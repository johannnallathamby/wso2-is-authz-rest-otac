package org.wso2.carbon.identity.extension.authz.rest.otac.exception;

import org.wso2.carbon.identity.auth.service.exception.AuthServerException;

/**
 * Create exceptions for server side errors
 */
public class OneTimeAuthzCodeRestAuthServerException extends AuthServerException {

    private String errorCode;

    public OneTimeAuthzCodeRestAuthServerException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public OneTimeAuthzCodeRestAuthServerException(String errorCode, String message, Throwable throwable) {
        super(message, throwable);

    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
