package com.linkallcloud.web.session;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.util.Date8;
import com.linkallcloud.sh.Encrypts;

public class LacToken extends SessionUser {
	private static final long serialVersionUID = -496108462462094172L;

	private String vtime;// 有效期截止时间

	public LacToken() {
		super();
	}

	public LacToken(SessionUser user) {
		super();
		if (user != null) {
			try {
				BeanUtils.copyProperties(user, this);
			} catch (Throwable e) {
				// log.warn("Fail to copy properties!", e);
			}
		}
	}

	/**
	 * 
	 * @param user
	 * @param validPeriod 有效时长，单位：分钟。<=0表示长期有效(默认7天)
	 */
	public LacToken(SessionUser user, int validPeriod) {
		this(user);
		LocalDateTime now = LocalDateTime.now();
		if (validPeriod <= 0) {
			validPeriod = 60 * 24 * 7;
		}
		LocalDateTime end = Date8.addTime(now, ChronoUnit.MINUTES, validPeriod);
		this.vtime = Date8.formatLocalDateTime(end);
	}

	public LacToken(Long id, String uuid, String loginName, String name, String userType, Long companyId,
			String companyUuid, String companyName, Long orgId, String orgUuid, String orgName) {
		super(id, uuid, loginName, name, userType, companyId, companyUuid, companyName, orgId, orgUuid, orgName);
	}

	public LacToken(Long id, String uuid, String loginName, String name, String userType) {
		super(id, uuid, loginName, name, userType);
	}

	public String getVtime() {
		return vtime;
	}

	public void setVtime(String vtime) {
		this.vtime = vtime;
	}

	public boolean timeout() {
		LocalDateTime end = Date8.stringToLocalDateTime(this.getVtime());
		if (end.isAfter(LocalDateTime.now())) {
			return false;
		} else {
			return true;
		}
	}

	public SessionUser toSessionUser() {
		SessionUser user = new SessionUser();
		try {
			BeanUtils.copyProperties(this, user);
		} catch (Throwable e) {
			// log.warn("Fail to copy properties!", e);
		}
		return user;
	}

	public String encryptToken(String encKey) {
		String src = JSON.toJSONString(this);
		try {
			return Encrypts.encryptAES(src, encKey);
		} catch (Exception e) {
			return null;
		}
	}

	public String sign(String signKey) {
		String src = JSON.toJSONString(this);
		return Lang.sha1(src + signKey);
	}

	public String token(String encKey, String signKey) {
		String msg = this.getLoginName() + "|" + encryptToken(encKey) + "|" + sign(signKey);
		return msg;
	}

	public static String decrypt(String token, String encKey) {
		try {
			return Encrypts.decryptAES(token, encKey);
		} catch (Exception e) {
		}
		return null;
	}

	public static SessionUser check(String token, String encKey, String signKey) throws BizException {
		if (Strings.isBlank(token)) {
			throw new BizException("100002", "token验证失败。");
		}

		int idx1 = token.indexOf("|");
		int idx2 = token.lastIndexOf("|");
		if (idx1 == -1 || idx2 == -1) {
			throw new BizException("100002", "token验证失败。");
		}

		String loginName = token.substring(0, idx1);
		String encMsg = token.substring(idx1 + 1, idx2);
		String sign = token.substring(idx2 + 1);

		try {
			String src = Encrypts.decryptAES(encMsg, encKey);
			String thisSign = Lang.sha1(src + signKey);
			if (thisSign.equals(sign)) {
				LacToken tokenObj = JSON.parseObject(src, LacToken.class);
				if (tokenObj.timeout()) {
					throw new BizException("100002", "token超时。");
				}
				if (tokenObj.getLoginName().equals(loginName)) {
					return tokenObj.toSessionUser();
				}
			}
		} catch (Exception e) {
		}
		throw new BizException("100002", "token验证失败。");
	}

}
