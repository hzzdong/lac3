package com.linkallcloud.core.dto;

import com.linkallcloud.core.domain.TreeDomain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public class LayuiTrees {

    /**
     * 全部展开
     *
     * @param root
     */
    public static <PK extends Serializable> void expand(LayuiTree root) {
        if (root != null) {
            root.setSpread(true);
        }
        if (root.getChildren() != null && root.getChildren().size() > 0) {
            for (LayuiTree item : root.getChildren()) {
                expand(item);
            }
        }
    }

    /**
     * 全部收起
     *
     * @param root
     */
    public static <PK extends Serializable> void collapse(LayuiTree root) {
        if (root != null) {
            root.setSpread(false);
        }
        if (root.getChildren() != null && root.getChildren().size() > 0) {
            for (LayuiTree item : root.getChildren()) {
                expand(item);
            }
        }
    }

    /**
     * 把List组装成Tree
     *
     * @param list
     * @return
     */
    public static <T extends TreeDomain> LayuiTree assembleLayuiTree(List<T> list) {
        LayuiTree root = new LayuiTree();
        assembleLayuiTree(root, list, -1, null);
        return root;
    }

    /**
     * 把List组装成Tree
     *
     * @param list
     * @param extendsFields
     * @return
     */
    public static <T extends TreeDomain> LayuiTree assembleLayuiTree(List<T> list,
                                                                     String[] extendsFields) {
        LayuiTree root = new LayuiTree();
        assembleLayuiTree(root, list, -1, extendsFields);
        return root;
    }

    /**
     * 把List组装成Tree
     *
     * @param list
     * @param statusGe 忽略小于status的状态的节点。-1表示不过滤。
     * @return
     */
    public static <T extends TreeDomain> LayuiTree assembleLayuiTree(List<T> list,
                                                                     int statusGe) {
        LayuiTree root = new LayuiTree();
        assembleLayuiTree(root, list, statusGe, null);
        return root;
    }

    /**
     * 把List组装成Tree
     *
     * @param list
     * @param statusGe      忽略小于status的状态的节点。-1表示不过滤。
     * @param extendsFields 扩展属性
     * @return
     */
    public static <T extends TreeDomain> LayuiTree assembleLayuiTree(List<T> list,
                                                                     int statusGe, String[] extendsFields) {
        LayuiTree root = new LayuiTree();
        assembleLayuiTree(root, list, statusGe, extendsFields);
        return root;
    }

    /**
     * @param parent
     * @param nodeList
     * @param statusGe 忽略小于status的状态的节点。-1表示不过滤。
     */
    public static <T extends TreeDomain> void assembleLayuiTree(LayuiTree parent,
                                                                List<T> nodeList, int statusGe) {
        LayuiTrees.assembleLayuiTree(parent, nodeList, statusGe, null);
    }

    /**
     * @param parent
     * @param nodeList
     * @param statusGe      忽略小于status的状态的节点。-1表示不过滤。
     * @param extendsFields 扩展属性
     */
    public static <T extends TreeDomain> void assembleLayuiTree(LayuiTree parent,
                                                                List<T> nodeList, int statusGe, String[] extendsFields) {
        if (parent == null) {
            parent = new LayuiTree();
        }
        if (parent.getId() == null) {// 虚拟根节点
            for (TreeDomain node : nodeList) {
                if (node.isTopParent() && (statusGe == -1 || node.getStatus() >= statusGe)) {
                    LayuiTree item = createLayuiTreeNode(null, node, extendsFields);
                    parent.addChild(item);
                }
            }
        } else {
            for (TreeDomain node : nodeList) {
                if (node.getParentId().equals(parent.getId()) && (statusGe == -1 || node.getStatus() >= statusGe)) {
                    LayuiTree item = createLayuiTreeNode(parent, node, extendsFields);
                    parent.addChild(item);
                }
            }
        }

        if (parent.getChildren() != null && !parent.getChildren().isEmpty()) {
            for (LayuiTree subNode : parent.getChildren()) {
                assembleLayuiTree(subNode, nodeList, statusGe, extendsFields);
            }
        }
    }

    private static <PK extends Serializable> LayuiTree createLayuiTreeNode(LayuiTree parent,
                                                                           TreeDomain node, String[] extendsFields) {
        LayuiTree item = new LayuiTree(node.getId(), node.getUuid(), node.getName());
        item.setCode(node.getCode());
        item.setSort(node.getSort());
        item.setStatus(node.getStatus());
        item.setHref(node.getUrl());
        if (parent != null) {
            item.setLevel(parent.getLevel() + 1);
        } else {
            item.setLevel(0);
        }
        item.setType(node.getType());
        item.setIco(node.getIco());
        item.setRemark(node.getRemark());

        //item.setRelId(node.getRelId());
        //item.setRelUuid(node.getRelUuid());

        if (extendsFields != null && extendsFields.length > 0) {
            Object[] efvs = new Object[extendsFields.length];
            for (int i = 0; i < extendsFields.length; i++) {
                Object fv = LayuiTrees.getFieldValueByFieldName(extendsFields[i], node);
                efvs[i] = fv;
            }
            item.setFields(efvs);
        }
        return item;
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    private static Object getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = LayuiTrees.getFieldByClasss(fieldName, object);
            if (field != null) {
                // 设置对象的访问权限，保证对private的属性的访问
                field.setAccessible(true);
                return field.get(object);
            }

        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 根据属性名获取属性元素，包括各种安全范围和所有父类
     *
     * @param fieldName
     * @param object
     * @return
     */
    private static Field getFieldByClasss(String fieldName, Object object) {
        Field field = null;
        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                if (field != null) {
                    break;
                }
            } catch (Exception e) {
                // 这里甚么都不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会进入
            }
        }
        return field;

    }

}
