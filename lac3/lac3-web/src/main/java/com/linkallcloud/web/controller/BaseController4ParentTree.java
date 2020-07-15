package com.linkallcloud.web.controller;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.manager.ITreeManager;
import org.springframework.ui.ModelMap;

import java.util.List;

public abstract class BaseController4ParentTree<T extends TreeDomain, TS extends ITreeManager<T>, P extends TreeDomain, PS extends ITreeManager<P>>
        extends BaseController4Parent<T, TS, P, PS> {

    public BaseController4ParentTree() {
        super();
    }

    @Override
    protected String doAdd4Parent(boolean prepare, Long parentId, String parentClass, Trace t, ModelMap modelMap,
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
    protected String doEdit4Parent(boolean prepare, Long parentId, String parentClass, Long id, String uuid, Trace t,
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
    protected T doGet4Parent(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
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
