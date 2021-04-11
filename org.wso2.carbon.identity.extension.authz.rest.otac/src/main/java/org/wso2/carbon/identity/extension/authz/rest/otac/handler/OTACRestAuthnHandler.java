package org.wso2.carbon.identity.extension.authz.rest.otac.handler;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.auth.service.AuthenticationContext;
import org.wso2.carbon.identity.auth.service.AuthenticationResult;
import org.wso2.carbon.identity.auth.service.AuthenticationStatus;
import org.wso2.carbon.identity.auth.service.handler.AuthenticationHandler;
import org.wso2.carbon.identity.core.bean.context.MessageContext;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.extension.authz.rest.otac.Constants;
import org.wso2.carbon.identity.extension.authz.rest.otac.dao.OneTimeAuthzCodeDAO;
import org.wso2.carbon.identity.extension.authz.rest.otac.exception.OneTimeAuthzCodeRestAuthServerException;
import org.wso2.carbon.identity.extension.authz.rest.otac.internal.DataHolder;
import org.wso2.carbon.identity.extension.authz.rest.otac.model.OTACRestAuthenticationResult;
import org.wso2.carbon.identity.extension.authz.rest.otac.model.OneTimeAuthzCode;

import java.sql.Timestamp;
import java.util.Date;

/**
 * One-time-authorization-code authentication handler
 */
public class OTACRestAuthnHandler extends AuthenticationHandler {

    private final OneTimeAuthzCodeDAO dao = DataHolder.getInstance().getDAO();

    public boolean canHandle(MessageContext messageContext) {

        if (IdentityUtil.threadLocalProperties.get() != null
                && IdentityUtil.threadLocalProperties.get().get(Constants.OTAC_RESPONSE_FACADE) != null) {
            return true;
        }
        return false;
    }

    @Override
    protected AuthenticationResult doAuthenticate(MessageContext messageContext) throws OneTimeAuthzCodeRestAuthServerException {

        AuthenticationResult result = new OTACRestAuthenticationResult(AuthenticationStatus.FAILED,
                Constants.OTAC_FAILURE.UNKNOWN);
        AuthenticationContext authenticationContext = (AuthenticationContext) messageContext;
        String otac = authenticationContext.getAuthenticationRequest().getHeader(Constants.OTAC_HEADER_NAME);
        if(StringUtils.isNotEmpty(otac)) {
            validateSecret(otac);
            result.setAuthenticationStatus(AuthenticationStatus.SUCCESS);
            flagOTACAuthn();
        }
        return result;
    }

    private AuthenticationResult validateSecret(String secret) throws OneTimeAuthzCodeRestAuthServerException {

        OneTimeAuthzCode oneTimeAuthzCode = dao.getOTAC(secret);
        Timestamp currentTime = new java.sql.Timestamp(new Date().getTime());
        if (oneTimeAuthzCode.getSecret() == null) {
            return new OTACRestAuthenticationResult(AuthenticationStatus.FAILED, Constants.OTAC_FAILURE.INVALID);
        } else if (currentTime.after(oneTimeAuthzCode.getExpiry())) {
            return new OTACRestAuthenticationResult(AuthenticationStatus.FAILED, Constants.OTAC_FAILURE.EXPIRED);
        } else if (StringUtils.equals(oneTimeAuthzCode.getSecretStatus(), Constants.OTAC_STATE.INACTIVE.toString())) {
            return new OTACRestAuthenticationResult(AuthenticationStatus.FAILED, Constants.OTAC_FAILURE.INACTIVE);
        }
        return new OTACRestAuthenticationResult(AuthenticationStatus.SUCCESS);
    }

    private void flagOTACAuthn () {

        IdentityUtil.threadLocalProperties.get().put("isOTACAuthn", true);
    }
}
