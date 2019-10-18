package com.linkallcloud.core.aop;

import com.linkallcloud.core.domain.IDomain;
import com.linkallcloud.core.lang.Strings;

public class DomainDescription {

	private Class<? extends IDomain> domainClass;

	private String showName;

	private String[] logFields;

	public DomainDescription() {
		super();
	}

	public DomainDescription(Class<? extends IDomain> domainClass, String showName, String[] logFields) {
		super();
		this.domainClass = domainClass;
		this.showName = showName;
		this.logFields = logFields;
	}

	public DomainDescription(Class<? extends IDomain> domainClass, String showName, String logFieldsStr) {
		super();
		this.domainClass = domainClass;
		this.showName = showName;
		this.logFields = Strings.isBlank(logFieldsStr) ? new String[] { "id", "name" }
				: Strings.trimAll(logFieldsStr).split(",");
	}

	public Class<? extends IDomain> getDomainClass() {
		return domainClass;
	}

	public void setDomainClass(Class<? extends IDomain> domainClass) {
		this.domainClass = domainClass;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String[] getLogFields() {
		return logFields;
	}

	public void setLogFields(String[] logFields) {
		this.logFields = logFields;
	}

	public void setLogFields(String logFieldsStr) {
		this.logFields = Strings.isBlank(logFieldsStr) ? new String[] { "id", "name" }
				: Strings.trimAll(logFieldsStr).split(",");
	}

}
