package com.linkallcloud.web.controller;

import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.manager.IManager;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;
import com.linkallcloud.web.utils.Controllers;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public abstract class BaseController<T extends Domain, S extends IManager<T>> {
    protected Log log = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public BaseController() {
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

    /**
     * 首页
     *
     * @return
     */
    protected String getMainPage() {
        return "page/" + getDomainClass().getSimpleName() + "/main";
    }

    /**
     * 编辑页面
     *
     * @return
     */
    protected String getEditPage() {
        return "page/" + getDomainClass().getSimpleName() + "/edit";
    }

    protected String getViewPage() {
        return "page/" + getDomainClass().getSimpleName() + "/view";
    }

    /**
     * 选择页面
     *
     * @return
     */
    protected String getSelectPage() {
        return "page/" + getDomainClass().getSimpleName() + "/select";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(@RequestParam(value = "prepare", required = false) boolean prepare,
                       @RequestParam(value = "parentId", required = false) Long parentId,
                       @RequestParam(value = "parentClass", required = false) String parentClass, Trace t, ModelMap modelMap,
                       AppVisitor av) {
        return doMain(prepare, parentId, parentClass, t, modelMap, av);
    }

    /**
     * @param prepare
     * @param parentId
     * @param parentClass
     * @param t
     * @param modelMap
     * @param av
     * @return
     */
    protected String doMain(boolean prepare, Long parentId, String parentClass, Trace t, ModelMap modelMap,
                            AppVisitor av) {
        modelMap.put("parentId", parentId);
        modelMap.put("parentClass", parentClass);
        return getMainPage();
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public @ResponseBody
    Result<Object> page(@RequestBody WebPage webPage, Trace t, AppVisitor av) {
        Page<T> page = doFindPage(webPage, t, av);
        return new Result<Object>(page);
    }

    protected Page<T> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
        Page<T> page = webPage.toPage();
        addAreaCnd2Page(page, av);
        return manager().findPage(t, page);
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, ModelMap modelMap,
                       Trace t, AppVisitor av) {
        return doView(id, uuid, modelMap, t, av);
    }

    protected String doView(Long id, String uuid, ModelMap modelMap, Trace t, AppVisitor av) {
        modelMap.put("id", id);
        modelMap.put("uuid", uuid);
        return getViewPage();
    }

    /**
     * @param prepare     true:初始化entity放入ModelMap，false:不处理
     * @param parentId
     * @param parentClass
     * @param t
     * @param modelMap
     * @param av
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@RequestParam(value = "prepare", required = false) boolean prepare,
                      @RequestParam(value = "parentId", required = false) Long parentId,
                      @RequestParam(value = "parentClass", required = false) String parentClass, Trace t, ModelMap modelMap,
                      AppVisitor av) {
        return doAdd(prepare, parentId, parentClass, t, modelMap, av);
    }

    protected String doAdd(boolean prepare, Long parentId, String parentClass, Trace t, ModelMap modelMap,
                           AppVisitor av) {
        modelMap.put("parentId", parentId);
        modelMap.put("parentClass", parentClass);
        if (prepare) {
            T entity = mirror.born();
            modelMap.put("entity", entity);
        }
        return getEditPage();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "prepare", required = false) boolean prepare,
                       @RequestParam(value = "parentId", required = false) Long parentId,
                       @RequestParam(value = "parentClass", required = false) String parentClass,
                       @RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t, ModelMap modelMap,
                       AppVisitor av) {
        return doEdit(prepare, parentId, parentClass, id, uuid, t, modelMap, av);
    }

    protected String doEdit(@RequestParam(value = "prepare", required = false) boolean prepare, Long parentId,
                            String parentClass, Long id, String uuid, Trace t, ModelMap modelMap, AppVisitor av) {
        modelMap.put("id", id);
        modelMap.put("uuid", uuid);

        if (prepare) {
            T entity = manager().fetchByIdUuid(t, id, uuid);
            modelMap.put("entity", entity);
        }

        return getEditPage();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    Result<Object> get(@RequestParam(value = "parentId", required = false) Long parentId,
                       @RequestParam(value = "parentClass", required = false) String parentClass,
                       @RequestParam(value = "id", required = false) Long id,
                       @RequestParam(value = "uuid", required = false) String uuid, Trace t, AppVisitor av) {
        T domain = doGet(parentId, parentClass, id, uuid, t, av);
        return new Result<Object>(domain);
    }

    protected T doGet(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
        if (id != null && uuid != null) {
            return manager().fetchByIdUuid(t, id, uuid);
        } else {
            T entity = mirror.born();
            return entity;
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @WebLog(db = true)
    public @ResponseBody
    Result<Object> save(@RequestBody @Valid T entity, Trace t, AppVisitor av) {
        if (!checkReferer(true)) {
            return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
        }
        T domain = doSave(entity, t, av);
        return new Result<Object>(domain);
    }

    protected T doSave(T entity, Trace t, AppVisitor av) {
        return manager().save(t, entity);
    }

    /**
     * 删除
     *
     * @param id
     * @param uuid
     * @return
     */
    @WebLog(db = true, desc = "用户([(${visitor.name})])删除了[(${domainShowName})]([(${id})]), TID:[(${t.tid})]")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Result<Object> delete(@RequestParam(value = "id") Long id,
                          @RequestParam(value = "uuid") String uuid, Trace t, AppVisitor av) {
        if (!checkReferer(true)) {
            return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
        }
        Boolean ret = doDelete(id, uuid, t, av);
        return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "删除对象失败");
    }

    protected Boolean doDelete(Long id, String uuid, Trace t, AppVisitor av) {
        return manager().delete(t, id, uuid);
    }

    /**
     * 批量删除
     *
     * @param uuidIds
     * @return
     */
    @WebLog(db = true, desc = "用户([(${visitor.name})])删除了[(${domainShowName})]([(${uuidIds})]), TID:[(${t.tid})]")
    @RequestMapping(value = "deletes", method = RequestMethod.POST)
    public @ResponseBody
    Result<Object> deletes(@RequestBody Map<String, Long> uuidIds, Trace t, AppVisitor av) {
        if (!checkReferer(true)) {
            return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
        }
        Boolean ret = doDeletes(uuidIds, t, av);
        return new Result<Object>(!ret, Exceptions.CODE_ERROR_DELETE, "删除对象失败");
    }

    protected Boolean doDeletes(Map<String, Long> uuidIds, Trace t, AppVisitor av) {
        return manager().deletes(t, uuidIds);
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public String select(@RequestBody List<StringRuleDescriptor> cnds,
                         @RequestParam(value = "multi", required = false) boolean multi, ModelMap modelMap, AppVisitor av) {
        return doSelect(cnds, multi, modelMap, av);
    }

    protected String doSelect(List<StringRuleDescriptor> cnds, boolean multi, ModelMap modelMap, AppVisitor av) {
        modelMap.put("cnds", cnds);
        modelMap.put("multi", multi);
        return getSelectPage();
    }

    @RequestMapping(value = "/page4Select", method = RequestMethod.GET)
    public @ResponseBody
    Result<Object> page4Select(@RequestBody WebPage webPage, Trace t, AppVisitor av) {
        Page<T> page = doPage4Select(webPage, t, av);
        return new Result<Object>(page);
    }

    protected Page<T> doPage4Select(WebPage webPage, Trace t, AppVisitor av) {
        Page<T> page = webPage.toPage();
        addAreaCnd2Page(page, av);
        return manager().findPage4Select(t, page);
    }

    @WebLog(db = true,
            desc = "用户([(${visitor.name})])更改[(${domainShowName})]([(${id})])的状态为([(${status})]), TID:[(${t.tid})]")
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public @ResponseBody
    Result<Object> changeStatus(@RequestParam(value = "status") int status,
                                @RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t) {
        if (!checkReferer(true)) {
            return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
        }
        Boolean ret = doChangeStatus(t, id, uuid, status);
        return new Result<Object>(!ret, Exceptions.CODE_ERROR_UPDATE, "更新对象状态失败");
    }

    protected Boolean doChangeStatus(Trace t, Long id, String uuid, int status) {
        return manager().updateStatus(t, status, id, uuid);
    }

    @WebLog(db = true, desc = "用户([(${visitor.name})])更改了[(${domainShowName})]([(${uuidIds})])的状态为([(${status})]), TID:[(${t.tid})]")
    @RequestMapping(value = "/changeStatuss", method = RequestMethod.POST)
    public @ResponseBody
    Result<Object> changeStatuss(@RequestParam(value = "status") int status, @RequestBody Map<String, Long> uuidIds, Trace t) {
        if (!checkReferer(true)) {
            return new Result<Object>(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
        }
        Boolean ret = doChangeStatuss(t, uuidIds, status);
        return new Result<Object>(!ret, Exceptions.CODE_ERROR_UPDATE, "更新对象状态失败");
    }

    protected Boolean doChangeStatuss(Trace t, Map<String, Long> uuidIds, int status) {
        return manager().updates(t, status, uuidIds);
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
                page.addRule(new Equal("areaId", Long.parseLong(av.getAreaId())));
            }
        }
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
}
