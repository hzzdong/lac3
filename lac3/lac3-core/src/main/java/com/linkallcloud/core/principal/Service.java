package com.linkallcloud.core.principal;

/**
 * Marker interface for Services. Services are generally either remote applications utilizing SSO or applications that
 * principals wish to gain access to. In most cases this will be some form of web application.
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public interface Service {

    /**
     * Service resource id(url)
     * 
     * @return id
     */
    String getId();

    /**
     * Service resource code;
     * 
     * @return code
     */
    String getCode();

}
