package com.example.taskmanagement.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskGetterSetter() {
        // 创建测试对象
        Task task = new Task();
        
        // 测试setter和getter方法
        Long id = 1L;
        String title = "Test Task";
        String description = "This is a test task description";
        Boolean completed = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(completed);
        task.setCreatedAt(createdAt);
        task.setUpdatedAt(updatedAt);
        
        // 验证getter方法
        assertEquals(id, task.getId());
        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(completed, task.getCompleted());
        assertEquals(createdAt, task.getCreatedAt());
        assertEquals(updatedAt, task.getUpdatedAt());
    }
    
    @Test
    public void testTaskEqualsAndHashCode() {
        // 创建两个相同的任务
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        
        Task task2 = new Task();
        task2.setId(1L);
        task2.setTitle("Task 1");
        
        // 创建一个不同的任务
        Task task3 = new Task();
        task3.setId(2L);
        task3.setTitle("Task 2");
        
        // 测试equals方法
        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
        assertNotEquals(task1, null);
        assertNotEquals(task1, "not a task");
        
        // 测试hashCode方法
        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1.hashCode(), task3.hashCode());
    }
    
    @Test
    public void testTaskToString() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        
        // 验证toString方法包含对象的属性信息
        String toString = task.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("title=Test Task"));
    }
    
    @Test
    public void testTaskWithNullValues() {
        // 测试空值情况
        Task task = new Task();
        
        assertNull(task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertNull(task.getCompleted());
        assertNull(task.getCreatedAt());
        assertNull(task.getUpdatedAt());
        
        // 设置一些值为null后再测试
        task.setId(1L);
        task.setTitle("Test");
        task.setTitle(null);
        
        assertNull(task.getTitle());
        assertEquals(1L, task.getId());
    }
    
    @Test
    public void testTaskCopy() {
        // 测试复制对象属性
        Task source = new Task();
        source.setId(1L);
        source.setTitle("Source Task");
        source.setDescription("Source description");
        source.setCompleted(false);
        
        Task target = new Task();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setCompleted(source.getCompleted());
        
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getTitle(), target.getTitle());
        assertEquals(source.getDescription(), target.getDescription());
        assertEquals(source.getCompleted(), target.getCompleted());
    }
}