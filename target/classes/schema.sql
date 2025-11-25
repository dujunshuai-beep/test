DROP TABLE IF EXISTS tasks;

CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入一些测试数据
INSERT INTO tasks (title, description, completed) VALUES
('完成Spring Boot项目', '创建一个任务管理系统', FALSE),
('学习MyBatis', '掌握动态SQL和分页查询', FALSE),
('写单元测试', '确保代码覆盖率超过80%', TRUE);