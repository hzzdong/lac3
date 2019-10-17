package com.linkallcloud.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

public class TreeDto implements Serializable {
    private static final long serialVersionUID = -302328383975491077L;

    private static Log log = Logs.get();

    private String parentId;

    private String id;
    private String code;
    private String name;
    private Integer sortIndex;

    private List<TreeDto> children;

    public TreeDto() {
        super();
    }

    public TreeDto(Object entity) {
        try {
            BeanUtils.copyProperties(entity, this);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    public TreeDto(String id, String code, String name, Integer sortIndex) {
        super();
        this.id = id;
        this.code = code;
        this.name = name;
        this.sortIndex = sortIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
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

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public List<TreeDto> getChildren() {
        return children;
    }

    public void addChild(TreeDto child) {
        if (children == null) {
            children = new ArrayList<TreeDto>();
        }
        children.add(child);
    }

    public void setChildren(List<TreeDto> children) {
        this.children = children;
    }

}
