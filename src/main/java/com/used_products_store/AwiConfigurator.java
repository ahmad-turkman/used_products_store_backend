package com.used_products_store;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class AwiConfigurator {

	 @Bean
	 MultipartConfigElement multipartConfigElement() {

		 MultipartConfigFactory factory = new MultipartConfigFactory();
	     factory.setMaxFileSize(DataSize.ofMegabytes(3000));
	     factory.setMaxRequestSize(DataSize.ofMegabytes(3000));
	     
	     return factory.createMultipartConfig();
	 }
}