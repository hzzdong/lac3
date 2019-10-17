package com.linkallcloud.core.principal;

import java.io.Serializable;

/**
 * Generic concept of an authenticated thing. Examples include a person or a service.
 * <p>
 * The implementation SimplePrincipal just contains the Id property. More complex Principal objects may contain
 * additional information that are meaningful to the View layer but are generally transparent to the rest of SSO.
 * </p>
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public interface Principal extends Serializable {

    /**
     * Constant representing where we flag a principal.
     */
    public static final String PRINCIPAL_KEY = "_sso_principal_";

    public static final int NEW_ACCOUNT = 1;// SSO和Site采用统一账号
    public static final int MAPING_ACCOUNT = 2;// SSO和Site采用映射账号
    public static final int MAPING_NEW_ACCOUNT = 21;// SSO和Site采用映射账号：以SSO账号新开户
    public static final int MAPING_EXIST_ACCOUNT = 22;// SSO和Site采用映射账号：老账号映射

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
