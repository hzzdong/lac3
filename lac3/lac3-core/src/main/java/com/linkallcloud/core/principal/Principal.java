package com.linkallcloud.core.principal;

import java.io.Serializable;

/**
 * Generic concept of an authenticated thing. Examples include a person or a service.
 * <p>
 * The implementation SimplePrincipal just contains the Id property. More complex Principal objects may contain
 * additional information that are meaningful to the View layer but are generally transparent to the rest of SSO.
 * </p>
 */
public interface Principal extends Serializable {

    /**
     * Constant representing where we flag a principal.
     */
    public static final String PRINCIPAL_KEY = "_sso_principal_";

    /*
     * SSO account loginname
     */
    String getId();

    /*
     * Site account loginname
     */
    String getSiteId();

    /*
     * Account mapping type
     */
    int getMappingType();
}
