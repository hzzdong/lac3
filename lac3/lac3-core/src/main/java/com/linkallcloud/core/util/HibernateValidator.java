package com.linkallcloud.core.util;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.exception.BaseRuntimeException;

/**
 * Bean 数据校验工具类
 *
 */
public class HibernateValidator {
    // public static final String HTML_BR = "<br>";

    private static HibernateValidator instance;

    private Validator validator;

    private HibernateValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static HibernateValidator getInstance() {
        if (instance == null) {
            synchronized (HibernateValidator.class) {
                if (instance == null) {
                    instance = new HibernateValidator();
                }
            }
        }
        return instance;
    }

    public <T> Set<ConstraintViolation<T>> validator(T object) {
        if (object == null) {
            throw new BaseRuntimeException("10000001", "您要验证属性的对象不能为空");
        }
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        return constraintViolations;
    }

    public <T> void validatorThrowException(T object) {
        Set<ConstraintViolation<T>> constraintViolations = validator(object);
        if (constraintViolations != null && constraintViolations.size() > 0) {
            // StringBuilder sb = new StringBuilder(HTML_BR);
            StringBuilder sb = new StringBuilder();
            String message = null;
            for (ConstraintViolation<T> violation : constraintViolations) {
                message = violation.getMessage();
                if (message != null) {
                    message = replaceBlank(message);
                }
                if (Strings.isBlank(message)) {
                    throw new BaseRuntimeException("10000001", "验证属性必须设置message不能为空");
                }
                String res = ResBundle.getMessage(message);
                if (Strings.isBlank(res)) {
                    // sb.append(message).append(HTML_BR);
                    sb.append(message).append(" ");
                } else {
                    // sb.append(res).append(HTML_BR);
                    sb.append(res).append(" ");
                }
            }

            String code = message;
            if (Strings.isBlank(code)) {
                code = "10000002";
            }
            throw new BaseRuntimeException(code, sb.toString());
        }
    }

    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
