package com.rsupport.notice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
            .title("Notice Service API DOCS")
            .version("v1")
            .description("공지사항 서비스 api 명세서");

        return new OpenAPI()
            .info(info);
    }
}
