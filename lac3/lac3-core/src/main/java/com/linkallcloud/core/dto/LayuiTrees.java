package com.linkallcloud.core.dto;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import com.linkallcloud.core.domain.TreeDomain;

public class LayuiTrees {

	/**
	 * 全部展开
	 * 
	 * @param root
	 */
	public static <PK extends Serializable> void expand(LayuiTree<PK> root) {
		if (root != null) {
			root.setSpread(true);
		}
		if (root.getChildren() != null && root.getChildren().size() > 0) {
			for (LayuiTree<PK> item : root.getChildren()) {
				expand(item);
			}
		}
	}

	/**
	 * 全部收起
	 * 
	 * @param root
	 */
	public static <PK extends Serializable> void collapse(LayuiTree<PK> root) {
		if (root != null) {
			root.setSpread(false);
		}
		if (root.getChildren() != null && root.getChildren().size() > 0) {
			for (LayuiTree<PK> item : root.getChildren()) {
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
	public static <PK extends Serializable, T extends TreeDomain<PK>> LayuiTree<PK> assembleLayuiTree(List<T> list) {
		LayuiTree<PK> root = new LayuiTree<PK>();
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
	public static <PK extends Serializable, T extends TreeDomain<PK>> LayuiTree<PK> assembleLayuiTree(List<T> list,
			String[] extendsFields) {
		LayuiTree<PK> root = new LayuiTree<PK>();
		assembleLayuiTree(root, list, -1, extendsFields);
		return root;
	}

	/**
	 * 把List组装成Tree
	 * 
	 * @param list
	 * @param statusGe
	 *            忽略小于status的状态的节点。-1表示不过滤。
	 * @return
	 */
	public static <PK extends Serializable, T extends TreeDomain<PK>> LayuiTree<PK> assembleLayuiTree(List<T> list,
			int statusGe) {
		LayuiTree<PK> root = new LayuiTree<PK>();
		assembleLayuiTree(root, list, statusGe, null);
		return root;
	}

	/**
	 * 把List组装成Tree
	 * 
	 * @param list
	 * @param statusGe
	 *            忽略小于status的状态的节点。-1表示不过滤。
	 * @param extendsFields
	 *            扩展属性
	 * @return
	 */
	public static <PK extends Serializable, T extends TreeDomain<PK>> LayuiTree<PK> assembleLayuiTree(List<T> list,
			int statusGe, String[] extendsFields) {
		LayuiTree<PK> root = new LayuiTree<PK>();
		assembleLayuiTree(root, list, statusGe, extendsFields);
		return root;
	}

	/**
	 * 
	 * @param parent
	 * @param nodeList
	 * @param statusGe
	 *            忽略小于status的状态的节点。-1表示不过滤。
	 */
	public static <PK extends Serializable, T extends TreeDomain<PK>> void assembleLayuiTree(LayuiTree<PK> parent,
			List<T> nodeList, int statusGe) {
		LayuiTrees.assembleLayuiTree(parent, nodeList, statusGe, null);
	}

	/**
	 * 
	 * @param parent
	 * @param nodeList
	 * @param statusGe
	 *            忽略小于status的状态的节点。-1表示不过滤。
	 * @param extendsFields
	 *            扩展属性
	 */
	public static <PK extends Serializable, T extends TreeDomain<PK>> void assembleLayuiTree(LayuiTree<PK> parent,
			List<T> nodeList, int statusGe, String[] extendsFields) {
		if (parent == null) {
			parent = new LayuiTree<PK>();
		}
		if (parent.getId() == null) {// 虚拟根节点
			for (TreeDomain<PK> node : nodeList) {
				if (node.isTopParent() && (statusGe == -1 || node.getStatus() >= statusGe)) {
					LayuiTree<PK> item = createLayuiTreeNode(null, node, extendsFields);
					parent.addChild(item);
				}
			}
		} else {
			for (TreeDomain<PK> node : nodeList) {
				if (node.getParentId().equals(parent.getId()) && (statusGe == -1 || node.getStatus() >= statusGe)) {
					LayuiTree<PK> item = createLayuiTreeNode(parent, node, extendsFields);
					parent.addChild(item);
				}
			}
		}

		if (parent.getChildren() != null && !parent.getChildren().isEmpty()) {
			for (LayuiTree<PK> subNode : parent.getChildren()) {
				assembleLayuiTree(subNode, nodeList, statusGe, extendsFields);
			}
		}
	}

	private static <PK extends Serializable> LayuiTree<PK> createLayuiTreeNode(LayuiTree<PK> parent,
			TreeDomain<PK> node, String[] extendsFields) {
		LayuiTree<PK> item = new LayuiTree<PK>(node.getId(), node.getUuid(), node.getName());
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
