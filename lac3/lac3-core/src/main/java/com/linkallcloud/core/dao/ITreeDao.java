package com.linkallcloud.core.dao;

import java.io.Serializable;

import org.apache.ibatis.annotations.Param;

import com.linkallcloud.core.domain.TreeDomain;
import com.linkallcloud.core.dto.Trace;

public interface ITreeDao<T extends TreeDomain> extends IDao<T> {

    /**
     * 根据 ID 查询
     *
     * @param t
     *            trace, 业务流水ID
     * @param govCode
     *            行政编码
     * @return T
     */
    T fetchByGovCode(@Param("t") Trace t, @Param("govCode") String govCode);

    /**
     * 
     * @param t
     * @param id
     * @param uuid
     * @param parentClass
     * @return
     */
    T fetchByIdUuidJoinParent(@Param("t") Trace t, @Param("id") Long id, @Param("uuid") String uuid,
            @Param("parentClass") String parentClass);

}
