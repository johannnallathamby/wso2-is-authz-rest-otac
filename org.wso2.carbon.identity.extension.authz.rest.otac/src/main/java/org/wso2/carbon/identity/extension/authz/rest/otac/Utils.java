package org.wso2.carbon.identity.extension.authz.rest.otac;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.extension.authz.rest.otac.exception.OneTimeAuthzCodeRestAuthServerException;
import org.wso2.carbon.identity.extension.authz.rest.otac.internal.DataHolder;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.wso2.carbon.identity.extension.authz.rest.otac.Constants.OTAC_REGISTRY_RESOURCE;
import static org.wso2.carbon.identity.extension.authz.rest.otac.Constants.OTAC_VALIDITY_PROPERTY;

/**
 * Utils
 */
public class Utils {

    public static JdbcTemplate getNewIdentityTemplate() {
        return new JdbcTemplate(IdentityDatabaseUtil.getDataSource());
    }

    public static Timestamp getOTACExpiryTime() throws OneTimeAuthzCodeRestAuthServerException {
        int expiryTimeInMinutes = getOTACValidityInMinutes();
        return new java.sql.Timestamp(
                DateUtils.addMinutes(Calendar.getInstance().getTime(), expiryTimeInMinutes).getTime());
    }

    public static int getOTACValidityInMinutes() throws OneTimeAuthzCodeRestAuthServerException {

        int validDuration = 60;
        try {
            Registry registry = DataHolder.getInstance().getRegistryService().getConfigSystemRegistry(
                    MultitenantConstants.SUPER_TENANT_ID);
            if (registry.resourceExists(OTAC_REGISTRY_RESOURCE)) {
                Resource resource = registry.get(OTAC_REGISTRY_RESOURCE);
                String requestSecretValidDuration = resource.getProperty(OTAC_VALIDITY_PROPERTY);
                if (requestSecretValidDuration != null) {
                    validDuration = Integer.parseInt(requestSecretValidDuration);
                }
            }
        } catch (RegistryException e) {
            throw Utils.handleServerException(Constants.SERVER_ERROR.OTAC_VALIDITY_RETRIEVE_ERROR, null, e);
        }

        return validDuration;
    }

    public static OneTimeAuthzCodeRestAuthServerException handleServerException(
            Constants.SERVER_ERROR error, String data, Throwable e) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getMessage(), data);
        } else {
            message = error.getMessage();
        }
        return new OneTimeAuthzCodeRestAuthServerException(error.getCode(), message, e);
    }
}