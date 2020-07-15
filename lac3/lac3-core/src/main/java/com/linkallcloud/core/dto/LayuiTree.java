package com.linkallcloud.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LayuiTree implements Serializable {
    private static final long serialVersionUID = 1151209789460444836L;

    private Long id;
    private Long parentId;

    private String uuid;

    private String code;
    private String name;
    private boolean spread;// 是否展开状态（默认false）
    private String href;
    private List<LayuiTree> children;

    private int sort;// 排序
    private int status;// 状态
    private int level;// 层级

    private int type;
    private String ico;
    private String remark;

    private Long relId;// 关联对象ID
    private String relUuid;

    private Object[] fields;// 其它扩展属性

    public LayuiTree() {
        super();
    }

    public LayuiTree(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public LayuiTree(Long id, String uuid, String name) {
        super();
        this.id = id;
        this.uuid = uuid;
        this.name = name;
    }

    public LayuiTree copyNode() {
        LayuiTree nnd = new LayuiTree(this.getId(), this.getUuid(), this.getName());
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
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

    public List<LayuiTree> getChildren() {
        return children;
    }

    public void setChildren(List<LayuiTree> children) {
        this.children = children;
    }

    public void addChild(LayuiTree child) {
        if (this.children == null) {
            this.children = new ArrayList<LayuiTree>();
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

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
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
