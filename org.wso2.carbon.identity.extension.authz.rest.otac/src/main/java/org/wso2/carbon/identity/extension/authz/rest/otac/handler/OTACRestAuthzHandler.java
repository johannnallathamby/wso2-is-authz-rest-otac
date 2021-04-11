/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.extension.authz.rest.otac.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.authz.service.AuthorizationContext;
import org.wso2.carbon.identity.authz.service.AuthorizationResult;
import org.wso2.carbon.identity.authz.service.AuthorizationStatus;
import org.wso2.carbon.identity.authz.service.exception.AuthzServiceServerException;
import org.wso2.carbon.identity.authz.service.handler.AuthorizationHandler;
import org.wso2.carbon.identity.core.util.IdentityUtil;

/**
 * One-time-authorization-code authorization handler
 */
public class OTACRestAuthzHandler extends AuthorizationHandler {

    private static final Log log = LogFactory.getLog(OTACRestAuthzHandler.class);

    @Override
    public AuthorizationResult handleAuthorization(AuthorizationContext authorizationContext)
            throws AuthzServiceServerException {

        AuthorizationResult result = super.handleAuthorization(authorizationContext);
        if (AuthorizationStatus.DENY.equals(result.getAuthorizationStatus())) {
            boolean isOTACAuthn = (Boolean)IdentityUtil.threadLocalProperties.get().get("isOTACAuthn");
            if (isOTACAuthn){
                result = new AuthorizationResult(AuthorizationStatus.GRANT);
            }
        }
        return result;
    }

    @Override
    public String getName() {

        return "OTACRestAuthzHandler";
    }

    @Override
    public int getPriority() {

        return 80;
    }
}
