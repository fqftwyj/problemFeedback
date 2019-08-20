package com.yuanwang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@ServletComponentScan
@SpringBootApplication

public class WebFrameApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(WebFrameApplication.class, args);
	}
/*	protected SpringApplicationBuilder configure (SpringApplicationBuilder builder){
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(WebFrameApplication.class);
	}*/
}


