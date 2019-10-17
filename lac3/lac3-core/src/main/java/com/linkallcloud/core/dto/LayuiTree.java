package com.linkallcloud.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LayuiTree<T> implements Serializable {
	private static final long serialVersionUID = 1151209789460444836L;

	private T id;
	private T parentId;

	private String uuid;

	private String code;
	private String name;
	private boolean spread;// 是否展开状态（默认false）
	private String href;
	private List<LayuiTree<T>> children;

	private int sort;// 排序
	private int status;// 状态
	private int level;// 层级

	private int type;
	private String ico;
	private String remark;

	private T relId;// 关联对象ID
	private String relUuid;

	private Object[] fields;// 其它扩展属性

	public LayuiTree() {
		super();
	}

	public LayuiTree(T id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public LayuiTree(T id, String uuid, String name) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.name = name;
	}

	public LayuiTree<T> copyNode() {
		LayuiTree<T> nnd = new LayuiTree<T>(this.getId(), this.getUuid(), this.getName());
		nnd.setCode(this.getCode());
		nnd.setParentId(this.getParentId());
		nnd.setSpread(this.isSpread());
		nnd.setHref(this.getHref());
		nnd.setSort(this.getSort());
		nnd.setStatus(this.getStatus());
		nnd.setLevel(this.getLevel());
		nnd.setType(this.getType());
		nnd.setIco(this.getIco());
		nnd.setRemark(this.getRemark());
		nnd.setRelId(this.getRelId());
		nnd.setRelUuid(this.getRelUuid());
		if (this.fields != null && this.fields.length > 0) {
			Object[] fs = new Object[this.fields.length];
			for (int i = 0; i < this.fields.length; i++) {
				fs[i] = this.fields[i];
			}
			nnd.setFields(fs);
		}
		return nnd;
	}

	public Object[] getFields() {
		return fields;
	}

	public void setFields(Object[] fields) {
		this.fields = fields;
	}

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public T getParentId() {
		return parentId;
	}

	public void setParentId(T parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSpread() {
		return spread;
	}

	public void setSpread(boolean spread) {
		this.spread = spread;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public List<LayuiTree<T>> getChildren() {
		return children;
	}

	public void setChildren(List<LayuiTree<T>> children) {
		this.children = children;
	}

	public void addChild(LayuiTree<T> child) {
		if (this.children == null) {
			this.children = new ArrayList<LayuiTree<T>>();
		}
		if (child != null) {
			this.children.add(child);
			child.setParentId(this.getId());
		}
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public T getRelId() {
		return relId;
	}

	public void setRelId(T relId) {
		this.relId = relId;
	}

	public String getRelUuid() {
		return relUuid;
	}

	public void setRelUuid(String relUuid) {
		this.relUuid = relUuid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
