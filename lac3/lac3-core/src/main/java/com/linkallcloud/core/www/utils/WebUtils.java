package com.linkallcloud.core.www.utils;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.util.IConstants;

public class WebUtils {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final String CONTENTTYPE_DEFAULT = "application/x-www-form-urlencoded";
    public static final String CONTENTTYPE_JSON = "application/json";
    public static final String CONTENTTYPE_JSON_RPC = "application/json-rpc";
    public static final String X_Requested_With = "X-Requested-With";
    public static final String X_Requested_With_AJAX = "XMLHttpRequest";
    public static final String USER_AGENT = "User-Agent";

    public static final String WECHART = "MicroMessenger";
    public static final String ALIPAY = "AlipayClient";
    public static final String QQ = "QQ";
    public static final String YD_10086_APP = "10086APP";

    public static final String UNKNOWN = "unknown";

    /**
     * 判断是否是Ajax请求
     * 
     * @param req
     * @return boolean
     */
    public static boolean isAjax(HttpServletRequest req) {
        return X_Requested_With_AJAX.equals(req.getHeader(X_Requested_With));
    }

    /**
     * 判断contentType是否json
     * 
     * @param contentType
     * @return boolean
     */
    public static boolean isJson(String contentType) {
        return CONTENTTYPE_JSON.equalsIgnoreCase(contentType);
    }

    /**
     * 判断contentType是否jsonRpc
     * 
     * @param contentType
     * @return boolean
     */
    public static boolean isJsonRpc(String contentType) {
        return CONTENTTYPE_JSON_RPC.equalsIgnoreCase(contentType);
    }

    /**
     * 判断是否json返回。 spring ajax 返回含有 ResponseBody 或者 RestController注解
     * 
     * @param handlerMethod
     * @param request
     * @return 是否ajax请求
     */
    public static boolean isJsonResponse(HttpServletRequest request, HandlerMethod handlerMethod) {
        if (handlerMethod != null) {
            ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
            if (null != responseBody) {
                return true;
            }

            RestController restAnnotation = handlerMethod.getBean().getClass().getAnnotation(RestController.class);
            if (null != restAnnotation) {
                return true;
            }
        }
        // return isJsonResponse(request);
        return false;
    }

    /**
     * 得到contentType
     * 
     * @param req
     * @return boolean
     */
    public static String getContentType(HttpServletRequest req) {
        String contentType = req.getHeader("content-type");
        if (contentType != null) {
            int iSemicolonIdx;
            if ((iSemicolonIdx = contentType.indexOf(";")) != -1)
                contentType = contentType.substring(0, iSemicolonIdx);
        }
        return contentType;
    }

    /**
     * 设置contentType
     * 
     * @param resp
     * @param contentType
     */
    public static void setContentType(HttpServletResponse resp, String contentType) {
        resp.setContentType(contentType + ";charset=" + DEFAULT_ENCODING);
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 参考文章：
     * http://developer.51cto.com/art/201111/305181.htm
     * 
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
     * 
     * 用户真实IP为： 192.168.1.110
     * 
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    public static boolean isWechart(HttpServletRequest request) {
        String heads = request.getHeader(USER_AGENT);
        if (Strings.isBlank(heads)) {
            return false;
        }
        return heads.indexOf(WECHART) != -1 || !Strings.isBlank(request.getParameter(WECHART))
                || !Strings.isBlank((String) request.getAttribute(WECHART));
    }

    public static void setWechartTag(HttpServletRequest request) {
        request.setAttribute(WECHART, "1");
    }

    public static boolean isQQ(HttpServletRequest request) {
        String heads = request.getHeader(USER_AGENT);
        if (Strings.isBlank(heads)) {
            return false;
        }
        return heads.indexOf(QQ) != -1 || !Strings.isBlank(request.getParameter(QQ))
                || !Strings.isBlank((String) request.getAttribute(QQ));
    }

    public static void setQQTag(HttpServletRequest request) {
        request.setAttribute(QQ, "1");
    }

    public static boolean isApliPay(HttpServletRequest request) {
        String heads = request.getHeader(USER_AGENT);
        if (Strings.isBlank(heads)) {
            return false;
        }
        return heads.indexOf(ALIPAY) != -1 || !Strings.isBlank(request.getParameter(ALIPAY))
                || !Strings.isBlank((String) request.getAttribute(ALIPAY));
    }

    public static void setAlipayTag(HttpServletRequest request) {
        request.setAttribute(ALIPAY, "1");
    }

    public static boolean isYd10086App(HttpServletRequest request) {
        String heads = request.getHeader(USER_AGENT);
        if (Strings.isBlank(heads)) {
            return false;
        }
        return heads.indexOf(YD_10086_APP) != -1 || !Strings.isBlank(request.getParameter(YD_10086_APP))
                || !Strings.isBlank((String) request.getAttribute(YD_10086_APP));
    }

    public static void setYd10086AppTag(HttpServletRequest request) {
        request.setAttribute(YD_10086_APP, "1");
    }

    public static void dealQDTag(HttpServletRequest request) {
        if (isWechart(request)) {
            setWechartTag(request);
        } else if (isApliPay(request)) {
            setAlipayTag(request);
        } else if (isQQ(request)) {
            setQQTag(request);
        } else if (isYd10086App(request)) {
            setYd10086AppTag(request);
        }
    }

    /**
     * parse the server domain or ip address from http url
     * 
     * @param url
     * @return Server
     */
    public static String parseServerFromUrl(String url) {
        if (Strings.isBlank(url) || !url.startsWith(IConstants.HTTP)) {
            return null;
        } else {
            String tmp = url.startsWith(IConstants.HTTPS) ? url.substring(IConstants.HTTPS_REQUEST_FREFIX.length())
                    : url.substring(IConstants.HTTP_REQUEST_FREFIX.length());
            int idx = tmp.indexOf(IConstants.COLON);
            if (idx > 0) {
                return tmp.substring(0, idx);
            } else {
                int idx2 = tmp.indexOf(IConstants.LEFT_SLASH);
                return idx2 > 0 ? tmp.substring(0, idx2) : tmp;
            }
        }
    }

    /**
     * 
     * @param url
     * @param perameterName
     * @param perameterValue
     * @return url
     * @throws UnsupportedEncodingException
     */
    public static String urlAppend(String url, String perameterName, String perameterValue)
            throws UnsupportedEncodingException {
        if (url.indexOf("?") == -1) {
            url += "?";
        } else {
            url += "&";
        }
        return url + perameterName + "=" + URLEncoder.encode(perameterValue, "UTF-8");
    }

    /**
     * 得到把参数拼装成url的字符串
     * 
     * @param request
     * @return uri
     */
    public static String getRequestUriWithParameters(HttpServletRequest request) {
        Map<?, ?> map = request.getParameterMap();
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        if (map != null && !map.isEmpty()) {
            for (Object param : map.keySet()) {
                String pn = (String) param;
                Object value = getParameterValue(map.get(pn));
                sb.append(pn).append("=").append("password".equals(pn) ? "******" : value).append("&");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return request.getRequestURI() + sb.toString();
    }

    private static String getParameterValue(Object obj) {
        if (null == obj) {
            return "";
        } else {
            if (obj instanceof String) {
                return (String) obj;
            } else if (obj instanceof String[]) {
                return (String) ((String[]) obj)[0];
            } else {
                return obj.toString();
            }
        }
    }

    /**
     * 取得带相同前缀的Request Parameters.
     * 
     * 返回的结果Parameter名已去除前缀.
     */
    public static Map<String, Object> getParametersStartingWith(HttpServletRequest request, String prefix) {
        if (request == null) {
            return null;
        }
        Enumeration<?> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        String paramPrefix = prefix == null ? "" : prefix;
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(paramPrefix) || paramName.startsWith(paramPrefix)) {
                String unprefixed = paramName.substring(paramPrefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {// NOSONAR
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    public static void out(ServletResponse response, Object result) {
        PrintWriter out = null;
        // ServletOutputStream os = null;
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");// 设置编码
            response.setContentType("application/json");// 设置返回类型

            // os = response.getOutputStream();
            // os.println(JSONObject.toJSONString(resultMap));
            out = response.getWriter();
            out.println(JSON.toJSONString(result));// 输出
        } catch (Exception e) {
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
            // if (null != os) {
            // try {
            // os.flush();
            // os.close();
            // } catch (IOException e) {
            // }
            // }
        }
    }

    // public static Map<String, Object> makeErrorResult(Throwable t) {
    // Map<String, Object> result = new HashMap<String, Object>();
    // result.put("error", true);
    //
    // if (t == null) {
    // result.put("code", "10000001");
    // result.put("message", "未知错误，请联系管理员。");
    // } else {
    // if (t.getClass().equals(BaseException.class)) {
    // BaseException be = (BaseException) t;
    // result.put("code", String.valueOf(be.getCode()));
    // result.put("message", be.getMessage());
    // } else if (t.getClass().equals(BaseRuntimeException.class)) {
    // BaseRuntimeException be = (BaseRuntimeException) t;
    // result.put("code", String.valueOf(be.getCode()));
    // result.put("message", be.getMessage());
    // } else {
    // result.put("code", "10000001");
    // result.put("message", t.getMessage());
    // }
    // }
    // return result;
    // }
    //
    // public static Map<String, Object> makeErrorResult(String code, String message) {
    // Map<String, Object> result = new HashMap<String, Object>();
    // result.put("error", true);
    // result.put("code", code);
    // result.put("message", message);
    // return result;
    // }

    public static Map<String, Object> makeSuccessResult(String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("error", false);
        result.put("code", "0");
        result.put("message", message);
        return result;
    }

    public static Map<String, Object> makeSuccessResult(String fieldName, String fieldValue) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("error", false);
        result.put("code", "0");
        result.put(fieldName, fieldValue);
        return result;
    }

    public static Map<String, Object> makeSuccessResult(Map<String, Object> mvs) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("error", false);
        result.put("code", "0");

        if (mvs != null && !mvs.isEmpty()) {
            Iterator<String> itr = mvs.keySet().iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                result.put(key, mvs.get(key));
            }
        }
        return result;
    }

    /**
     * 
     * @param request
     * @return
     */
    public static String getRequestUrl(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String url = null;
        if (Strings.isBlank(ctx) || ctx.equals("/")) {
            url = uri;
        } else {
            url = uri.substring(ctx.length());
        }
        return url;
    }

}
