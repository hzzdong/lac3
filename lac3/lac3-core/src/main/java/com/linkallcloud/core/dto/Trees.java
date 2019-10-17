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

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.lang.Strings;

public class Trees {

    public static List<Tree> tree2List(Tree tree) {
        List<Tree> result = new ArrayList<Tree>();
        if (tree != null) {
            tree2List(result, tree);
        }
        if (result.size() > 0) {
            for (Tree item : result) {
                item.setChildren(null);
            }
        }
        return result;
    }

    private static void tree2List(List<Tree> result, Tree tree) {
        result.add(tree);
        if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
            for (Tree item : tree.getChildren()) {
                tree2List(result, item);
            }
        }

    }

    public static Map<String, Tree> tree2Map(Tree tree) {
        Map<String, Tree> result = new HashMap<>();
        assembleTree2Map(result, tree);
        return result;
    }

    private static void assembleTree2Map(Map<String, Tree> result, Tree tree) {
        if (tree == null) {
            return;
        }

        if (tree.getId() != null) {
            result.put(tree.getId(), tree);
        }

        if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
            for (Tree item : tree.getChildren()) {
                assembleTree2Map(result, item);
            }
        }

    }

    /**
     * 把nodeList中的根节点作为直接子节点挂到parent上
     * 
     * @param parent
     * @param nodeList
     */
    public static <PK extends Serializable, T extends TreeDomain<PK>> void assembleDirectTreeNode(Tree parent,
            List<T> nodeList, String idPreFix) {
        if (parent == null) {
            parent = new Tree("0", null, "虚拟根节点");
        }

        if (nodeList == null || nodeList.isEmpty()) {
            return;
        }

        for (TreeDomain<PK> node : nodeList) {
            Tree item = node.toTreeNode();
            item.setpId(parent.getId());
            if (!Strings.isBlank(idPreFix)) {
                item.setId(idPreFix + item.getId());
            }
            parent.addChild(item);
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
     * 把nodeList挂成rootId的直接子节点
     * 
     * @param rootId
     * @param nodeList
     * @param idPreFix
     * @return
     */
    public static <PK extends Serializable, T extends TreeDomain<PK>> List<Tree>
            assembleDirectTreeNodeList(String rootId, List<T> nodeList, String idPreFix) {
        if (nodeList == null || nodeList.isEmpty()) {
            return null;
        }
        List<Tree> result = new ArrayList<Tree>();
        for (TreeDomain<PK> node : nodeList) {
            Tree item = node.toTreeNode();
            item.setpId(rootId);
            if (!Strings.isBlank(idPreFix)) {
                item.setId(idPreFix + item.getId());
            }
            result.add(item);
        }

        return result;
    }

    /**
     * 把nodeList中节点挂成父子关系
     * 
     * @param nodeList
     * @return
     */
    public static <PK extends Serializable, T extends TreeDomain<PK>> List<Tree> assembleTreeList(List<T> nodeList) {
        return Trees.assembleTreeList(null, nodeList, -1);
    }

    /**
     * 把nodeList中的根节点挂到rootId上，其它正常挂成父子关系
     * 
     * @param rootId
     * @param nodeList
     * @return
     */
    public static <PK extends Serializable, T extends TreeDomain<PK>> List<Tree> assembleTreeList(String rootId,
            List<T> nodeList) {
        return Trees.assembleTreeList(rootId, nodeList, -1);
    }

    /**
     * 把nodeList中的根节点挂到rootId上，其它正常挂成父子关系，支持自定义扩展字段。
     * 
     * @param rootId
     * @param nodeList
     * @param statusGe
     *            忽略小于status的状态的节点。-1表示不过滤。
     * @param extendsFields
     *            扩展属性
     * @return
     */
    public static <PK extends Serializable, T extends TreeDomain<PK>> List<Tree> assembleTreeList(String rootId,
            List<T> nodeList, int statusGe, String[] extendsFields) {
        if (nodeList == null || nodeList.isEmpty()) {
            return null;
        }

        List<Tree> result = new ArrayList<Tree>();
        for (TreeDomain<PK> node : nodeList) {
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
     * @param statusGe
     *            忽略小于status的状态的节点。-1表示不过滤。
     * @return
     */
    public static <PK extends Serializable, T extends TreeDomain<PK>> List<Tree> assembleTreeList(String rootId,
            List<T> nodeList, int statusGe) {
        List<Tree> result = new ArrayList<Tree>();

        if (nodeList == null || nodeList.isEmpty()) {
            return result;
        }

        for (TreeDomain<PK> node : nodeList) {
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
     * @param root
     *            若root不为null，这把nodes中的第一层节点挂到root下，并和root一起返回
     * @param nodes
     * @return
     */
    public static <PK extends Serializable, T extends TreeDomain<PK>> List<Tree> assembleDomain2List(Tree root,
            List<T> nodeList) {
        CopyOnWriteArrayList<Tree> nodes = new CopyOnWriteArrayList<>();
        if (nodeList != null && !nodeList.isEmpty()) {
            for (TreeDomain<PK> enode : nodeList) {
                Tree tnode = enode.toTreeNode();
                nodes.add(tnode);
            }
        }
        return Trees.assembleTrees2List(root, nodes);
    }

    /**
     * 把nodes检查父子关系，计算后与root挂起来，并把有效的树节点返回
     * 
     * @param root
     *            若root不为null，这把nodes中的第一层节点挂到root下，并和root一起返回
     * @param nodes
     * @return
     */
    public static List<Tree> assembleTrees2List(Tree root, CopyOnWriteArrayList<Tree> nodes) {
        Trees.assembleTree(root, nodes, -1);
        return Trees.tree2List(root);
    }

    /**
     * 把nodeList中的根节点挂到parent上，返回一颗树parent
     * 
     * @param parent
     * @param nodes
     */
    public static void assembleTree(Tree parent, List<Tree> nodes) {
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
     * @param statusGe
     *            忽略小于status的状态的节点。-1表示不过滤。
     */
    public static void assembleTree(Tree parent, CopyOnWriteArrayList<Tree> nodes, int statusGe) {
        if (parent == null) {
            parent = new Tree("0", null, "虚拟根节点");
        }

        if (nodes == null || nodes.isEmpty()) {
            return;
        }

        if (parent.getId() == null || parent.getpId() == null) {// 根节点
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
    public static <PK extends Serializable, T extends TreeDomain<PK>> void assembleDomain2Tree(Tree parent,
            Collection<T> nodeList) {
        Trees.assembleDomain2Tree(parent, nodeList, -1);
    }

    /**
     * 把nodeList中的根节点挂到parent上，返回一颗树
     * 
     * @param parent
     * @param nodeList
     * @param statusGe
     *            忽略小于status的状态的节点。-1表示不过滤。
     */
    public static <PK extends Serializable, T extends TreeDomain<PK>> void assembleDomain2Tree(Tree parent,
            Collection<T> nodeList, int statusGe) {
        CopyOnWriteArrayList<Tree> nodes = new CopyOnWriteArrayList<>();
        if (nodeList != null && !nodeList.isEmpty()) {
            for (TreeDomain<PK> enode : nodeList) {
                Tree tnode = enode.toTreeNode();
                nodes.add(tnode);
            }
        }
        Trees.assembleTree(parent, nodes, statusGe);
    }

    /**
     * 把nodeList中的根节点挂到parent上，返回一颗树，支持自定义字段。
     * 
     * @param parent
     * @param nodeList
     * @param statusGe
     *            忽略小于status的状态的节点。-1表示不过滤。
     * @param extendsFields
     *            扩展属性
     */
    public static <PK extends Serializable, T extends TreeDomain<PK>> void assembleDomain2Tree(Tree parent,
            CopyOnWriteArrayList<T> nodeList, int statusGe, String[] extendsFields) {
        CopyOnWriteArrayList<Tree> nodes = new CopyOnWriteArrayList<>();
        if (nodeList != null && !nodeList.isEmpty()) {
            for (TreeDomain<PK> enode : nodeList) {
                Tree tnode = Trees.createTreeNode(null, enode, extendsFields);
                nodes.add(tnode);
            }
        }
        Trees.assembleTree(parent, nodes, statusGe);
    }

    private static <PK extends Serializable> Tree createTreeNode(String parentId, TreeDomain<PK> node,
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
}
