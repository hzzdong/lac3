package com.linkallcloud.core.dto;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.lang.Strings;

public class Trees {

	public static boolean isVirtual(Tree node) {
		if (node != null
				&& ("v-root".equals(node.getId()) || "0".equals(node.getId()) || "v-root".equals(node.getType()))) {
			return true;
		}
		return false;
	}

	/**
	 * 创建一个虚拟根节点
	 *
	 * @param name
	 * @return
	 */
	public static Tree vroot(String name) {
		return new Tree("v-root", null, name);
	}

	/**
	 * 创建一个虚拟根节点
	 *
	 * @param name
	 * @return
	 */
	public static Tree root0(String name) {
		return new Tree("0", null, name);
	}

	public static List<Tree> tree2List(Tree tree) {
		List<Tree> result = new ArrayList<Tree>();
		if (tree != null) {
			tree2List(tree, result);
		}
		if (result.size() > 0) {
			for (Tree item : result) {
				item.setChildren(null);
			}
		}
		return result;
	}

	private static void tree2List(Tree tree, List<Tree> result) {
		result.add(tree);
		if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
			for (Tree item : tree.getChildren()) {
				tree2List(item, result);
			}
		}

	}

	public static Map<String, Tree> tree2Map(Tree tree) {
		Map<String, Tree> result = new HashMap<>();
		if (tree != null) {
			assembleTree2Map(tree, result);
		}
		return result;
	}

	private static void assembleTree2Map(Tree tree, Map<String, Tree> result) {
		if (tree == null) {
			return;
		}

		if (tree.getId() != null) {
			result.put(tree.getId(), tree);
		}

		if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
			for (Tree item : tree.getChildren()) {
				assembleTree2Map(item, result);
			}
		}

	}

	/**
	 * 把children挂到parent下，并把parent加入children中。
	 *
	 * @param children
	 * @param parent
	 */
	public static List<Tree> assembleChildren2Parent(List<Tree> children, Tree parent) {
		if (children == null) {
			children = new ArrayList<Tree>();
		} else {
			Iterator<Tree> itr = children.iterator();
			while (itr.hasNext()) {
				Tree node = itr.next();
				if (Strings.isBlank(node.getpId()) || node.getpId().equals("0")) {
					node.setpId(parent.getId());
				}
			}
		}
		children.add(parent);
		return children;
	}

	/**
	 * 把nodeList中的根节点作为直接子节点挂到parent上
	 *
	 * @param parent
	 * @param nodeList
	 */
	public static <T extends TreeDomain> void assembleDirectDomain(Tree parent, List<T> children, String idPreFix) {
		if (parent == null) {
			parent = Trees.root0("虚拟根节点");// new Tree("0", null, "虚拟根节点");
		}

		if (children == null || children.isEmpty()) {
			return;
		}

		for (TreeDomain node : children) {
			Tree item = node.toTreeNode();
			item.setpId(parent.getId());
			if (!Strings.isBlank(idPreFix)) {
				item.setId(idPreFix + item.getId());
			}
			parent.addChild(item);
		}
	}

	public static <T extends TreeDomain> List<Tree> assembleDirectDomain(String rootId, List<T> domains,
			String idPreFix) {
		List<Tree> result = new ArrayList<Tree>();
		if (domains != null && !domains.isEmpty()) {
			for (T domain : domains) {
				Tree item = domain.toTreeNode();
				item.setpId(rootId);
				if (!Strings.isBlank(idPreFix)) {
					item.setId(idPreFix + item.getId());
				}
				result.add(item);
			}
		}
		return result;
	}

	/**
	 * 把nodeList中节点挂成父子关系
	 *
	 * @param nodeList
	 * @return
	 */
	public static <T extends TreeDomain> List<Tree> assembleTreeList(List<T> nodeList) {
		return Trees.assembleTreeList(null, nodeList, -1);
	}

	/**
	 * 把nodeList中的根节点挂到rootId上，其它正常挂成父子关系
	 *
	 * @param rootId
	 * @param nodeList
	 * @return
	 */
	public static <T extends TreeDomain> List<Tree> assembleTreeList(String rootId, List<T> nodeList) {
		return Trees.assembleTreeList(rootId, nodeList, -1);
	}

	/**
	 * 把nodeList中的根节点挂到rootId上，其它正常挂成父子关系，支持自定义扩展字段。
	 *
	 * @param rootId
	 * @param nodeList
	 * @param statusGe      忽略小于status的状态的节点。-1表示不过滤。
	 * @param extendsFields 扩展属性
	 * @return
	 */
	public static <T extends TreeDomain> List<Tree> assembleTreeList(String rootId, List<T> nodeList, int statusGe,
			String[] extendsFields) {
		if (nodeList == null || nodeList.isEmpty()) {
			return null;
		}

		List<Tree> result = new ArrayList<Tree>();
		for (TreeDomain node : nodeList) {
			if (statusGe == -1 || node.getStatus() >= statusGe) {
				if (node.isTopParent()) {
					Tree item = createTreeNode(rootId, node, extendsFields);
					result.add(item);
				} else {
					Tree item = createTreeNode(node.getParentId().toString(), node, extendsFields);
					result.add(item);
				}
			}
		}

		return result;
	}

	/**
	 * 把nodeList中的根节点挂到rootId上，其它正常挂成父子关系
	 *
	 * @param rootId
	 * @param nodeList
	 * @param statusGe 忽略小于status的状态的节点。-1表示不过滤。
	 * @return
	 */
	public static <T extends TreeDomain> List<Tree> assembleTreeList(String rootId, List<T> nodeList, int statusGe) {
		List<Tree> result = new ArrayList<Tree>();

		if (nodeList == null || nodeList.isEmpty()) {
			return result;
		}

		for (TreeDomain node : nodeList) {
			if (statusGe == -1 || node.getStatus() >= statusGe) {
				if (node.isTopParent()) {
					Tree item = node.toTreeNode();
					item.setpId(rootId);
					result.add(item);
				} else {
					Tree item = node.toTreeNode();
					result.add(item);
				}
			}
		}

		return result;
	}

	/**
	 * 从nodes中找出根节点
	 *
	 * @param nodes
	 * @return
	 */
	public static List<Tree> findRootNodes(List<Tree> nodes) {
		if (nodes == null || nodes.isEmpty()) {
			return null;
		}

		List<Tree> result = new ArrayList<Tree>();
		for (Tree node : nodes) {
			if (Strings.isBlank(node.getpId())) {// root
				result.add(node);
			}
		}

		if (result.size() > 0) {
			return result;
		}

		for (Tree node : nodes) {
			boolean find = false;
			for (Tree node2 : nodes) {
				if (node.getpId().equals(node2.getId())) {
					find = true;
					break;
				}
			}
			if (!find) {
				result.add(node);
			}
		}

		return result;
	}

	/**
	 * 过滤无效的树节点，目前主要功能是：过滤非根节点，但是父节点被禁用删除之类的节点
	 *
	 * @param nodes
	 * @return
	 */
	public static List<Tree> filterTreeNode(List<Tree> nodes) {
		if (nodes == null || nodes.isEmpty()) {
			return null;
		}

		List<Tree> result = new ArrayList<Tree>();
		for (Tree node : nodes) {
			if (Strings.isBlank(node.getpId())) {// root
				result.add(node);
			} else {
				for (Tree parent : nodes) {
					if (node.getpId().equals(parent.getId())) {
						result.add(node);
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 把nodes检查父子关系，计算后与root挂起来，并把有效的树节点返回
	 *
	 * @param root     若root不为null，这把nodes中的第一层节点挂到root下，并和root一起返回
	 * @param nodeList
	 * @return
	 */
	public static <T extends TreeDomain> List<Tree> assembleDomain2List(Tree root, List<T> nodeList) {
		CopyOnWriteArrayList<Tree> nodes = new CopyOnWriteArrayList<>();
		if (nodeList != null && !nodeList.isEmpty()) {
			for (TreeDomain enode : nodeList) {
				Tree tnode = enode.toTreeNode();
				nodes.add(tnode);
			}
		}
		return Trees.assembleTrees2List(root, nodes);
	}

	/**
	 * 把nodes检查父子关系，计算后与root挂起来，并把有效的树节点返回
	 *
	 * @param root  若root不为null，这把nodes中的第一层节点挂到root下，并和root一起返回
	 * @param nodes
	 * @return
	 */
	public static List<Tree> assembleTrees2List(Tree root, CopyOnWriteArrayList<Tree> nodes) {
		Trees.assembleTree(root, nodes, -1);
		return Trees.tree2List(root);
	}

	/**
	 * 从nodeList中找出唯一根节点root，若不唯一则创建一个虚拟根节点root，然后把其它节点按树形挂载到root下，返回root
	 *
	 * @param nodes
	 */
	public static <T extends TreeDomain> Tree assembleTree4Domains(List<T> domains) {
		List<Tree> nodes = new ArrayList<Tree>();
		if (domains != null && !domains.isEmpty()) {
			for (T domain : domains) {
				Tree item = domain.toTreeNode();
				nodes.add(item);
			}
		}
		return Trees.assembleTree(nodes);
	}

	/**
	 * 从nodeList中找出唯一根节点root，若不唯一则创建一个虚拟根节点root，然后把其它节点按树形挂载到root下，返回root
	 *
	 * @param nodes
	 */
	public static Tree assembleTree(List<Tree> nodes) {
		if (nodes == null) {
			throw new BizException("nodes不能为空");
		}
		Tree root = Trees.vroot("虚拟根节点");
		List<Tree> children = null;
		List<Tree> roots = Trees.findRootNodes(nodes);
		if (roots != null && !roots.isEmpty() && roots.size() == 1) {
			root = roots.get(0);
			final String rootId = root.getId();
			children = nodes.stream().filter(s -> !s.getId().toString().equals(rootId)).collect(Collectors.toList());
		}
		Trees.assembleTree(root, children == null ? nodes : children);
		return root;
	}

	/**
	 * 把nodeList中的根节点挂到parent上，返回一颗树parent
	 *
	 * @param parent
	 * @param nodes
	 */
	public static void assembleTree(Tree parent, List<Tree> nodes) {
		if (parent == null) {
			throw new BizException("parent不能为空");
		}
		if (nodes != null && !nodes.isEmpty()) {
			CopyOnWriteArrayList<Tree> nodes2 = new CopyOnWriteArrayList<Tree>(nodes);
			Trees.assembleTree(parent, nodes2, -1);
		}
		return;
	}

	/**
	 * 把nodeList中的根节点挂到parent上，返回一颗树parent
	 *
	 * @param parent
	 * @param nodes
	 * @param statusGe 忽略小于status的状态的节点。-1表示不过滤。
	 */
	public static void assembleTree(Tree parent, CopyOnWriteArrayList<Tree> nodes, int statusGe) {
		if (parent == null) {
			throw new BizException("parent不能为空");
		}

		if (nodes == null || nodes.isEmpty()) {
			return;
		}

		if (Trees.isVirtual(parent)) {// 虚拟根节点
			List<Tree> roots = Trees.findRootNodes(nodes);
			if (roots != null && !roots.isEmpty()) {
				for (Tree pnode : roots) {
					if (statusGe == -1 || pnode.getStatus() >= statusGe) {
						pnode.setpId(parent.getId());
						parent.addChild(pnode);
						nodes.remove(pnode);
					}
				}
			}
		} else {
			for (Tree node : nodes) {
				if (node.getpId().equals(parent.getId()) && (statusGe == -1 || node.getStatus() >= statusGe)) {
					parent.addChild(node);
					nodes.remove(node);
				}
			}
		}

		if (parent.getChildren() != null && !parent.getChildren().isEmpty() && !nodes.isEmpty()) {
			for (Tree subNode : parent.getChildren()) {
				Trees.assembleTree(subNode, nodes, statusGe);
			}
		}

	}

	/**
	 * 把nodeList中的根节点挂到parent上，返回一颗树
	 *
	 * @param parent
	 * @param nodeList
	 */
	public static <T extends TreeDomain> void assembleDomain2Tree(Tree parent, Collection<T> nodeList) {
		if (parent == null) {
			throw new BizException("parent参数不能为空");
		}
		if (nodeList != null && !nodeList.isEmpty()) {
			Trees.assembleDomain2Tree(parent, nodeList, -1);
		}
	}

	/**
	 * 把nodeList中的根节点挂到parent上，返回一颗树
	 *
	 * @param parent
	 * @param nodeList
	 * @param statusGe 忽略小于status的状态的节点。-1表示不过滤。
	 */
	public static <T extends TreeDomain> void assembleDomain2Tree(Tree parent, Collection<T> nodeList, int statusGe) {
		if (parent == null) {
			throw new BizException("parent参数不能为空");
		}
		if (nodeList != null && !nodeList.isEmpty()) {
			CopyOnWriteArrayList<Tree> nodes = new CopyOnWriteArrayList<>();
			if (nodeList != null && !nodeList.isEmpty()) {
				for (TreeDomain enode : nodeList) {
					Tree tnode = enode.toTreeNode();
					nodes.add(tnode);
				}
			}
			Trees.assembleTree(parent, nodes, statusGe);
		}
	}

	/**
	 * 把nodeList中的根节点挂到parent上，返回一颗树，支持自定义字段。
	 *
	 * @param parent
	 * @param nodeList
	 * @param statusGe      忽略小于status的状态的节点。-1表示不过滤。
	 * @param extendsFields 扩展属性
	 */
	public static <T extends TreeDomain> void assembleDomain2Tree(Tree parent, CopyOnWriteArrayList<T> nodeList,
			int statusGe, String[] extendsFields) {
		CopyOnWriteArrayList<Tree> nodes = new CopyOnWriteArrayList<>();
		if (nodeList != null && !nodeList.isEmpty()) {
			for (TreeDomain enode : nodeList) {
				Tree tnode = Trees.createTreeNode(null, enode, extendsFields);
				nodes.add(tnode);
			}
		}
		Trees.assembleTree(parent, nodes, statusGe);
	}

	private static <PK extends Serializable> Tree createTreeNode(String parentId, TreeDomain node,
			String[] extendsFields) {
		Tree item = new Tree(node.getId().toString(), node.getUuid(),
				Strings.isBlank(parentId) ? node.getParentId().toString() : parentId, node.getName(), node.getGovCode(),
				String.valueOf(node.getType()), node.getStatus());

		if (extendsFields != null && extendsFields.length > 0) {
			for (int i = 0; i < extendsFields.length; i++) {
				Object fv = Trees.getFieldValueByFieldName(extendsFields[i], node);
				item.addAttribute(extendsFields[i], fv);
			}
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
			Field field = Trees.getFieldByClasss(fieldName, object);
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

	public static int checked(Tree tree, Long[] checkedIds) {
		int checkCount = 0;
		if (tree != null && checkedIds != null && checkedIds.length > 0) {
			for (Long checkedId : checkedIds) {
				boolean hasCheched = Trees.checked(tree, checkedId);
				if (hasCheched) {
					checkCount++;
				}
			}
		}
		return checkCount;
	}

	public static boolean checked(Tree tree, Long checkedId) {
		if (tree != null && checkedId != null) {
			if (tree.getId().equals(checkedId.toString())) {
				tree.setChecked(true);
				return true;
			}
			if (tree.getChildren() != null && tree.getChildren().size() > 0) {
				for (Tree item : tree.getChildren()) {
					boolean hasCheched = Trees.checked(item, checkedId);
					if (hasCheched) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static List<String> getIds(List<Tree> items) {
		List<String> ids = new ArrayList<String>();
		if (items != null && !items.isEmpty()) {
			for (Tree item : items) {
				ids.add(item.getId());
			}
		}
		return ids;
	}

	public static List<Long> getLongIds(List<Tree> items, boolean plus) {
		List<Long> ids = new ArrayList<Long>();
		if (items != null && !items.isEmpty()) {
			for (Tree item : items) {
				String id = item.getId();
				if (plus) {
					if (id.startsWith("-")) {
						id = id.substring(1);
					}
				}
				ids.add(Long.parseLong(id));
			}
		}
		return ids;
	}

	public static List<String> getIds(Tree tree) {
		List<String> ids = new ArrayList<String>();
		if (tree != null) {
			parseIds(tree, ids);
		}
		return ids;
	}

	public static void parseIds(Tree tree, List<String> ids) {
		if (ids != null && tree != null) {
			ids.add(tree.getId());
			if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
				for (Tree child : tree.getChildren()) {
					parseIds(child, ids);
				}
			}
		}
	}

	public static List<Long> getLongIds(Tree tree, boolean plus) {
		List<Long> ids = new ArrayList<Long>();
		if (tree != null) {
			parseLongIds(tree, ids, plus);
		}
		return ids;
	}

	public static void parseLongIds(Tree tree, List<Long> ids, boolean plus) {
		if (ids != null && tree != null) {
			String id = tree.getId();
			if (plus) {
				if (id.startsWith("-")) {
					id = id.substring(1);
				}
			}
			ids.add(Long.parseLong(id));

			if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
				for (Tree child : tree.getChildren()) {
					parseLongIds(child, ids, plus);
				}
			}
		}
	}

//	public static List<Tree> cast2TreeNodes(List<TreeDomain> domains) {
//		List<Tree> nodes = new ArrayList<Tree>();
//		if(domains!=null && !domains.isEmpty()) {
//			for(TreeDomain domain: domains) {
//				Tree node = domain.toTreeNode();
//				nodes.add(node);
//			}
//		}
//		return nodes;
//	}

	/**
	 * 克隆树节点
	 * 
	 * @param node
	 * @return
	 */
	public static Tree clone(Tree node) {
		Tree result = new Tree(node.getId(), node.getUuid(), node.getpId(), node.getName(), node.getGovCode(),
				node.getType(), node.getStatus());
		result.setFullName(node.getFullName());
		result.setOpen(node.getOpen());
		result.setChecked(node.getChecked());
		result.setNocheck(node.getNocheck());
		result.setChkDisabled(result.getChkDisabled());
		result.setIcon(node.getIcon());
		result.setIconOpen(node.getIconOpen());
		result.setIconClose(node.getIconClose());
		result.setIconSkin(node.getIconSkin());
		result.setLevel(node.getLevel());
		result.setSort(node.getSort());
		result.setRemark(node.getRemark());
		result.setTitle(node.getTitle());
		result.setValue(node.getValue());
		if (node.getAttributes() != null && !node.getAttributes().isEmpty()) {
			for (String key : node.getAttributes().keySet()) {
				result.addAttribute(key, node.getAttributes().get(key));
			}
		}
		return result;
	}

	/**
	 * 克隆整棵树
	 * 
	 * @param node
	 * @return
	 */
	public static Tree cloneTree(Tree node) {
		Tree result = Trees.clone(node);
		deepClone(result, node.getChildren());
		return result;
	}

	private static void deepClone(Tree parent, List<Tree> children) {
		if (children != null && !children.isEmpty()) {
			for (Tree cnode : children) {
				Tree child = Trees.clone(cnode);
				parent.addChild(child);
				deepClone(child, cnode.getChildren());
			}
		}
	}

	/**
	 * 克隆整棵树中type与根节点相同的节点
	 * 
	 * @param node
	 * @return
	 */
	public static Tree cloneTreeType(Tree node) {
		Tree result = Trees.clone(node);
		deepCloneType(result, node.getChildren());
		return result;
	}

	private static void deepCloneType(Tree parent, List<Tree> children) {
		if (children != null && !children.isEmpty()) {
			for (Tree cnode : children) {
				if (cnode.getType().equals(parent.getType())) {
					Tree child = Trees.clone(cnode);
					parent.addChild(child);
					deepCloneType(child, cnode.getChildren());
				}
			}
		}
	}

	/**
	 * 从orgTree中过滤得到某type的子树。同一层级节点type不唯一，返回第一个节点。
	 * 
	 * @param tree
	 * @param type
	 * @param virtual 若找不到是否挂一个虚拟节点
	 * @return
	 */
	public static Tree orgTreeTypeFilter(Tree orgTree, int type, boolean virtual) {
		Tree typeTree = null;
		if (orgTree != null && orgTree.getChildren() != null && !orgTree.getChildren().isEmpty()) {
			// 优先从部门中找
			typeTree = orgTreeTypeFilterDepartments(orgTree, type);

			// 部门中无此类型，从所有子节点中找
			if (typeTree == null) {
				String stype = type + "";
				for (Tree child : orgTree.getChildren()) {
					if (child.getType().equals(stype)) {
						typeTree = Trees.cloneTreeType(child);
						break;
					}
				}
			}

			// 找不到，挂虚拟节点
			if (typeTree == null && virtual == true) {
				typeTree = Trees.vroot(orgTree.getName());
			}

			if (typeTree != null) {
				// 过滤挂载下级
				for (Tree child : orgTree.getChildren()) {
					if ("Company".equals(child.getAttribute("alias"))) {
						if (!typeTree.getId().equals(child.getId())) {
							Tree childTypeTree = orgTreeTypeFilter(child, type, false);
							if (childTypeTree != null) {
								typeTree.addChild(childTypeTree);
							}
						}
					}
				}
			}
		}
		return (Trees.isVirtual(typeTree) && (typeTree.getChildren() == null || typeTree.getChildren().isEmpty()))
				? null
				: typeTree;
	}

	/**
	 * 从部门中找机构类型为type的机构
	 * 
	 * @param orgTree
	 * @param type
	 * @return
	 */
	private static Tree orgTreeTypeFilterDepartments(Tree orgTree, int type) {
		Tree typeTree = null;
		if (orgTree != null && orgTree.getChildren() != null && !orgTree.getChildren().isEmpty()) {
			String stype = type + "";

			// 优先从部门中找
			for (Tree child : orgTree.getChildren()) {
				if (!"Company".equals(child.getAttribute("alias"))) {
					if (child.getType().equals(stype)) {
						typeTree = Trees.cloneTreeType(child);
						break;
					}
				}
			}

			if (typeTree == null) {
				for (Tree child : orgTree.getChildren()) {
					if (!"Company".equals(child.getAttribute("alias"))) {
						typeTree = orgTreeTypeFilterDepartments(child, type);
						if (typeTree != null) {
							return typeTree;
						}
					}
				}
			}
		}
		return typeTree;
	}

}
