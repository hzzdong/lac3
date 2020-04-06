package com.linkallcloud.web.face.base;

import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.StatusFaceRequest;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class BaseFace<T extends Domain, S extends IManager<T>> {
    protected final Log log = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public BaseFace() {
        super();
        try {
            mirror = Mirror.me((Class<T>) Mirror.getTypeParams(getClass())[0]);
        } catch (Throwable e) {
            if (log.isWarnEnabled()) {
                log.warn("!!!Fail to get TypeParams for self!", e);
            }
        }
    }

    public Class<T> getDomainClass() {
        return (null == mirror) ? null : mirror.getType();
    }

    protected abstract S manager();

    @Face(simple = true)
    @RequestMapping(value = "/fetch", method = RequestMethod.POST)
    public @ResponseBody
    Object fetchById(IdFaceRequest faceReq, Trace t, SessionUser su) {
        if (Strings.isBlank(faceReq.getId()) || Strings.isBlank(faceReq.getUuid())) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
        }
        T entity = doFetch(t, Long.parseLong(faceReq.getId()), faceReq.getUuid(), su);
        return convert("fetch", entity);
    }

    /**
     * 再次进行类型转换
     *
     * @param method
     * @param entity
     * @return
     */
    protected Object convert(String method, T entity) {
        return entity;
    }

    protected T doFetch(Trace t, Long id, String uuid, SessionUser su) {
        if (id == null || Strings.isBlank(uuid)) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
        }
        return manager().fetchByIdUuid(t, id, uuid);
    }

    @WebLog(db = true)
    @Face(simple = true)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody
    Object save(ObjectFaceRequest<T> fr, Trace t, SessionUser su) {
        T entity = fr.getData();
        doSave(t, entity, su);
        return convert("save", entity);
    }

    protected void doSave(Trace t, T entity, SessionUser su) {
        if (entity.getId() != null && entity.getUuid() != null) {
            manager().update(t, entity);
        } else {
            entity.setUuid(entity.generateUuid());
            Long id = manager().insert(t, entity);
            entity.setId(id);
        }
    }

    @Face(simple = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @WebLog(db = true, desc = "用户([(${visitor.name})])删除了[(${domainShowName})], TID:[(${tid})]")
    public @ResponseBody
    Object delete(IdFaceRequest faceReq, Trace t, SessionUser su) {
        if (Strings.isBlank(faceReq.getId()) || Strings.isBlank(faceReq.getUuid())) {
            throw new BizException(Exceptions.CODE_ERROR_DELETE, "参数错误");
        }
        return doDelete(t, Long.parseLong(faceReq.getId()), faceReq.getUuid(), su);
    }

    protected Boolean doDelete(Trace t, Long id, String uuid, SessionUser su) {
        if (id == null || Strings.isBlank(uuid)) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
        }
        return manager().delete(t, id, uuid);
    }

    @Face(simple = true)
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public @ResponseBody
    Object changeStatus(StatusFaceRequest sfr, Trace t, SessionUser su) {
        if (Strings.isBlank(sfr.getId()) || Strings.isBlank(sfr.getUuid())) {
            throw new BizException(Exceptions.CODE_ERROR_UPDATE, "参数错误");
        }
        return doChangeStatus(t, sfr.getStatus(), Long.parseLong(sfr.getId()), sfr.getUuid(), su);
    }

    protected Boolean doChangeStatus(Trace t, int status, Long id, String uuid, SessionUser su) {
        if (id == null || Strings.isBlank(uuid)) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
        }
        return manager().updateStatus(t, status, id, uuid);
    }
}
