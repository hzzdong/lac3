package com.linkallcloud.core.principal;

import org.springframework.util.Assert;

/**
 * The simplest implementation of a representation of a service. Provides no additional attributes beyond those in the
 * Service interface.
 * <p>
 */
public class SimpleService implements Service {

    private String url;
    private String code;

    /**
     *
     */
    public SimpleService() {
        super();
    }

    /**
     * Constructs a new SimpleService using the provided unique id for the service.
     *
     * @param url
     * @param code
     * @throws IllegalArgumentException if the url is null
     */
    public SimpleService(final String url, final String code) {
        Assert.notNull(url, "id cannot be null");
        Assert.notNull(code, "code cannot be null");
        this.url = url;
        this.code = code;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }

        final SimpleService p = (SimpleService) o;

        return this.url.equals(p.getUrl()) && this.code.equals(p.getCode());
    }

}
