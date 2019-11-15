package com.linkallcloud.core.www;

import com.linkallcloud.core.lang.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UrlPattern {
    private List<Pattern> patterns;

    public UrlPattern() {
        this("");
    }

    public UrlPattern(String commaDelim) {
        super();
        if (Strings.isBlank(commaDelim)) {
            commaDelim = "/js/*,/css/*,/images/*,/img/*,.jpg,.png,.jpeg,.js,.css,/static/*,/verifyCode,/exit,/logout,/unsupport,/error,/pub/*,/face/*";
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

    public List<Pattern> getPatterns() {
        return patterns;
    }
}
