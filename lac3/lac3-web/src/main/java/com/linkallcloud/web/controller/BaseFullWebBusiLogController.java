package com.linkallcloud.web.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.laclog.WebBusiLog;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.manager.IWebBusiLogManager;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.pagination.WebPage;

public abstract class BaseFullWebBusiLogController<T extends WebBusiLog<Long>, S extends IWebBusiLogManager<Long, T>> {

    private Log log = Logs.get();

    protected Mirror<T> mirror;

    @SuppressWarnings("unchecked")
    public BaseFullWebBusiLogController() {
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
        return "page/log/main";
    }

    /**
     * 编辑页面
     * 
     * @return
     */
    protected String getWiewPage() {
        return "page/log/view";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String list() {
        return getMainPage();
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public @ResponseBody Result<Object> page(@RequestBody WebPage webPage, Trace t, AppVisitor av) {
        Page<Long, T> page = doFindPage(webPage, t, av);
        return new Result<Object>(page);
    }

    protected Page<Long, T> doFindPage(WebPage webPage, Trace t, AppVisitor av) {
        return manager().findPage(t, webPage.toPage());
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t,
            ModelMap modelMap) {
        T entity = doGet(id, uuid, t);
        modelMap.put("entity", entity);
        return getWiewPage();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody Result<Object> get(@RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "uuid", required = false) String uuid, Trace t) {
        T domain = doGet(id, uuid, t);
        return new Result<Object>(domain);
    }

    protected T doGet(Long id, String uuid, Trace t) {
        T entity = manager().fetchByIdUuid(t, id, uuid);
        return entity;
    }

}
