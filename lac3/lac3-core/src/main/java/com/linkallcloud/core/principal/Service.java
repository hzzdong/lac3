package com.linkallcloud.core.principal;

/**
 * Marker interface for Services. Services are generally either remote applications utilizing SSO or applications that
 * principals wish to gain access to. In most cases this will be some form of web application.
 * 
 */
public interface Service {

    /**
     * Service resource url
     * 
     * @return url
     */
    String getUrl();

    /**
     * Service resource code;
     * 
     * @return code
     */
    String getCode();

}
