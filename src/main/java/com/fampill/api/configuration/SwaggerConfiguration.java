package com.fampill.api.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
	
	@Bean
	public Docket api() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		docket.securityContexts(Arrays.asList(securityContext()));
		docket.securitySchemes(Arrays.asList(apiKey()));	
		docket.apiInfo(new ApiInfoBuilder()
			.title("Famfill API")
			.description("Famfill API Frontend ~ Backend 연동 문서<br/><br/>로그인 API<br/>URL : /login<br/> Method : post <br/> 아이디 : username <br/> 비밀번호 : password <br/> 인증이 필요한 API 요청시 Header에 Authorization=Bearer 토큰 추가해서 요청하면됨.")
			.build());
		return docket
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.fampill"))
			.paths(PathSelectors.any())
			.build();
	}
	
    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build();
    }

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
	}

	private ApiKey apiKey() {
		return new ApiKey("Authorization", "Authorization", "header");
	}
}
