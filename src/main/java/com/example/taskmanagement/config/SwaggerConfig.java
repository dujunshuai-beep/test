package com.example.taskmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("任务管理系统 API")
                        .version("1.0")
                        .description("提供任务的增删改查功能，支持分页、搜索和过滤")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .servers(getServers());
    }
    
    private List<Server> getServers() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("开发环境");
        List<Server> servers = new ArrayList<>();
        servers.add(server);
        return servers;
    }
}