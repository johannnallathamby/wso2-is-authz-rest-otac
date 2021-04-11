package org.wso2.carbon.identity.extension.authz.rest.otac.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.wso2.carbon.identity.auth.service.handler.AuthenticationHandler;
import org.wso2.carbon.identity.authz.service.handler.AuthorizationHandler;
import org.wso2.carbon.identity.extension.authz.rest.otac.dao.OneTimeAuthzCodeDAOImpl;
import org.wso2.carbon.identity.extension.authz.rest.otac.handler.OTACRestAuthnHandler;
import org.wso2.carbon.identity.extension.authz.rest.otac.handler.OTACRestAuthzHandler;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * OSGi service component for One-Time-Authorization-Code Rest Authentication bundle
 */
@Component(name = "wso2is.authz.rest.otac.component",
        immediate = true
)
public class OTACServiceComponent {

    private static final Log log = LogFactory.getLog(OTACServiceComponent.class);

    @Activate
    protected void activate(ComponentContext componentContext) {
        try {
            DataHolder.getInstance().setOneTimeAuthzCodeDAO(OneTimeAuthzCodeDAOImpl.getInstance());
            AuthenticationHandler authnHandler = new OTACRestAuthnHandler();
            componentContext.getBundleContext().registerService(AuthenticationHandler.class, authnHandler, null);
            AuthorizationHandler authzHandler = new OTACRestAuthzHandler();
            componentContext.getBundleContext().registerService(AuthorizationHandler.class, authzHandler, null);
            if (log.isDebugEnabled())
                log.debug("One-Time-Authorization-Code Rest Auhtorization bundle activated successfully.");
            if (log.isDebugEnabled()) {
                log.debug("One-Time-Authorization-Code Rest Authorization bundle activated successfully");
            }
        } catch (Throwable e) {
            log.error("Error while activating One-Time-Authorization-Code Rest Authentication bundle", e);
        }
    }

    @Reference(name = "realm.service",
            service = org.wso2.carbon.user.core.service.RealmService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRealmService")
    protected void setRealmService(RealmService realmService) {

        if (log.isDebugEnabled()) {
            log.debug("Setting the Realm Service");
        }
        DataHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {

        if (log.isDebugEnabled()) {
            log.debug("Unset the Realm Service.");
        }
        DataHolder.getInstance().setRealmService(null);
    }

    @Reference(
            name = "registry.service",
            service = RegistryService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRegistryService"
    )
    protected void setRegistryService(RegistryService registryService) {
        DataHolder.getInstance().setRegistryService(registryService);
    }

    protected void unsetRegistryService(RegistryService registryService) {
        DataHolder.getInstance().setRegistryService(null);
    }

}
