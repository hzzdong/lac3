package com.linkallcloud.core.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Query.GroupOperator;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;

public class WebQuery implements Serializable {
	private static final long serialVersionUID = -7056272723587483018L;

	private List<StringRuleDescriptor> cnds;

	private boolean distinct;
	private boolean mapUnderscoreToCamelCase;

	// 排序条件（非datatables）
	private Orderby orderby;

	public WebQuery() {
		super();
	}

	public static void copyWebQueryFields2Query(WebQuery src, Query dest) {
		if (src == null || dest == null) {
			return;
		}
		dest.setDistinct(src.isDistinct());
		dest.setMapUnderscoreToCamelCase(src.isMapUnderscoreToCamelCase());
		dest.setOrderby(src.getOrderby());
		dest.setGroupOp(GroupOperator.AND);
		dest.setGroups(null);
		if (src.getCnds() != null && !src.getCnds().isEmpty()) {
			for (StringRuleDescriptor rd : src.getCnds()) {
				dest.addRule(rd);
			}
		}
	}

	public Query toQuery() {
		Query result = new Query();
		if (this.cnds != null && !this.cnds.isEmpty()) {
			for (StringRuleDescriptor rd : cnds) {
				result.addRule(rd);
			}
		}
		return result;
	}

	public List<StringRuleDescriptor> getCnds() {
		return cnds;
	}

	public void setCnds(List<StringRuleDescriptor> cnds) {
		this.cnds = cnds;
	}

	public void addRule(StringRuleDescriptor rule) {
		if (rule != null) {
			if (this.cnds == null) {
				this.cnds = new ArrayList<StringRuleDescriptor>();
			}
			this.cnds.add(rule);
		}
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isMapUnderscoreToCamelCase() {
		return mapUnderscoreToCamelCase;
	}

	public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
		this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
	}

	public Orderby getOrderby() {
		return orderby;
	}

	public void setOrderby(Orderby orderby) {
		this.orderby = orderby;
	}

	@JSONField(serialize = false)
	public String[] getOrdersSqlFrags() {
		if (orderby != null && orderby.isOrderBySetted()) {
			return orderby.getOrderSqls(isMapUnderscoreToCamelCase());
		}
		return null;
	}

	/**
	 * order sql, 比如：id desc,name asc
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public String getOrdersSql() {
		String[] orderbys = getOrdersSqlFrags();
		if (orderbys != null && orderbys.length > 0) {
			return Strings.join2(",", orderbys);
		}
		return null;
	}

}
