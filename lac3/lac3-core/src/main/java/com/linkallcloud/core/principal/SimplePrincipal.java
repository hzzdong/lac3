package com.linkallcloud.core.principal;

import org.springframework.util.Assert;

/**
 * Simplest implementation of a Principal. Provides no additional attributes
 * beyond those in the Principal interface. This is the closest representation
 * of a principal in the SSO world.
 * 
 */
public class SimplePrincipal implements Principal {
	private static final long serialVersionUID = 3248752885262404636L;

	/** The unique identifier for the principal. */
	private final String id;

	/** The unique identifier for the site account loginname. */
	private final String siteId;

	/** The site app clazz. 0：运维，1：客户 */
	private final int siteClazz;

	/** The account mapping type. */
	private final int mappingType;

	/**
	 * Constructs the SimplePrincipal using the provided unique id.
	 * 
	 * @param id          the identifier for the Principal
	 * @param siteId      The unique identifier for the site account loginname
	 * @param siteClazz   The site app clazz. 0：运维，1：客户
	 * @param mappingType The account mapping type.
	 */
	public SimplePrincipal(final String id, final String siteId, final int siteClazz, final int mappingType) {
		Assert.notNull(id, "id cannot be null");
		this.id = id;
		this.siteId = siteId;
		this.siteClazz = siteClazz;
		this.mappingType = mappingType;
	}

	public final String getId() {
		return this.id;
	}

	public String getSiteId() {
		return siteId;
	}

	public int getSiteClazz() {
		return siteClazz;
	}

	public int getMappingType() {
		return mappingType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(final Object o) {
		if (o == null || !this.getClass().equals(o.getClass())) {
			return false;
		}

		final SimplePrincipal p = (SimplePrincipal) o;

		return this.id.equals(p.getId()) && this.siteId.equals(p.getSiteId()) && this.mappingType == p.getMappingType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.id + "," + this.siteId + "," + this.mappingType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode() ^ this.id.hashCode();
	}
}
