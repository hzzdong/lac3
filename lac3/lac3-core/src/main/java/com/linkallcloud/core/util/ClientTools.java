package com.linkallcloud.core.util;

import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Client;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.ISessionUser;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

public class ClientTools {

	/**
	 * 设置客户端信息到缓存中
	 * 
	 * @param cacheManager
	 * @param clientCacheName
	 * @param loginName
	 * @param client
	 */
	public static void setClientInfo2Cache(CacheManager cacheManager, String clientCacheName, String loginName,
			Client client) {
		if (cacheManager != null) {
			Cache cache = cacheManager.getCache(clientCacheName);
			if (cache != null) {
				cache.put(loginName, client);
			}
		}
	}

	/**
	 * 从缓存中取出客户端信息
	 * 
	 * @param cacheManager
	 * @param clientCacheName
	 * @param loginName
	 * @return
	 */
	public static Client getClientInfoFromCache(CacheManager cacheManager, String clientCacheName, String loginName) {
		if (cacheManager != null) {
			Cache cache = cacheManager.getCache(clientCacheName);
			if (cache != null) {
				ValueWrapper vr = cache.get(loginName);
				if (vr != null) {
					Client client = (Client) vr.get();
					return client;
				}
			}
		}
		return null;
	}

	/**
	 * 从缓存中获取登录用户
	 * 
	 * @param cacheManager
	 * @param sessionUserCacheName
	 * @param sessionId
	 * @return
	 */
	public static ISessionUser getSessionUser(CacheManager cacheManager, String sessionUserCacheName,
                                              String sessionId) {
		ISessionUser su = getSessionUserFromCache(cacheManager, sessionUserCacheName, sessionId);
		return su;
	}

	/**
	 * 登录用户至缓存会话中
	 * 
	 * @param cacheManager
	 * @param sessionUserCacheName
	 * @param suser
	 * @return
	 */
	public static String loginUserFaceModeSession(CacheManager cacheManager, String sessionUserCacheName,
			ISessionUser suser) {
		String sessionId = Utils.getRandomID(32);

		if (suser != null && !Strings.isBlank(suser.getLoginName())) {
			String[] accountInfo = suser.getLoginName().split("@");
			if (accountInfo != null && accountInfo.length > 1) {
				sessionId = accountInfo[0] + "@" + sessionId;
			}
		}

		setSessionUser2Cache(cacheManager, sessionUserCacheName, sessionId, suser);
		return sessionId;
	}

	/**
	 * 从缓存中登出用户
	 * 
	 * @param cacheManager
	 * @param sessionUserCacheName
	 * @param sessionId
	 */
	public static void logoutUserFaceModeSession(CacheManager cacheManager, String sessionUserCacheName,
			String sessionId) {
		if (cacheManager != null) {
			Cache cache = cacheManager.getCache(sessionUserCacheName);
			if (cache != null) {
				cache.evict(sessionId);
			}
		}
	}

	/**
	 * 保存用户信息到缓存中
	 * 
	 * @param cacheManager
	 * @param sessionUserCacheName
	 * @param sessionId
	 * @param suser
	 */
	public static void setSessionUser2Cache(CacheManager cacheManager, String sessionUserCacheName, String sessionId,
			ISessionUser suser) {
		if (cacheManager != null) {
			Cache cache = cacheManager.getCache(sessionUserCacheName);
			if (cache != null) {
				cache.put(sessionId, suser);
			}
		}
	}

	/**
	 * 从缓存中取出用户信息
	 * 
	 * @param cacheManager
	 * @param sessionUserCacheName
	 * @param sessionId
	 * @return
	 */
	public static ISessionUser getSessionUserFromCache(CacheManager cacheManager, String sessionUserCacheName,
			String sessionId) {
		if (cacheManager != null) {
			Cache cache = cacheManager.getCache(sessionUserCacheName);
			if (cache != null) {
				ValueWrapper vr = cache.get(sessionId);
				if (vr != null) {
					ISessionUser su = (ISessionUser) vr.get();
					return su;
				}
			}
		}
		return null;
	}

	/**
	 * FACE模式
	 * 
	 * @return
	 */
	public static AppVisitor getAppVisitor(CacheManager cacheManager, String sessionUserCacheName, String sessionId) {
		ISessionUser su = getSessionUser(cacheManager, sessionUserCacheName, sessionId);
		if (su != null) {
			AppVisitor av = new AppVisitor(su.getCompanyId(), su.getCompanyName(), su.getOrgId(), su.getOrgName(),
					su.getOrgType(), su.getId(), su.getName(), su.getLoginName(), su.getUserType(), null, null);
			Client client = getClientInfoFromCache(cacheManager, sessionUserCacheName, su.getLoginName());
			if (client != null) {
				av.setClient(client);
			}
			return av;
		}
		return null;
	}

}
