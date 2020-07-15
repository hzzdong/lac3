package com.linkallcloud.web.utils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.util.HibernateValidator;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.web.session.LacToken;
import com.linkallcloud.web.session.SessionUser;

public class Controllers {
	public static String CURRENT_APP_KEY = "_LAC_CURRENT_APP_KEY_";

	public static HttpServletRequest getHttpRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static SessionUser getSessionUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return Controllers.getSessionUser(request);
	}

	public static SessionUser getSessionUser(String appCode) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return Controllers.getSessionUser(appCode, request);
	}

	public static SessionUser getSessionUser(HttpServletRequest request) {
		if (request != null) {
			HttpSession session = request.getSession();
			if (session != null) {
				String appCode = (String) session.getAttribute(Controllers.CURRENT_APP_KEY);
				if (!Strings.isBlank(appCode)) {
					return (SessionUser) session.getAttribute(appCode + ISessionUser.SESSION_USER_KEY);
				}
			}
		}
		return null;
	}

	public static SessionUser getSessionUser(String appCode, HttpServletRequest request) {
		if (request != null && !Strings.isBlank(appCode)) {
			HttpSession session = request.getSession();
			if (session != null) {
				return (SessionUser) session.getAttribute(appCode + ISessionUser.SESSION_USER_KEY);
			}
		}
		return null;
	}

	public static void login(String appCode, ISessionUser su) {
		setSessionObject(Controllers.CURRENT_APP_KEY, appCode);
		setSessionObject(appCode + ISessionUser.SESSION_USER_KEY, su);
	}

	public static void switchLogin2App(String appCode) {
		setSessionObject(Controllers.CURRENT_APP_KEY, appCode);
	}

	public static String getCurrentAppKey() {
		return (String) Controllers.getSessionObject(Controllers.CURRENT_APP_KEY);
	}

	public static Object getSessionObject(String key) {
		HttpSession session = getHttpRequest().getSession();
		if (session != null) {
			return session.getAttribute(key);
		}
		return null;
	}

	public static void setSessionObject(String key, Object value) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (request != null) {
			HttpSession session = request.getSession();
			if (session != null) {
				session.setAttribute(key, value);
			}
		}
	}

	public static AppVisitor getAppVisitor() {
		ISessionUser su = getSessionUser();
		return Controllers.getAppVisitor(su);
	}

	public static AppVisitor getAppVisitor(ISessionUser su) {
		if (su != null) {
			AppVisitor av = new AppVisitor();
			av.setCompanyId(su.getCompany());
			av.setOrgId(su.getOrg());
			av.setAreaId(su.getArea());
			av.setAreaLevel(su.getAreaLevel());
			av.setAppId(su.getApp());
			av.setId(su.getSid());
			av.setLoginName(su.getLoginName());
			av.setType(su.getUserType());
			av.setPhone(su.getPhone());
			av.setAdmin(su.isAdmin());
			av.setIp(getHttpRequest().getRemoteHost());

			// Client client = getClientInfoFromCache(su.getLoginName());
			// if (client != null) {
			// av.setClient(client);
			// }
			return av;
		}
		return null;
	}

//	/**
//	 * 验证token
//	 *
//	 * @param token
//	 * @return
//	 * @throws BaseException
//	 */
//	public static SessionUser checkToken(String token) throws BaseException {
//		if (Strings.isBlank(token)) {
//			throw new BaseException("100002", "token验证失败。");
//		}
//
//		int idx1 = token.indexOf("|");
//		int idx2 = token.lastIndexOf("|");
//		if (idx1 == -1 || idx2 == -1) {
//			throw new BaseException("100002", "token验证失败。");
//		}
//
//		String loginName = token.substring(0, idx1);
//		String encMsg = token.substring(idx1 + 1, idx2);
//		String sign = token.substring(idx2 + 1);
//
//		String src = decrypt(encMsg);
//		if (src != null && Lang.sha1(src + signKey).equals(sign)) {
//			String[] srcArray = src.split(",");
//			if (srcArray != null && srcArray.length == 8) {
//				if (!loginName.equals(srcArray[1])) {
//					throw new BaseException("100003", "token错误。");
//				}
//
//				LocalDateTime end = Date8.stringToLocalDateTime(srcArray[7]);
//				if (end.isAfter(LocalDateTime.now())) {
//					SessionUser su = new SessionUser();
//					su.setInfo(srcArray[0], srcArray[1], srcArray[2], null, Long.parseLong(srcArray[3]),
//							Long.parseLong(srcArray[4]), null, srcArray[5]);
//					return su;
//				} else {
//					throw new BaseException("100001", "token超时。");
//				}
//			}
//		}
//
//		throw new BaseRuntimeException("100002", "token验证失败。");
//	}
//
//	/**
//	 * @param userType
//	 * @param loginName
//	 * @param userName
//	 * @param userId
//	 * @param companyId
//	 * @param companyName
//	 * @param validPeriod 有效时长，单位：分钟。<=0表示长期有效(默认365天)
//	 */
//	public static String createToken(String userType, String loginName, String userName, Long userId, Long companyId,
//			String companyName, int validPeriod) {
//		LocalDateTime now = LocalDateTime.now();
//		if (validPeriod <= 0) {
//			validPeriod = 60 * 24 * 365;
//		}
//		LocalDateTime end = Date8.addTime(now, ChronoUnit.MINUTES, validPeriod);
//		String src = userType + "," + loginName + "," + userName + "," + userId + "," + companyId + "," + companyName
//				+ "," + Date8.formatLocalDateTime(now) + "," + Date8.formatLocalDateTime(end);
//		String sign = Lang.sha1(src + signKey);
//		String msg = loginName + "|" + encrypt(src) + "|" + sign;
//		return msg;
//	}

	/**
	 * 
	 * @param suser
	 * @param validPeriod 有效时长，单位：分钟。<=0表示长期有效(默认7天)
	 * @return
	 */
	public static String createToken(SessionUser suser, int validPeriod) {
		LacToken tokenObj = new LacToken(suser, validPeriod);
		return tokenObj.token(encKey, signKey);
	}

	public static SessionUser checkToken(String token) throws BaseException {
		return LacToken.check(token, encKey, signKey);
	}

	private static String encKey = "6$TuO!XH@hr4lJtA";
	// private static String encOffset = "R$jzDtexk1J7eK!P";
	private static String signKey = "SIGN8sdfhweHDK9sdfjskfdjWndew";

//	private static String encrypt(String src) {
//		try {
//			return Encrypts.encryptAES(src, encKey);
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	private static String decrypt(String src) {
//		try {
//			return Encrypts.decryptAES(src, encKey);
//		} catch (Exception e) {
//			return null;
//		}
//	}

	public static String redirect(String format, Object... arguments) {
		return new StringBuffer("redirect:").append(MessageFormat.format(format, arguments)).toString();
	}

	public static <T> void beanValidator(T object) {
		HibernateValidator.getInstance().validatorThrowException(object);
	}

	public static void putSessionUser2Model(ModelMap modelMap) {
		ISessionUser user = getSessionUser();
		modelMap.put("user", user);
	}

	public static <T extends Domain> Map<Long, T> getIdMap(List<T> entitys) {
		return entitys.stream().collect(Collectors.toMap(T::getId, Function.identity(), (key1, key2) -> key2));
	}

	public static String getRequestScheme(HttpServletRequest request) {
		String scheme = request.getHeader("X-Forwarded-Proto");
		if (Strings.isBlank(scheme)) {
			scheme = request.getHeader("X-Http-Scheme");
		}
		if (Strings.isBlank(scheme)) {
			scheme = request.getScheme();
		}
		return scheme;
	}

	public static String getRequestServerName(HttpServletRequest request) {
		String host = request.getHeader("X-Real-Host");
		if (Strings.isBlank(host)) {
			host = request.getServerName();
		}
		return host;
	}

}
