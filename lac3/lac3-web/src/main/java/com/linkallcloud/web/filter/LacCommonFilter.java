package com.linkallcloud.web.filter;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Principal;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class LacCommonFilter implements Filter {
    protected static Log log = Logs.get();

    // 不过滤的uri
    protected List<String> notFilterResources = Lang.list("/static/", "/js/", "/css/", "/images/", "/img/", ".jpg", ".png",
            ".jpeg", ".js", ".css", "/imageValidate", "/verifyCode", "/exit", "/nnl/", "/unsupport", "/error", "/pub/");

    // 需要过滤的uri
    protected List<String> filterResources = null;

    public LacCommonFilter() {
        super();
    }

    /**
     * @param ignoreRes
     * @param override  是否覆盖
     */
    public LacCommonFilter(List<String> ignoreRes, boolean override) {
        this(ignoreRes, false, override);
    }

    /**
     * @param reses       filterRes 和ignoreRes二选一，只能设置一个
     * @param isFilterRes 是否filterRes
     * @param override    是否覆盖
     */
    public LacCommonFilter(List<String> reses, boolean isFilterRes, boolean override) {
        if (isFilterRes) {
            this.initFilterResources(reses, override);
        } else {
            this.initNotFilterResources(reses, override);
        }
    }

    private void initFilterResources(List<String> reses, boolean override) {
        if (override) {
            this.filterResources = reses;
        } else if (reses != null && reses.size() > 0) {
            if (this.filterResources != null && this.filterResources.size() > 0) {
                for (String res : reses) {
                    boolean exist = false;
                    for (String uri : filterResources) {
                        if (uri.equals(res)) {
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        this.filterResources.add(res);
                    }
                }
            } else {
                this.filterResources = new ArrayList<String>();
                for (String res : reses) {
                    this.filterResources.add(res);
                }
            }
        }
    }

    private void initNotFilterResources(List<String> reses, boolean override) {
        if (override) {
            this.notFilterResources = reses;
        } else if (reses != null && reses.size() > 0) {
            if (this.notFilterResources != null && this.notFilterResources.size() > 0) {
                for (String res : reses) {
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
            } else {
                this.notFilterResources = new ArrayList<String>();
                for (String res : reses) {
                    this.notFilterResources.add(res);
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
        if (this.filterResources != null && this.filterResources.size() > 0) {
            for (String s : this.filterResources) {
                if (uri.indexOf(s) != -1) {
                    return true;
                }
            }
            return false;
        } else {
            if (notFilterResources != null && notFilterResources.size() > 0) {
                for (String s : notFilterResources) {
                    if (uri.indexOf(s) != -1) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * 得到SSO认证通过后设置的Assertion。
     *
     * @param request
     * @return Assertion
     */
    protected Assertion getAssertion(String appCode, HttpServletRequest request) {
        return (Assertion) Controllers.getSessionObject(appCode + Assertion.ASSERTION_KEY);
    }

    /**
     * 得到SSO认证通过后设置的Principal。
     *
     * @param request
     * @return Principal
     */
    protected Principal getSSOPrincipal(String appCode, HttpServletRequest request) {
        Assertion as = getAssertion(appCode, request);
        if (as != null) {
            return as.getPrincipal();
        }
        return null;
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

    /**
     * @param loginUrl
     * @param request
     * @param hResponse
     * @throws IOException
     */
    protected void toLogin(String loginUrl, HttpServletRequest request, HttpServletResponse hResponse) throws IOException {
        if (!Strings.isBlank(loginUrl) && !loginUrl.startsWith("http")
                && !loginUrl.startsWith(request.getContextPath())) {
            loginUrl = request.getContextPath() + loginUrl;
        }
        if (WebUtils.isAjax(request)) {
            hResponse.setCharacterEncoding("UTF-8");
            Result<Object> result = Exceptions.makeErrorResult(Exceptions.CODE_ERROR_SESSION_TIMEOUT, "会话超时");
            result.setData(loginUrl);
            // response.sendError(HttpStatus.UNAUTHORIZED.value(), "您已经太长时间没有操作,请刷新页面");
            WebUtils.out(hResponse, result);
        } else {
            hResponse.sendRedirect(loginUrl);
        }
    }
}
