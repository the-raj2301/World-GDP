package com.raj.worldgdp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
//@ComponentScan("com.raj.worldgdp")
public class ViewConfiguration {
	
	
	@Bean
	public SpringResourceTemplateResolver resolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("classpath:/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCacheable(false);
		return resolver;
	}
	
	@Bean
	public SpringTemplateEngine engine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(resolver());
		return engine;
	}
	
	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(engine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}
}
