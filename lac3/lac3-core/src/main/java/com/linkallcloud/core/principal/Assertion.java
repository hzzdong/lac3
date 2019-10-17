package com.linkallcloud.core.principal;

import java.io.Serializable;
import java.util.Map;

/**
 * Interface to represent a successful response from the SSO Server.
 * 
 * 2011-6-14
 * 
 */
public interface Assertion extends Serializable {
    /**
     * Constant representing where we store the <code>Assertion</code> in the session.
     */
    public static final String ASSERTION_KEY = "_sso_assertion_";

    /**
     * Method to retrieve the principal.
     * 
     * @return the principal.
     */
    Principal getPrincipal();

    /**
     * Map of attributes returned by the SSO server. A client must know what attributes he is looking for as SSO makes
     * no claims about what attributes are returned.
     * 
     * @return the map of attributes.
     */
    Map<String, Object> getAttributes();

    /**
     * Retrieves a proxy ticket for the specific service.
     * 
     * @param service
     *            The service to proxy to.
     * @return the Proxy Ticket Id or null.
     */
    String getProxyTicketFor(Service service);
}
