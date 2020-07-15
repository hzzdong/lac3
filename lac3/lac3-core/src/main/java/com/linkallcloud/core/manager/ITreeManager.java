package com.linkallcloud.core.manager;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;

import java.util.List;

public interface ITreeManager<T extends TreeDomain> extends IManager<T> {


    /**
     * @param t
     * @param id
     * @param uuid
     * @param parentClass
     * @return
     */
    T fetchByIdUuidJoinParent(Trace t, Long id, String uuid, String parentClass);

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
    Boolean updateCode(Trace t, Long id, String code);

}
