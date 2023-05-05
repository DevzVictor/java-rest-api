package br.com.victor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("RESTfull API")
						.version("v1")
						.description("API for training Java with springboot")
						.termsOfService("https://github.com/DevzVictor")
						.license(
							new License()
								.name("Apache 2.0")
								.url("https://github.com/DevzVictor")));
	}
}
