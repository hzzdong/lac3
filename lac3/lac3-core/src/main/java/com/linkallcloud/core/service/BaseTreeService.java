package com.linkallcloud.core.service;

import com.linkallcloud.core.activity.ITreeActivity;
import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class BaseTreeService<T extends TreeDomain, A extends ITreeActivity<T>>
        extends BaseService<T, A> implements ITreeService<T> {

    public BaseTreeService() {
        super();
    }

    @Override
    public T fetchByIdUuidJoinParent(Trace t, Long id, String uuid, String parentClass) {
        return activity().fetchByIdUuidJoinParent(t, id, uuid, parentClass);
    }

    @Override
    public List<Tree> getTreeNodes(Trace t, boolean valid) {
        return activity().getTreeNodes(t, valid);
    }

    @Override
    public Tree getTree(Trace t, boolean valid) {
        return activity().getTree(t, valid);
    }

    @Transactional
    @Override
    public Boolean updateCode(Trace t, Long id, String code) {
        return activity().updateCode(t, id, code);
    }

}
