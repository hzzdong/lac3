package com.linkallcloud.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.linkallcloud.core.lang.Lang;

public abstract class LacCommonFilter extends OncePerRequestFilter {

	// 不过滤的uri
	protected List<String> notFilter = Lang.list("/layuicms2.0/", "/js/", "/css/", "/images/", "/img/", ".jpg", ".png",
			".jpeg", ".js", ".css", "/static/", "/login", "/verifyCode", "/exit", "/unsupport", "/error", "/pub/");

	public LacCommonFilter() {
		super();
	}

	/**
	 * 
	 * @param ignoreRes
	 * @param override
	 *            是否覆盖
	 */
	public LacCommonFilter(List<String> ignoreRes, boolean override) {
		super();
		if (ignoreRes != null && ignoreRes.size() > 0) {
			if (override) {
				this.notFilter = ignoreRes;
			} else {
				for (String res : ignoreRes) {
					boolean exist = false;
					for (String uri : notFilter) {
						if (uri.equals(res)) {
							exist = true;
							break;
						}
					}
					if (!exist) {
						this.notFilter.add(res);
					}
				}
			}
		}
	}

	@Override
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
		for (String s : notFilter) {
			if (uri.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}
}
