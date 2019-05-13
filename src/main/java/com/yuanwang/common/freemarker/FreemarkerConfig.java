package com.yuanwang.common.freemarker;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.TemplateException;

@Component
public class FreemarkerConfig {
	@Resource
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@PostConstruct
	public void setSharedVariable() throws IOException,TemplateException{
		freeMarkerConfigurer.getConfiguration().setSharedVariable("shiro", new ShiroTags());
	}
}
