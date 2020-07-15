package com.linkallcloud.core.manager;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.service.ITreeService;

import java.util.List;

public abstract class BaseTreeManager<T extends TreeDomain, S extends ITreeService<T>>
        extends BaseManager<T, S> implements ITreeManager<T> {
    public BaseTreeManager() {
        super();
    }

    @Override
    public T fetchByIdUuidJoinParent(Trace t, Long id, String uuid, String parentClass) {
        return service().fetchByIdUuidJoinParent(t, id, uuid, parentClass);
    }

    @Override
    public List<Tree> getTreeNodes(Trace t, boolean valid) {
        return service().getTreeNodes(t, valid);
    }

    @Override
    public Tree getTree(Trace t, boolean valid) {
        return service().getTree(t, valid);
    }

    @Override
    public Boolean updateCode(Trace t, Long id, String code) {
        return service().updateCode(t, id, code);
    }

}
