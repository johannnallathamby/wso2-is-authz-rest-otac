package org.wso2.carbon.identity.extension.authz.rest.otac.valve;

import com.google.gson.JsonObject;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.catalina.valves.ValveBase;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.wso2.carbon.identity.auth.service.AuthenticationContext;
import org.wso2.carbon.identity.auth.service.AuthenticationStatus;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.extension.authz.rest.otac.Constants;
import org.wso2.carbon.identity.extension.authz.rest.otac.Utils;
import org.wso2.carbon.identity.extension.authz.rest.otac.dao.OneTimeAuthzCodeDAO;
import org.wso2.carbon.identity.extension.authz.rest.otac.exception.OneTimeAuthzCodeRestAuthServerException;
import org.wso2.carbon.identity.extension.authz.rest.otac.internal.DataHolder;
import org.wso2.carbon.identity.extension.authz.rest.otac.model.OTACRestAuthenticationResult;
import org.wso2.carbon.identity.extension.authz.rest.otac.model.OneTimeAuthzCode;
import org.wso2.carbon.identity.extension.authz.rest.otac.facade.OneTimeAuthzCodeRestAuthzResponseFacade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * One-time-authorization-code Validation Valve
 */
public class OneTimeAuthzCodeRestAuthzValve extends ValveBase {

    private static final Log log = LogFactory.getLog(OneTimeAuthzCodeRestAuthzValve.class);

    private final OneTimeAuthzCodeDAO dao = DataHolder.getInstance().getDAO();

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {

        if (!StringUtils.equals(request.getRequestURI(), Constants.PROTECTED_REQUEST_URI)) {
            try {

                /**
                 * Need to debug and see whether we need to create a wrapper for the actual HttpServletRequest too
                 */
                OneTimeAuthzCodeRestAuthzResponseFacade facade =
                        new OneTimeAuthzCodeRestAuthzResponseFacade(response);
                response.setResponse(facade);

                setThreadLocalFacade(facade);

                getNext().invoke(request, response);

                OTACRestAuthenticationResult authenticationResult = (OTACRestAuthenticationResult)
                        ((AuthenticationContext) request.getAttribute(Constants.AUTH_CONTEXT)).getAuthenticationResult();
                if (AuthenticationStatus.FAILED.equals(authenticationResult.getAuthenticationStatus())) {
                    buildAuthenticationFailureResponse(facade, authenticationResult);
                }

                OneTimeAuthzCode newOTAC = new OneTimeAuthzCode();
                newOTAC.setId(UUID.randomUUID().toString());
                newOTAC.setSecret(UUID.randomUUID().toString());
                newOTAC.setSecretStatus(Constants.OTAC_STATE.ACTIVE.toString());
                try {
                    newOTAC.setExpiry(Utils.getOTACExpiryTime());
                    dao.addOTAC(newOTAC);
                } catch (OneTimeAuthzCodeRestAuthServerException e) {
                    buildAuthServerFailureResponse(facade, e);
                }
                facade.setHeader(Constants.OTAC_HEADER_NAME, newOTAC.getSecret());
                facade.getResponse().getOutputStream().write(facade.getBody().toByteArray());
            } finally {
                unsetThreadLocalFacade();
            }
        } else {
            getNext().invoke(request, response);
        }

    }

    private void buildAuthenticationFailureResponse(ResponseFacade facade, OTACRestAuthenticationResult result) throws IOException {
        facade.setContentType("application/json");
        facade.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        facade.setCharacterEncoding("UTF-8");
        JsonObject errorBody = new JsonObject();
        errorBody.addProperty("code", result.getReason().getCode());
        errorBody.addProperty("message", result.getReason().getMessage());
        errorBody.addProperty("description", result.getReason().getDescription());
        errorBody.addProperty(Constants.CORRELATION_ID_MDC, getCorrelation());
        facade.getWriter().print(errorBody.toString());
    }

    private void buildAuthServerFailureResponse(ResponseFacade facade, OneTimeAuthzCodeRestAuthServerException e) throws IOException {
        facade.setContentType("application/json");
        facade.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        facade.setCharacterEncoding("UTF-8");
        JsonObject errprBody = new JsonObject();
        errprBody.addProperty("code", e.getErrorCode());
        errprBody.addProperty("message", e.getMessage());
        errprBody.addProperty(Constants.CORRELATION_ID_MDC, getCorrelation());
        facade.getWriter().print(errprBody.toString());
    }

    public static String getCorrelation() {
        String ref;
        if (isCorrelationIDPresent()) {
            ref = MDC.get(Constants.CORRELATION_ID_MDC).toString();
        } else {
            ref = UUID.randomUUID().toString();
        }

        return ref;
    }

    public static boolean isCorrelationIDPresent() {
        return MDC.get(Constants.CORRELATION_ID_MDC) != null;
    }

    private void setThreadLocalFacade(HttpServletResponse responseFacade) {

        IdentityUtil.threadLocalProperties.get().put(Constants.OTAC_RESPONSE_FACADE, responseFacade);
    }

    private void unsetThreadLocalFacade() {
        IdentityUtil.threadLocalProperties.get().remove(Constants.OTAC_RESPONSE_FACADE);
        IdentityUtil.threadLocalProperties.get().remove("isOTACAuthn");
    }
}
