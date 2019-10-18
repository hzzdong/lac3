package com.linkallcloud.core.activity;

import com.linkallcloud.core.dao.ITreeDao;
import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.util.Domains;

import java.util.List;

public abstract class BaseTreeActivity<T extends TreeDomain, D extends ITreeDao<T>> extends BaseActivity<T, D> implements ITreeActivity<T> {

    @Override
    public T fetchByIdUuidJoinParent(Trace t, Long id, String uuid, String parentClass) {
        return dao().fetchByIdUuidJoinParent(t, id, uuid, parentClass);
    }

    @Override
    public List<Tree> getTreeNodes(Trace t, boolean valid) {
        Query query = new Query();
        if (valid) {
            query.addRule(new Equal("status", 0));
        }

        List<T> list = dao().find(t, query);
        return Trees.assembleTreeList(list);
    }

    @Override
    public Tree getTree(Trace t, boolean valid) {
        Query query = new Query();
        if (valid) {
            query.addRule(new Equal("status", 0));
        }

        List<T> list = dao().find(t, query);
        Tree root = new Tree("TREE_ROOT", null, "根节点");
        // Trees.assembleTree(root, new CopyOnWriteArrayList<T>(list));
        Trees.assembleDomain2Tree(root, list);
        return root;
    }

    @Override
    public Boolean updateCode(Trace t, Long id, String code) {
        int rows = dao().updateCode(t, id, code);
        if (rows > 0) {
            log.debugf("%s, updateCode 成功，tid：%s, id:%s", getDomainClass().getName(), t.getTid(), id);
        } else {
            log.debugf("%s, updateCode 失败，tid：%s, id:%s", getDomainClass().getName(), t.getTid(), id);
        }
        return retBool(rows);
    }


    @Override
    protected void before(Trace t, boolean isNew, T entity) {
        super.before(t, isNew, entity);
        treeBefore(t, isNew, entity);
    }

    protected void treeBefore(Trace t, boolean isNew, T entity) {
        if (isNew) {
            if (!Strings.isBlank(entity.getGovCode())) {
                T dbEntity = dao().fetchByGovCode(t, entity.getGovCode());
                if (dbEntity != null) {
                    throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER,
                            "已经存在相同govCode的节点：" + entity.getGovCode());
                }
            }
            if (entity.isTopParent()) {
                entity.setLevel(1);
            } else {
                T parent = dao().fetchById(t, entity.getParentId());
                if (parent != null) {
                    entity.setLevel(parent.getLevel() + 1);
                }
            }
        } else {// 修改
            updateCodeIfModifiedParent(t, entity);
        }
    }

    protected void updateCodeIfModifiedParent(Trace t, T entity) {
        boolean parentChanged = false;
        T dbEntity = dao().fetchById(t, entity.getId());
        if (dbEntity.isTopParent()) {
            if (!entity.isTopParent()) {
                parentChanged = true;
            }
        } else {
            if (!dbEntity.getParentId().equals(entity.getParentId())) {
                parentChanged = true;
            }
        }

        if (parentChanged) {
            if (entity.isTopParent()) {
                entity.setCode(Domains.genDomainCode(null, entity));
                entity.setLevel(1);
            } else {
                T parent = dao().fetchById(t, entity.getParentId());
                if (parent != null) {
                    entity.setCode(Domains.genDomainCode(parent, entity));
                    entity.setLevel(parent.getLevel() + 1);
                } else {
                    throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "parentId参数错误。");
                }
            }
            updateCode(t, entity.getId(), entity.getCode());
        }
    }

    @Override
    protected void after(Trace t, boolean isNew, T entity) {
        super.after(t, isNew, entity);
        treeAfter(t, isNew, entity);
    }

    protected void treeAfter(Trace t, boolean isNew, T entity) {
        if (isNew) {// 新增
            if (entity.isTopParent()) {
                entity.setCode(Domains.genDomainCode(null, entity));
            } else {
                T parent = dao().fetchById(t, entity.getParentId());
                if (parent != null) {
                    entity.setCode(Domains.genDomainCode(parent, entity));
                } else {
                    throw new BaseRuntimeException(Exceptions.CODE_ERROR_PARAMETER, "parentId参数错误。");
                }
            }
            updateCode(t, entity.getId(), entity.getCode());
        }
    }
}
