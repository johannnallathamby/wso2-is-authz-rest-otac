package org.wso2.carbon.identity.extension.authz.rest.otac.internal;

import org.wso2.carbon.identity.extension.authz.rest.otac.dao.OneTimeAuthzCodeDAO;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Data holder for One-Time-Authorization-Code Rest Authentication bundle
 */
public class DataHolder {

    private static final DataHolder dataHolder = new DataHolder();
    private OneTimeAuthzCodeDAO oneTimeAuthzCodeDAO;
    private RealmService realmService;
    private RegistryService registryService;

    public static DataHolder getInstance() {
        return dataHolder;
    }

    public OneTimeAuthzCodeDAO getDAO() {
        return oneTimeAuthzCodeDAO;
    }

    public void setOneTimeAuthzCodeDAO (OneTimeAuthzCodeDAO oneTimeAuthzCodeDAO) {
        this.oneTimeAuthzCodeDAO = oneTimeAuthzCodeDAO;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    public RealmService getRealmService() {
        return realmService;
    }

    public void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }

}
