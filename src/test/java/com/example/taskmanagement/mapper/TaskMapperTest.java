package com.example.taskmanagement.mapper;

import com.example.taskmanagement.model.Task;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskMapperTest {

    @Autowired
    private TaskMapper taskMapper;

    private Task testTask;

    @BeforeEach
    void setUp() {
        // 创建测试数据
        testTask = new Task();
        testTask.setTitle("测试任务");
        testTask.setDescription("这是一个测试任务的描述");
        testTask.setCompleted(false);
        testTask.setCreatedAt(LocalDateTime.now());
        testTask.setUpdatedAt(LocalDateTime.now());
        
        // 插入测试数据
        taskMapper.insert(testTask);
    }

    @AfterEach
    void tearDown() {
        // 清理测试数据
        if (testTask.getId() != null) {
            taskMapper.delete(testTask.getId());
        }
    }

    @Test
    void testFindById() {
        // 测试根据ID查询任务
        Task foundTask = taskMapper.findById(testTask.getId());
        assertNotNull(foundTask);
        assertEquals(testTask.getTitle(), foundTask.getTitle());
        assertEquals(testTask.getDescription(), foundTask.getDescription());
    }

    @Test
    void testFindById_NonExistent() {
        // 测试查询不存在的任务
        Task foundTask = taskMapper.findById(-1L);
        assertNull(foundTask);
    }

    @Test
    void testInsert() {
        // 测试插入新任务
        Task newTask = new Task();
        newTask.setTitle("新测试任务");
        newTask.setDescription("这是新测试任务的描述");
        newTask.setCompleted(true);
        newTask.setCreatedAt(LocalDateTime.now());
        newTask.setUpdatedAt(LocalDateTime.now());
        
        int result = taskMapper.insert(newTask);
        assertEquals(1, result);
        assertNotNull(newTask.getId());
        
        // 验证插入成功
        Task foundTask = taskMapper.findById(newTask.getId());
        assertNotNull(foundTask);
        assertEquals(newTask.getTitle(), foundTask.getTitle());
        
        // 清理
        taskMapper.delete(newTask.getId());
    }

    @Test
    void testUpdate() {
        // 测试更新任务
        testTask.setTitle("更新后的任务标题");
        testTask.setCompleted(true);
        testTask.setUpdatedAt(LocalDateTime.now());
        
        int result = taskMapper.update(testTask);
        assertEquals(1, result);
        
        // 验证更新成功
        Task updatedTask = taskMapper.findById(testTask.getId());
        assertEquals("更新后的任务标题", updatedTask.getTitle());
        assertTrue(updatedTask.getCompleted());
    }

    @Test
    void testDelete() {
        // 测试删除任务
        int result = taskMapper.delete(testTask.getId());
        assertEquals(1, result);
        
        // 验证删除成功
        Task deletedTask = taskMapper.findById(testTask.getId());
        assertNull(deletedTask);
    }

    @Test
    void testDelete_NonExistent() {
        // 测试删除不存在的任务
        int result = taskMapper.delete(-1L);
        assertEquals(0, result);
    }

    @Test
    void testFindAll_WithoutFilters() {
        // 测试不带过滤条件的查询
        Map<String, Object> params = new HashMap<>();
        Pageable pageable = PageRequest.of(0, 10);
        
        List<Task> tasks = taskMapper.findAll(params, pageable);
        assertNotNull(tasks);
        assertTrue(tasks.size() > 0);
    }

    @Test
    void testFindAll_WithTitleFilter() {
        // 测试带标题过滤的查询
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试");
        Pageable pageable = PageRequest.of(0, 10);
        
        List<Task> tasks = taskMapper.findAll(params, pageable);
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        
        // 验证过滤结果
        for (Task task : tasks) {
            assertTrue(task.getTitle().contains("测试"));
        }
    }

    @Test
    void testFindAll_WithDescriptionFilter() {
        // 测试带描述过滤的查询
        Map<String, Object> params = new HashMap<>();
        params.put("description", "描述");
        Pageable pageable = PageRequest.of(0, 10);
        
        List<Task> tasks = taskMapper.findAll(params, pageable);
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        
        // 验证过滤结果
        for (Task task : tasks) {
            assertTrue(task.getDescription().contains("描述"));
        }
    }

    @Test
    void testFindAll_WithCompletedFilter() {
        // 测试带完成状态过滤的查询
        Map<String, Object> params = new HashMap<>();
        params.put("completed", false);
        Pageable pageable = PageRequest.of(0, 10);
        
        List<Task> tasks = taskMapper.findAll(params, pageable);
        assertNotNull(tasks);
        
        // 验证过滤结果
        for (Task task : tasks) {
            assertFalse(task.getCompleted());
        }
    }

    @Test
    void testCount_WithoutFilters() {
        // 测试不带过滤条件的计数
        Map<String, Object> params = new HashMap<>();
        int count = taskMapper.count(params);
        assertTrue(count > 0);
    }

    @Test
    void testCount_WithFilters() {
        // 测试带过滤条件的计数
        Map<String, Object> params = new HashMap<>();
        params.put("title", "测试");
        int count = taskMapper.count(params);
        assertTrue(count > 0);
    }
}