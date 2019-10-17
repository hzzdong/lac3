package com.linkallcloud.core.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.linkallcloud.core.lang.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResBundle {
    private static Log log = LogFactory.getLog(ResBundle.class);

    private static Map<String, ResourceBundle> resMap = new HashMap<String, ResourceBundle>();

    private static final ResourceBundle NULL_BUNDLE = new ResourceBundle() {
        public Enumeration<String> getKeys() {
            return null;
        }

        protected Object handleGetObject(String key) {
            return null;
        }

        public String toString() {
            return "NULL_BUNDLE";
        }
    };

    private static ResourceBundle getBundle(String resourceName) {
        ResourceBundle rb = resMap.get(resourceName);
        if (rb == null) {
            try {
                rb = ResourceBundle.getBundle(resourceName, Locale.getDefault());
                if (rb == null) {
                    rb = NULL_BUNDLE;
                }
                resMap.put(resourceName, rb);
            } catch (Throwable mre) {
                log.error("No resource property file in the classpath or in the res folder.", mre);
            }
        }
        return rb;
    }

    public static String getString(String key) {
        return getResString("resource", key, null);
    }

    public static String getMessage(String key) {
        return getResString("resource", key, null);
    }

    public static String getString(String key, String defaultValue) {
        return getResString("resource", key, defaultValue);
    }

    public static String getMessage(String key, String defaultValue) {
        return getResString("resource", key, defaultValue);
    }

    public static String getResString(String resourceName, String key) {
        return getResString(resourceName, key, null);
    }

    public static String getResString(String resourceName, String key, String defaultValue) {
        try {
            String value = getBundle(resourceName).getString(key);
            return Strings.isBlank(value) ? defaultValue : value.trim();
        } catch (MissingResourceException e) {
            return Strings.isBlank(defaultValue) ? key : defaultValue;
        } catch (Throwable e) {
            return Strings.isBlank(defaultValue) ? key : defaultValue;
        }
    }

}
