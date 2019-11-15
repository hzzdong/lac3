package com.linkallcloud.web.filter;

import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public abstract class LacCommonFilter implements Filter {
	private static Log log = Logs.get();

    // 不过滤的uri
    protected List<String> notFilterResources = Lang.list("/static/", "/js/", "/css/", "/images/", "/img/", ".jpg", ".png",
            ".jpeg", ".js", ".css", "/imageValidate", "/verifyCode", "/exit", "/nnl/", "/unsupport", "/error", "/pub/");

    public LacCommonFilter() {
        super();
    }

    /**
     * @param ignoreRes
     * @param override  是否覆盖
     */
    public LacCommonFilter(List<String> ignoreRes, boolean override) {
        super();
        if (ignoreRes != null && ignoreRes.size() > 0) {
            if (override) {
                this.notFilterResources = ignoreRes;
            } else {
                for (String res : ignoreRes) {
                    boolean exist = false;
                    for (String uri : notFilterResources) {
                        if (uri.equals(res)) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        this.notFilterResources.add(res);
                    }
                }
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (needFiltered(uri)) {
            doConcreteFilter(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    protected abstract void doConcreteFilter(HttpServletRequest request, HttpServletResponse response,
                                             FilterChain filterChain) throws ServletException, IOException;

    /**
     * 是否需要过滤
     *
     * @param uri
     * @return
     */
    private boolean needFiltered(String uri) {
        for (String s : notFilterResources) {
            if (uri.indexOf(s) != -1) {
                return false;
            }
        }
        return true;
    }

	/**
	 * 取出登录用户
	 *
	 * @param appCode
	 * @param request
	 * @return
	 */
	protected SessionUser getLoginUser(String appCode, HttpServletRequest request) {
		return (SessionUser) Controllers.getSessionUser(appCode, request);
	}
}
