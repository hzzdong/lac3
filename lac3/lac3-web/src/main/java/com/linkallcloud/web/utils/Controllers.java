package com.linkallcloud.web.utils;

import com.linkallcloud.sh.Encrypts;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.util.Date8;
import com.linkallcloud.core.util.HibernateValidator;
import com.linkallcloud.core.www.ISessionUser;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Controllers {

    public static HttpServletRequest getHttpRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static ISessionUser getSessionUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return Controllers.getSessionUser(request);
    }

    public static ISessionUser getSessionUser(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                return (ISessionUser) session.getAttribute(ISessionUser.SESSION_USER_KEY);
            }
        }
        return null;
    }

    public static void login(ISessionUser su) {
        setSessionObject(ISessionUser.SESSION_USER_KEY, su);
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
            AppVisitor av = new AppVisitor(su.getCompanyId(), su.getCompanyName(), su.getOrgId(), su.getOrgName(),
                    su.getOrgType(), su.getId(), su.getName(), su.getLoginName(), su.getUserType(),
                    getHttpRequest().getRemoteHost(), 0);
            av.setUuid(su.getUuid());
            av.setPhone(su.getPhone());

            av.setAreaId(su.getAreaId());
            av.setAreaLevel(su.getAreaLevel());
            av.setAreaName(su.getAreaName());

            av.setAppId(su.getAppId());
            av.setAppUuid(su.getAppUuid());
            av.setAppName(su.getAppName());

            av.setAdmin(su.isAdmin());

            // Client client = getClientInfoFromCache(su.getLoginName());
            // if (client != null) {
            // av.setClient(client);
            // }
            return av;
        }
        return null;
    }

    /**
     * 验证token
     *
     * @param token
     * @return
     * @throws BaseException
     */
    public static SessionUser checkToken(String token) throws BaseException {
        if (Strings.isBlank(token)) {
            throw new BaseException("100002", "token验证失败。");
        }

        int idx1 = token.indexOf("|");
        int idx2 = token.lastIndexOf("|");
        if (idx1 == -1 || idx2 == -1) {
            throw new BaseException("100002", "token验证失败。");
        }

        String loginName = token.substring(0, idx1);
        String encMsg = token.substring(idx1 + 1, idx2);
        String sign = token.substring(idx2 + 1);

        String src = decrypt(encMsg);
        if (src != null && Lang.sha1(src + signKey).equals(sign)) {
            String[] srcArray = src.split(",");
            if (srcArray != null && srcArray.length == 8) {
                if (!loginName.equals(srcArray[1])) {
                    throw new BaseException("100003", "token错误。");
                }

                LocalDateTime end = Date8.stringToLocalDateTime(srcArray[7]);
                if (end.isAfter(LocalDateTime.now())) {
                    SessionUser su = new SessionUser();
                    su.setInfo(srcArray[0], srcArray[1], srcArray[2], srcArray[3], srcArray[4], srcArray[5]);
                    return su;
                } else {
                    throw new BaseException("100001", "token超时。");
                }
            }
        }

        throw new BaseRuntimeException("100002", "token验证失败。");
    }

    /**
     * @param userType
     * @param loginName
     * @param userName
     * @param userId
     * @param companyId
     * @param companyName
     * @param validPeriod 有效时长，单位：分钟。<=0表示长期有效(默认365天)
     */
    public static String createToken(String userType, String loginName, String userName, Long userId, Long companyId,
                                     String companyName, int validPeriod) {
        LocalDateTime now = LocalDateTime.now();
        if (validPeriod <= 0) {
            validPeriod = 60 * 24 * 365;
        }
        LocalDateTime end = Date8.addTime(now, ChronoUnit.MINUTES, validPeriod);
        String src = userType + "," + loginName + "," + userName + "," + userId + "," + companyId + "," + companyName
                + "," + Date8.formatLocalDateTime(now) + "," + Date8.formatLocalDateTime(end);
        String sign = Lang.sha1(src + signKey);
        String msg = loginName + "|" + encrypt(src) + "|" + sign;
        return msg;
    }

    private static String encKey = "6$TuO!XH@hr4lJtA";
    // private static String encOffset = "R$jzDtexk1J7eK!P";
    private static String signKey = "SIGN8sdfhweHDK9sdfjskfdjWndew";

    private static String encrypt(String src) {
        try {
            return Encrypts.encryptAES(src, encKey);
        } catch (Exception e) {
            return null;
        }
    }

    private static String decrypt(String src) {
        try {
            return Encrypts.decryptAES(src, encKey);
        } catch (Exception e) {
            return null;
        }
    }

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

    public static <PK extends Serializable, T extends Domain<PK>> Map<PK, T> getIdMap(List<T> entitys) {
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
