package com.linkallcloud.web.controller;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.manager.ITreeManager;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public abstract class BaseTreeContorller<T extends TreeDomain, S extends ITreeManager<T>>
        extends BaseController<T, S> {

    public BaseTreeContorller() {
        super();
    }

    @RequestMapping(value = "/loadTree", method = RequestMethod.GET)
    public @ResponseBody
    Result<Object> loadTree(Trace t, AppVisitor av) {
        List<Tree> tree = doLoadTree(t, av);
        return new Result<Object>(tree);
    }

    protected List<Tree> doLoadTree(Trace t, AppVisitor av) {
        List<Tree> items = manager().getTreeNodes(t, false);
        return items;
    }

    @Override
    protected String doAdd(boolean prepare, Long parentId, String parentClass, Trace t, ModelMap modelMap,
                           AppVisitor av) {
        modelMap.put("parentId", parentId);

        if (prepare) {
            List<Tree> items = manager().getTreeNodes(t, false);
            modelMap.put("nodeList", items);

            T entity = mirror.born();
            entity.setParentId(parentId);
            modelMap.put("entity", entity);
        }

        return getEditPage();
    }

    @Override
    protected String doEdit(boolean prepare, Long parentId, String parentClass, Long id, String uuid, Trace t,
                            ModelMap modelMap, AppVisitor av) {
        modelMap.put("id", id);
        modelMap.put("uuid", uuid);

        if (prepare) {
            List<Tree> items = manager().getTreeNodes(t, false);
            modelMap.put("nodeList", items);

            T entity = manager().fetchByIdUuid(t, id, uuid);
            modelMap.put("entity", entity);
        }

        return getEditPage();
    }

    @Override
    protected T doGet(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
        T entity = null;
        if (id != null && uuid != null) {
            entity = manager().fetchByIdUuid(t, id, uuid);
            if (entity.getParentId() == null) {
                // entity.setParentId(0L);
            }
        } else {
            entity = mirror.born();
            // entity.setParentId(parentId == null ? 0 : parentId);
            entity.setParentId(parentId);
        }
        return entity;
    }

}
