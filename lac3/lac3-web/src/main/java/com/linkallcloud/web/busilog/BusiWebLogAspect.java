package com.linkallcloud.web.busilog;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.linkallcloud.core.busilog.LacLogAspect;
import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.busilog.enums.LogMode;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.laclog.BusiLog;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.utils.WebUtils;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

public abstract class BusiWebLogAspect<T extends BusiLog> extends LacLogAspect<T> {

	@Override
	protected void subclassDealLogs(T operatelog, JoinPoint joinPoint, Class<?> clzz, Method method, LacLog logAnnot) {

		operatelog.setLogMode(LogMode.WEB.getCode());
		if (clzz.getAnnotation(Controller.class) != null) {
			SessionUser su = Controllers.getSessionUser();
			AppVisitor visitor = Controllers.getAppVisitor();

			/* busi */
			if (logAnnot != null) {
				operatelog.setOperateDesc(
						dealStringTtemplate(true, logAnnot.desc(), joinPoint, method, new HashMap<String, Object>() {
							private static final long serialVersionUID = 1L;
							{
								put("tid", operatelog.getTid());
								put("su", su);
								put("user", su);
								put("visitor", visitor);
								put("saveTag", operatelog.getSaveTag());
							}
						}));
			}
			
			if(Strings.isBlank(operatelog.getOperateDesc())) {
				operatelog.setOperateDesc("日志信息");
			}

			operatelog.setOperator(su);
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			operatelog.setIp(WebUtils.getIpAddress(request));
			operatelog.setUrl(request.getRequestURI());
		}
	}

}
