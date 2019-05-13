package com.yuanwang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class WebFrameApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFrameApplication.class, args);
	}
}
