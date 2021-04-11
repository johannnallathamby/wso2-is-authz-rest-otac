package org.wso2.carbon.identity.extension.authz.rest.otac.model;

import org.wso2.carbon.identity.auth.service.AuthenticationResult;
import org.wso2.carbon.identity.auth.service.AuthenticationStatus;
import org.wso2.carbon.identity.extension.authz.rest.otac.Constants;

public class OTACRestAuthenticationResult extends AuthenticationResult {

    private final Constants.OTAC_FAILURE reason;

    public Constants.OTAC_FAILURE getReason() {
        return reason;
    }

    public OTACRestAuthenticationResult (AuthenticationStatus status) {
        super(status);
        this.reason = Constants.OTAC_FAILURE.UNKNOWN;
    }

    public OTACRestAuthenticationResult (AuthenticationStatus status, Constants.OTAC_FAILURE reason) {
        super(status);
        if(reason == null) {
            reason = Constants.OTAC_FAILURE.UNKNOWN;
        }
        this.reason = reason;
    }
}
