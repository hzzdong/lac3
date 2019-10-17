package com.linkallcloud.web.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.ui.ModelMap;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.IManager;

public abstract class BaseController4Parent<PK extends Serializable, T extends Domain<PK>, TS extends IManager<PK, T>, P extends Domain<PK>, PS extends IManager<PK, P>>
        extends BaseController<PK, T, TS> {

    public BaseController4Parent() {
        super();
    }

    protected abstract PS parentManager(String parentClass);

    @Override
    protected String doMain(boolean prepare, PK parentId, String parentClass, Trace t, ModelMap modelMap,
            AppVisitor av) {
        return doMain4Parent(prepare, parentId, parentClass, t, modelMap, av);
    }

    /**
     * 访问对象主页的时候，需要进行父对象相关信息处理
     * 
     * @param prepare
     * @param parentId
     * @param parentClass
     * @param t
     * @param modelMap
     * @param av
     * @return
     */
    protected String doMain4Parent(boolean prepare, PK parentId, String parentClass, Trace t, ModelMap modelMap,
            AppVisitor av) {
        modelMap.put("parentId", parentId);
        modelMap.put("parentClass", parentClass);
        if (prepare && parentId != null) {
            P parent = parentManager(parentClass).fetchById(t, parentId);
            modelMap.put("parent", parent);
        }
        return getMainPage();
    }

    @Override
    protected String doAdd(boolean prepare, PK parentId, String parentClass, Trace t, ModelMap modelMap,
            AppVisitor av) {
        return doAdd4Parent(prepare, parentId, parentClass, t, modelMap, av);
    }

    /**
     * 添加操作的时候，需要进行父对象相关信息处理
     * 
     * @param prepare
     * @param parentId
     * @param parentClass
     * @param t
     * @param modelMap
     * @param av
     * @return
     */
    protected String doAdd4Parent(boolean prepare, PK parentId, String parentClass, Trace t, ModelMap modelMap,
            AppVisitor av) {
        modelMap.put("parentId", parentId);
        modelMap.put("parentClass", parentClass);
        if (prepare) {
            T entity = mirror.born();
            if (entity instanceof TreeDomain) {
                ((TreeDomain<PK>) entity).setParentId(parentId);
                ((TreeDomain<PK>) entity).setParentClass(parentClass);
            }
            modelMap.put("entity", entity);

            List<P> parents = parentManager(parentClass).find(t, null);
            modelMap.put("parents", parents);
        }
        return getEditPage();
    }

    @Override
    protected String doEdit(boolean prepare, PK parentId, String parentClass, PK id, String uuid, Trace t,
            ModelMap modelMap, AppVisitor av) {
        return doEdit4Parent(prepare, parentId, parentClass, id, uuid, t, modelMap, av);
    }

    /**
     * 编辑操作的时候，需要进行父对象相关信息处理
     * 
     * @param prepare
     * @param id
     * @param uuid
     * @param t
     * @param modelMap
     * @param av
     * @return
     */
    protected String doEdit4Parent(boolean prepare, PK parentId, String parentClass, PK id, String uuid, Trace t,
            ModelMap modelMap, AppVisitor av) {
        modelMap.put("id", id);
        modelMap.put("uuid", uuid);

        if (prepare) {
            T entity = manager().fetchByIdUuid(t, id, uuid);
            modelMap.put("entity", entity);

            List<P> parents = parentManager(parentClass).find(t, null);
            modelMap.put("parents", parents);
        }

        return getEditPage();
    }

    @Override
    protected T doGet(PK parentId, String parentClass, PK id, String uuid, Trace t, AppVisitor av) {
        return doGet4Parent(parentId, parentClass, id, uuid, t, av);
    }

    /**
     * 
     * @param parentId
     * @param id
     * @param uuid
     * @param t
     * @param av
     * @return
     */
    protected T doGet4Parent(PK parentId, String parentClass, PK id, String uuid, Trace t, AppVisitor av) {
        if (id != null && uuid != null) {
            return manager().fetchByIdUuid(t, id, uuid);
        } else {
            T entity = mirror.born();
            if (entity instanceof TreeDomain) {
                ((TreeDomain<PK>) entity).setParentId(parentId);
                ((TreeDomain<PK>) entity).setParentClass(parentClass);
            }
            return entity;
        }
    }

}
