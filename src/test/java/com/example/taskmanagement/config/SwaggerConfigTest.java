package com.example.taskmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SwaggerConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void testCustomOpenAPI() {
        // 验证OpenAPI对象是否创建成功
        assertNotNull(openAPI);
        
        // 验证Info信息是否正确
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("任务管理系统 API", info.getTitle());
        assertEquals("1.0", info.getVersion());
        assertEquals("提供任务的增删改查功能，支持分页、搜索和过滤", info.getDescription());
        
        // 验证服务器配置是否正确
        assertNotNull(openAPI.getServers());
        assertFalse(openAPI.getServers().isEmpty());
        Server server = openAPI.getServers().get(0);
        assertEquals("http://localhost:8080", server.getUrl());
        assertEquals("开发环境", server.getDescription());
    }
}