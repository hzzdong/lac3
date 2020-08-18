package com.linkallcloud.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * TreeVO
 */
public class Tree implements Serializable {
	private static final long serialVersionUID = 7776064286477356232L;

	private String id;
	private String pId;
	private String name;
	private String fullName;
	@JSONField(name = "isParent")
	private Boolean parent;

	private Boolean open;// open,closed
	private Boolean checked;
	private Boolean nocheck;
	private Boolean chkDisabled;

	private Map<String, Object> attributes;

	private String icon;
	private String iconOpen;
	private String iconClose;
	private String iconSkin;

	private List<Tree> children;

	private String uuid;
	private String type;
	private int status;
	private String govCode;
	private int level;
	private int sort;

	private String remark;

	private String value;
	private String title;

	public Tree() {
		super();
	}

	public Tree(String id, String pId, String name) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public Tree(String id, String pId, String name, String govCode) {
		this(id, pId, name);
		this.govCode = govCode;
	}

	public Tree(String id, String pId, String name, String govCode, String url) {
		this(id, pId, name, govCode);
		this.addAttribute("url", url);
	}

	public Tree(String id, String pId, String name, String govCode, boolean open) {
		this(id, pId, name, govCode);
		this.open = open;
	}

	public Tree(String id, String uuid, String pId, String name, String govCode, String type, int status) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.uuid = uuid;
		this.type = type;
		this.status = status;
		this.govCode = govCode;
	}

	public static void sort(List<Tree> items) {
		if (items != null && items.size() > 0) {
			Collections.sort(items, new Comparator<Tree>() {
				@Override
				public int compare(Tree u1, Tree u2) {
					int diff = u1.getSort() - u2.getSort();
					if (diff > 0) {
						return 1;
					} else if (diff < 0) {
						return -1;
					}
					return 0;
				}
			});
		}
	}

	public void sort() {
		if (this.children != null && this.children.size() > 0) {
			Collections.sort(this.children, new Comparator<Tree>() {
				@Override
				public int compare(Tree u1, Tree u2) {
					int diff = u1.getSort() - u2.getSort();
					if (diff > 0) {
						return 1;
					} else if (diff < 0) {
						return -1;
					}
					return 0;
				}
			});

			for (Tree child : this.children) {
				child.sort();
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Boolean getParent() {
		return parent;
	}

	public void setParent(Boolean parent) {
		this.parent = parent;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(String name, Object value) {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, Object>();
		}
		this.attributes.put(name, value);
	}

	public Object getAttribute(String name) {
		if (this.attributes == null) {
			return null;
		}
		return this.attributes.get(name);
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconOpen() {
		return iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	public String getIconClose() {
		return iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public List<Tree> getChildren() {
		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	public void addChild(Tree child) {
		if (children == null) {
			children = new ArrayList<Tree>();
		}
		children.add(child);
		child.setpId(this.id);
	}

	public Boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}

	public Boolean getChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(Boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
