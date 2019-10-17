package com.linkallcloud.core.thymeleaf;

import java.util.Locale;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public class Templates {
	
	public static String processStringTemplate(String stringTemplate, Map<String, Object> data) {
		Locale locale = Locale.getDefault();
		
		StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setCacheable(true);
		templateResolver.setCacheTTLMs(3600000L);
		templateResolver.setName("StringTemplate");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		
		TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(stringTemplate, new Context(locale, data));
	}

}
