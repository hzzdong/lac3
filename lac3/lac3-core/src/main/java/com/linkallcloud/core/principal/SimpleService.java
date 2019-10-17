package com.linkallcloud.core.principal;

import org.springframework.util.Assert;

/**
 * The simplest implementation of a representation of a service. Provides no additional attributes beyond those in the
 * Service interface.
 * 
 * 2011-6-14
 * 
 * @author <a href="mailto:hzzdong@gmail.com">ZhouDong</a>
 * 
 */
public class SimpleService implements Service {

    private String id;
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
     * @param id
     * @param code
     * @throws IllegalArgumentException
     *             if the ID is null
     */
    public SimpleService(final String id, final String code) {
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(code, "code cannot be null");
        this.id = id;
        this.code = code;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.zj.pubinfo.sso.principal.Service#getId()
     */
    public final String getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.zj.pubinfo.principal.Service#getCode()
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }

        final SimpleService p = (SimpleService) o;

        return this.id.equals(p.getId()) && this.code.equals(p.getCode());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.id + "|" + this.code;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode() ^ this.id.hashCode() ^ this.code.hashCode();
    }
}
