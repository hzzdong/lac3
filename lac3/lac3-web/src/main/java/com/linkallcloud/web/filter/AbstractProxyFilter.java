package com.linkallcloud.web.filter;

import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.principal.Assertion;
import com.linkallcloud.core.principal.Service;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.sso.ProxyRetriever;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public abstract class AbstractProxyFilter extends LacCommonFilter {

    private static List<String> frs = Lang.list("/doProxy/", "/proxyDo/");

    private ProxyRetriever proxyRetriever;

    public AbstractProxyFilter(ProxyRetriever proxyRetriever) {
        this(proxyRetriever, frs, true);
    }

    public AbstractProxyFilter(ProxyRetriever proxyRetriever, List<String> filterRes) {
        this(proxyRetriever, filterRes, true);
    }

    public AbstractProxyFilter(ProxyRetriever proxyRetriever, List<String> filterRes, boolean override) {
        super(filterRes, true, override);
        this.proxyRetriever = proxyRetriever;
    }

    protected abstract String getAppCode();

    protected abstract Service getTargetService();

    @Override
    protected void doConcreteFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SessionUser u = getLoginUser(getAppCode(), request);
        if (null != u) {
            Assertion assertion = getAssertion(getAppCode(), request);
            if (assertion != null) {
                String pgt = assertion.getProxyGrantingTicketId();
                if(!Strings.isBlank(pgt)){
                    String pt = proxyRetriever.getProxyTicketIdFor(pgt, getTargetService());
                    if (!Strings.isBlank(pt)) {
                        request.setAttribute("pt", pt);
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}
