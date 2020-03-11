package com.linkallcloud.core.www;

import com.linkallcloud.core.lang.Strings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UrlPattern {
    private StringBuffer url;
    private boolean urlHasParams;

    private List<Pattern> patterns;

    public UrlPattern() {
        this("");
    }

    public UrlPattern(String commaDelim) {
        super();
        this.url = new StringBuffer();
        this.urlHasParams = false;
        if (Strings.isBlank(commaDelim)) {
            commaDelim = "/js/*,/css/*,/images/*,/img/*,.jpg,.png,.jpeg,.js,.css,/static/*,/verifyCode,/exit,/logout,/unsupport,/error,/pub/*,/face/*";
        } else {
            this.url.append(commaDelim);
            if (commaDelim.indexOf("?") != -1) {
                this.urlHasParams = true;
            }
        }

        String[] split = commaDelim.split(",");
        this.patterns = new ArrayList<Pattern>(split.length);
        for (int i = 0; i < split.length; i++) {
            String pattern = split[i].trim();
            if (pattern.length() > 0) {
                this.patterns.add(Pattern.compile(pattern));
            }
        }
    }

    public boolean isMatcher(String expr) {
        for (Pattern pattern : this.patterns) {
            if (pattern.matcher(expr).matches())
                return true;
        }
        return false;
    }

    /**
     * @param perameterName
     * @param perameterValue
     * @return url
     * @throws UnsupportedEncodingException
     */
    public UrlPattern append(String perameterName, String perameterValue)
            throws UnsupportedEncodingException {
        if (this.urlHasParams) {
            this.url.append("&");
        } else {
            this.url.append("?");
            this.urlHasParams = true;
        }
        this.url.append(perameterName).append("=").append(URLEncoder.encode(perameterValue, "UTF-8"));
        return this;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public StringBuffer getUrl() {
        return url;
    }

    public String url() {
        return url.toString();
    }
}
