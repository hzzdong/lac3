package com.linkallcloud.core.domain;

import com.linkallcloud.core.dto.Tree;

public abstract class TreeDomain extends Domain  {
	private static final long serialVersionUID = -938988291042654935L;

	private String code;// 系统编码

	private Long parentId;
	private String parentClass;

	private String govCode;
	//@NotBlank(message = "名称不能为空")
	private String name;
	private int level;// 层级

	private int sort;
	private int type;
	private String ico;

	private String url;
	private String remark;

	// private PK relId;// 关联对象ID
	// private String relUuid;

	/* 以下为查询字段 */
	private String parentName;

	public TreeDomain() {
		super();
	}

	public TreeDomain(Long parentId, String name, String govCode, int sort) {
		super();
		this.parentId = parentId;
		this.name = name;
		this.govCode = govCode;
		this.sort = sort;
	}

	public TreeDomain(Long id, String uuid) {
		super(id, uuid);
	}

	public TreeDomain(Long id) {
		super(id);
	}

	public String codeTag() {
		return "-";
	}

	public void setParent(TreeDomain parent) {
		if (parent != null) {
			this.setParentId(parent.getId());
			this.setParentClass(parent.getClass().getSimpleName());
		}
	}

	public boolean isTopParent() {
		if (this.parentId == null) {
			return true;
		} else {
			if (this.parentId instanceof Long) {
				Long pid = (Long) this.parentId;
				if (pid.longValue() <= 0) {
					return true;
				}
			}
		}
		return false;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentClass() {
		return parentClass;
	}

	public void setParentClass(String parentClass) {
		this.parentClass = parentClass;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
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

	public String getGovCode() {
		return govCode;
	}

	public void setGovCode(String govCode) {
		this.govCode = govCode;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Tree toTreeNode() {
		Tree node = new Tree(this.getId() == null ? null : this.getId().toString(), this.getUuid(),
				this.getParentId() == null ? null : this.getParentId().toString(), this.getName(), this.getGovCode(),
				String.valueOf(this.getType()), this.getStatus());
		node.setSort(this.getSort());
		node.setRemark(this.getRemark());
		node.addAttribute("url", this.getUrl());
		node.addAttribute("alias", getAlias());
		node.addAttribute("level", getLevel());
		return node;
	}

	protected String getAlias() {
		return this.getClass().getSimpleName();
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}
