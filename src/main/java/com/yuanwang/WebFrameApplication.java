package com.yuanwang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.MultipartConfigElement;

/*
@ServletComponentScan
@SpringBootApplication
public class WebFrameApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(WebFrameApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(WebFrameApplication.class, args);
	}
}*/

@ServletComponentScan
@SpringBootApplication
public class WebFrameApplication{

	public static void main(String[] args) {
		SpringApplication.run(WebFrameApplication.class, args);
	}

}
