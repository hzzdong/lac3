package com.linkallcloud.core.service;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;

import java.io.Serializable;
import java.util.List;

public interface ITreeService<PK extends Serializable, T extends TreeDomain<PK>> extends IService<PK, T> {

    /**
     * 获取树节点列表
     *
     * @param t
     * @param valid
     * @return
     */
    List<Tree> getTreeNodes(Trace t, boolean valid);

    /**
     * 获取树
     *
     * @param t
     * @param valid
     * @return
     */
    Tree getTree(Trace t, boolean valid);

    /**
     * 更新系统编码
     *
     * @param t
     * @param id
     * @param code 系统编码
     * @return
     */
    Boolean updateCode(Trace t, PK id, String code);

    /**
     * @param t
     * @param id
     * @param uuid
     * @param parentClass
     * @return
     */
    T fetchByIdUuidJoinParent(Trace t, PK id, String uuid, String parentClass);

}
