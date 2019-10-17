package com.linkallcloud.core.manager;

import java.io.Serializable;
import java.util.List;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.service.ITreeService;

public abstract class TreeDomainManager<PK extends Serializable, T extends TreeDomain<PK>, S extends ITreeService<PK, T>>
        extends BaseManager<PK, T, S> implements ITreeManager<PK, T> {

    @Override
    public T fetchByIdUuidJoinParent(Trace t, PK id, String uuid, String parentClass) {
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
    public Boolean updateCode(Trace t, PK id, String code) {
        return service().updateCode(t, id, code);
    }

}
