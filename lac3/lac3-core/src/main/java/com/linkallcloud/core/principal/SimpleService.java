package com.linkallcloud.core.principal;

import org.springframework.util.Assert;

/**
 * The simplest implementation of a representation of a service. Provides no
 * additional attributes beyond those in the Service interface.
 * <p>
 */
public class SimpleService implements Service {

	private String url;
	private String code;
	private int clazz;//app clazz,0：运维，1：客户

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
	 * @param clazz
	 * @throws IllegalArgumentException if the url is null
	 */
	public SimpleService(final String url, final String code, final int clazz) {
		Assert.notNull(url, "id cannot be null");
		Assert.notNull(code, "code cannot be null");
		this.url = url;
		this.code = code;
		this.clazz = clazz;
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

	public int getClazz() {
		return clazz;
	}

	public void setClazz(int clazz) {
		this.clazz = clazz;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || !this.getClass().equals(o.getClass())) {
			return false;
		}

		final SimpleService p = (SimpleService) o;

		return this.url.equals(p.getUrl()) && this.code.equals(p.getCode()) && this.clazz == p.getClazz();
	}

}
