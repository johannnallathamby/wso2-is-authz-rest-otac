package org.wso2.carbon.identity.extension.authz.rest.otac.dao;

import org.wso2.carbon.identity.extension.authz.rest.otac.exception.OneTimeAuthzCodeRestAuthServerException;
import org.wso2.carbon.identity.extension.authz.rest.otac.model.OneTimeAuthzCode;

/**
 * Interface to perform CRUD on one-time-authz-code
 */
public interface OneTimeAuthzCodeDAO {

    void addOTAC(OneTimeAuthzCode oneTimeAuthzCode) throws OneTimeAuthzCodeRestAuthServerException;

    OneTimeAuthzCode getOTAC(String oneTimeAuthzCode) throws OneTimeAuthzCodeRestAuthServerException;

}
