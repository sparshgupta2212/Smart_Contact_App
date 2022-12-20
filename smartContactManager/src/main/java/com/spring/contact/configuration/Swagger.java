package com.spring.contact.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Paths;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class Swagger {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getInfo()).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
	}
	
	public ApiInfo getInfo() {
		return new ApiInfo("Smart Contact Manager", "Apiii","1.0" ,"Terms of serevice", new Contact("Sparsh",null,null ), null, null,Collections.emptyList());
	}
	
}
