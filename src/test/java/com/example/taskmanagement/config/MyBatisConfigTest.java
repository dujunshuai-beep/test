package com.example.taskmanagement.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MyBatisConfigTest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Test
    void testSqlSessionFactory() {
        // 验证SqlSessionFactory是否正确创建
        assertNotNull(sqlSessionFactory);
        
        // 验证数据源是否被正确配置
        assertEquals(dataSource, sqlSessionFactory.getConfiguration().getEnvironment().getDataSource());
        
        // 验证配置是否成功创建
        assertNotNull(sqlSessionFactory, "SqlSessionFactory应该被成功创建");
        assertNotNull(sqlSessionFactory.getConfiguration(), "配置应该被成功设置");
        assertNotNull(sqlSessionFactory.getConfiguration().getEnvironment(), "环境配置应该被成功设置");
    }

    @Test
    void testTransactionManager() {
        // 验证事务管理器是否正确创建
        assertNotNull(transactionManager);
        
        // 验证数据源是否被正确注入到事务管理器
        assertEquals(dataSource, transactionManager.getDataSource());
    }
}