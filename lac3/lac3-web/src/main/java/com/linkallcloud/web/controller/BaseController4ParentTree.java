package com.linkallcloud.web.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.ui.ModelMap;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.manager.ITreeManager;

public abstract class BaseController4ParentTree<PK extends Serializable, T extends TreeDomain<PK>, TS extends ITreeManager<PK, T>, P extends TreeDomain<PK>, PS extends ITreeManager<PK, P>>
        extends BaseController4Parent<PK, T, TS, P, PS> {

    public BaseController4ParentTree() {
        super();
    }

    @Override
    protected String doAdd4Parent(boolean prepare, PK parentId, String parentClass, Trace t, ModelMap modelMap,
            AppVisitor av) {
        modelMap.put("parentId", parentId);
        modelMap.put("parentClass", parentClass);
        if (prepare) {
            T entity = mirror.born();
            modelMap.put("entity", entity);

            List<Tree> parents = parentManager(parentClass).getTreeNodes(t, false);
            modelMap.put("parents", parents);
        }
        return getEditPage();
    }

    @Override
    protected String doEdit4Parent(boolean prepare, PK parentId, String parentClass, PK id, String uuid, Trace t,
            ModelMap modelMap, AppVisitor av) {
        modelMap.put("id", id);
        modelMap.put("uuid", uuid);

        if (prepare) {
            T entity = manager().fetchByIdUuid(t, id, uuid);
            modelMap.put("entity", entity);

            List<Tree> parents = parentManager(parentClass).getTreeNodes(t, false);
            modelMap.put("parents", parents);
        }

        return getEditPage();
    }

    @Override
    protected T doGet4Parent(PK parentId, String parentClass, PK id, String uuid, Trace t, AppVisitor av) {
        T entity = null;
        if (id != null && uuid != null) {
            // return manager().fetchByIdUuidJoinParent(t, id, uuid, parentClass);
            entity = manager().fetchByIdUuid(t, id, uuid);
        } else {
            entity = mirror.born();
            if (entity instanceof TreeDomain) {
                entity.setParentId(parentId);
                entity.setParentClass(parentClass);
            }
        }
        return entity;
    }

}
