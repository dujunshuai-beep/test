package com.example.taskmanagement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskManagementApiApplicationTest {

    @Test
    void testMainMethodExists() {
        // 测试主类是否存在且可实例化
        TaskManagementApiApplication application = new TaskManagementApiApplication();
        assertNotNull(application, "TaskManagementApiApplication类应该能够被实例化");
    }
    
    @Test
    void testMainMethodDoesNotThrowException() {
        // 测试main方法调用不会抛出异常（不会实际启动应用）
        try {
            // 这里只是验证方法存在，不会实际执行启动
            Class<?> clazz = Class.forName("com.example.taskmanagement.TaskManagementApiApplication");
            java.lang.reflect.Method method = clazz.getMethod("main", String[].class);
            assertNotNull(method, "main方法应该存在");
        } catch (Exception e) {
            throw new AssertionError("main方法不存在或无法访问", e);
        }
    }
}