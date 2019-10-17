package com.linkallcloud.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.core.www.utils.WebUtils;

public class LoginFilter extends LacCommonFilter {

	public LoginFilter() {
		super();
	}

	public LoginFilter(List<String> ignoreRes, boolean override) {
		super(ignoreRes, override);
	}

	@Override
	protected void doConcreteFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 从session中获取登录者实体
		Object obj = request.getSession().getAttribute(ISessionUser.SESSION_USER_KEY);
		if (null == obj) {
		    String loginUrl = request.getContextPath() + getLoginUrl();
			boolean isAjaxRequest = WebUtils.isAjax(request);
			if (isAjaxRequest) {
				response.setCharacterEncoding("UTF-8");
				// response.sendError(HttpStatus.UNAUTHORIZED.value(), "您已经太长时间没有操作,请刷新页面");
				Result<Object> result = Exceptions.makeErrorResult(Exceptions.CODE_ERROR_SESSION_TIMEOUT, "回话超时");
				result.setData(loginUrl);
				WebUtils.out(response, result);
				return;
			}
			response.sendRedirect(loginUrl);
			return;
		} else {
			// 如果session中存在登录者实体，则继续
			SessionUser su = null;
			if (obj instanceof SessionUser) {
				su = (SessionUser) obj;
			}
			doCheckPermission(su, request, response, filterChain);
		}
	}

	/**
	 * 检查权限等其它业务处理
	 * 
	 * @param suser
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doCheckPermission(SessionUser suser, HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		filterChain.doFilter(request, response);
	}

	private String getLoginUrl() {
		return "/login";
	}

}
