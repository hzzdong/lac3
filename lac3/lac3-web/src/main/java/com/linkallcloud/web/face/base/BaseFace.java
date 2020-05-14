package com.linkallcloud.web.face.base;

import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.*;
import com.linkallcloud.core.face.message.response.ErrorFaceResponse;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    Object fetch(IdFaceRequest faceReq, Trace t, SessionUser su) {
        if (faceReq.getId() == null || Strings.isBlank(faceReq.getUuid())) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
        }
        T entity = doFetch(t, faceReq.getId(), faceReq.getUuid(), su);
        return convert(t, "fetch", faceReq, entity);
    }

    @Face(simple = true)
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public @ResponseBody
    Object find(ListFaceRequest faceReq, Trace t, SessionUser su) {
        List<T> entities = doFind(t, faceReq.getQuery(), su);
        return converts(t, "find", faceReq, entities);
    }

    protected List<T> doFind(Trace t, WebQuery wq, SessionUser su) {
        Query q = new Query();
        if (wq != null) {
            q = wq.toQuery();
        }
        return manager().find(t, q);
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
        if (!checkReferer(true)) {
            return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
        }
        T entity = fr.getData();
        doSave(t, entity, su);
        return convert(t, "save", fr, entity);
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
    @WebLog(db = true, desc = "用户([(${su.sid.name})])删除了[(${domainShowName})],ID([(${fr.id})]), TID:[(${tid})]")
    public @ResponseBody
    Object delete(IdFaceRequest fr, Trace t, SessionUser su) {
        if (!checkReferer(true)) {
            return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
        }
        if (fr.getId() == null || Strings.isBlank(fr.getUuid())) {
            throw new BizException(Exceptions.CODE_ERROR_DELETE, "参数错误");
        }
        return doDelete(t, fr.getId(), fr.getUuid(), su);
    }

    protected Boolean doDelete(Trace t, Long id, String uuid, SessionUser su) {
        if (id == null || Strings.isBlank(uuid)) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
        }
        return manager().delete(t, id, uuid);
    }

    @WebLog(db = true, desc = "用户([(${su.sid.name})])修改了 [(${domainShowName})],ID([(${fr.id})])的状态为([(${fr.status})]), TID:[(${tid})]")
    @Face(simple = true)
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public @ResponseBody
    Object changeStatus(StatusFaceRequest fr, Trace t, SessionUser su) {
        if (!checkReferer(true)) {
            return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
        }
        if (fr.getId() == null || Strings.isBlank(fr.getUuid())) {
            throw new BizException(Exceptions.CODE_ERROR_UPDATE, "参数错误");
        }
        return doChangeStatus(t, fr.getStatus(), fr.getId(), fr.getUuid(), su);
    }

    protected Boolean doChangeStatus(Trace t, int status, Long id, String uuid, SessionUser su) {
        if (id == null || Strings.isBlank(uuid)) {
            throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误");
        }
        return manager().updateStatus(t, status, id, uuid);
    }

    @Face(simple = true)
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public @ResponseBody
    Object page(PageFaceRequest faceReq, Trace t, SessionUser su) {
        Page<T> page = new Page<>(faceReq);
        page = doPage(t, page, su);
        return convert(t, "page", faceReq, page);
    }

    protected Page<T> doPage(Trace t, Page<T> page, SessionUser su) {
        return manager().findPage(t, page);
    }

    @Face(simple = true)
    @RequestMapping(value = "/page4Select", method = RequestMethod.POST)
    public @ResponseBody
    Object page4Select(PageFaceRequest faceReq, Trace t, SessionUser su) {
        Page<T> page = new Page<>(faceReq);
        page = doPage4Select(t, page, su);
        return convert(t, "page4Select", faceReq, page);
    }

    protected Page<T> doPage4Select(Trace t, Page<T> page, SessionUser su) {
        return manager().findPage4Select(t, page);
    }

    /**
     * 再次进行类型转换
     *
     * @param method
     * @param entity
     * @return
     */
    protected Object convert(Trace t, String method, FaceRequest fr, T entity) {
        return entity;
    }

    protected Object converts(Trace t, String method, FaceRequest fr, List<T> entities) {
        return entities;
    }

    protected Object convert(Trace t, String method, FaceRequest fr, Page<T> page) {
        return page;
    }

    /**
     * 防CSRF攻击，referer验证
     *
     * @param needReferer
     * @return
     */
    protected boolean checkReferer(boolean needReferer) {
        HttpServletRequest request = Controllers.getHttpRequest();
        if (request != null) {
            String referer = request.getHeader("referer");
            if (needReferer && Strings.isBlank(referer)) {
                log.warnf("此方法（%s）需要Referer检查，但是其值未空。", request.getRequestURI());
                return false;
            }

            StringBuffer sb = new StringBuffer();
            String scheme = Controllers.getRequestScheme(request);
            sb.append(scheme).append("://").append(Controllers.getRequestServerName(request));
            if (referer != null && !referer.equals("")) {
                String server = String.valueOf(sb);
                //if (referer.lastIndexOf(server) != 0) {
                if (!referer.startsWith(server)) {
                    log.warnf("Referer（%s）检查失败，Server（%s）。", referer, server);
                    return false; //验证失败
                }
            }
        } else {
            log.warn("Controllers.getHttpRequest() is null,放弃本次Referer检查。");
        }
        return true;
    }

    /**
     * fd：把登录用户的area信息作为查询条件加入page中
     *
     * @param page
     * @param av
     */
    public void addAreaCnd2Page(Page<T> page, AppVisitor av) {
        if (av != null) {
            if (av.getAreaId() == null) {
                page.addRule(new Equal("level", 0));
                page.addRule(new Equal("areaId", 0L));
            } else {
                page.addRule(new Equal("level", av.getAreaLevel()));
                page.addRule(new Equal("areaId", av.areaId()));
            }
        }
    }

    /**
     * fd：把登录用户的area信息作为查询条件加入page中
     *
     * @param page
     * @param suser
     */
    public void addAreaCnd2Page(Page<T> page, SessionUser suser) {
        if (suser != null) {
            if (suser.getArea() == null) {
                page.addRule(new Equal("level", 0));
                page.addRule(new Equal("areaId", 0L));
            } else {
                page.addRule(new Equal("level", suser.getAreaLevel()));
                page.addRule(new Equal("areaId", suser.areaId()));
            }
        }
    }

}
