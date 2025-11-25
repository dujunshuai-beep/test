package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.mapper.TaskMapper;
import com.example.taskmanagement.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 初始化测试数据
        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("任务1");
        task1.setDescription("这是任务1的描述");
        task1.setCompleted(false);
        task1.setCreatedAt(LocalDateTime.now());
        task1.setUpdatedAt(LocalDateTime.now());

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("任务2");
        task2.setDescription("这是任务2的描述");
        task2.setCompleted(true);
        task2.setCreatedAt(LocalDateTime.now());
        task2.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testFindAllTasksWithParams() {
        List<Task> tasks = Arrays.asList(task1, task2);
        Map<String, Object> params = new HashMap<>();
        params.put("title", "任务");
        Pageable pageable = PageRequest.of(0, 10);
        
        when(taskMapper.findAll(eq(params), any(PageRequest.class))).thenReturn(tasks);
        when(taskMapper.count(params)).thenReturn(2);
        
        Page<Task> result = taskService.findAllTasks(params, pageable);
        
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(taskMapper).findAll(eq(params), any(PageRequest.class));
        verify(taskMapper).count(params);
    }
    
    @Test
    void testFindAllTasksWithTitleFilter() {
        // 测试标题过滤功能
        List<Task> filteredTasks = Arrays.asList(task1);
        Map<String, Object> params = new HashMap<>();
        params.put("title", "任务1");
        Pageable pageable = PageRequest.of(0, 10);
        
        when(taskMapper.findAll(eq(params), any(PageRequest.class))).thenReturn(filteredTasks);
        when(taskMapper.count(params)).thenReturn(1);
        
        Page<Task> result = taskService.findAllTasks(params, pageable);
        
        assertEquals(1, result.getTotalElements());
        assertEquals("任务1", result.getContent().get(0).getTitle());
        verify(taskMapper).findAll(eq(params), any(PageRequest.class));
        verify(taskMapper).count(params);
    }
    
    @Test
    void testFindAllTasksWithDescriptionFilter() {
        // 测试描述过滤功能
        List<Task> filteredTasks = Arrays.asList(task2);
        Map<String, Object> params = new HashMap<>();
        params.put("description", "任务2");
        Pageable pageable = PageRequest.of(0, 10);
        
        when(taskMapper.findAll(eq(params), any(PageRequest.class))).thenReturn(filteredTasks);
        when(taskMapper.count(params)).thenReturn(1);
        
        Page<Task> result = taskService.findAllTasks(params, pageable);
        
        assertEquals(1, result.getTotalElements());
        assertEquals("任务2", result.getContent().get(0).getTitle());
        verify(taskMapper).findAll(eq(params), any(PageRequest.class));
        verify(taskMapper).count(params);
    }
    
    @Test
    void testFindAllTasksWithCompletedFilter() {
        // 测试完成状态过滤功能
        List<Task> filteredTasks = Arrays.asList(task2); // task2是已完成的
        Map<String, Object> params = new HashMap<>();
        params.put("completed", true);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(taskMapper.findAll(eq(params), any(PageRequest.class))).thenReturn(filteredTasks);
        when(taskMapper.count(params)).thenReturn(1);
        
        Page<Task> result = taskService.findAllTasks(params, pageable);
        
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getCompleted());
        verify(taskMapper).findAll(eq(params), any(PageRequest.class));
        verify(taskMapper).count(params);
    }
    
    @Test
    void testFindAllTasksWithDateRangeFilter() {
        // 测试日期范围过滤功能
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        
        List<Task> filteredTasks = Arrays.asList(task1, task2);
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(taskMapper.findAll(eq(params), any(PageRequest.class))).thenReturn(filteredTasks);
        when(taskMapper.count(params)).thenReturn(2);
        
        Page<Task> result = taskService.findAllTasks(params, pageable);
        
        assertEquals(2, result.getTotalElements());
        verify(taskMapper).findAll(eq(params), any(PageRequest.class));
        verify(taskMapper).count(params);
    }
    
    @Test
    void testFindAllTasksWithMultipleFilters() {
        // 测试组合过滤条件
        List<Task> filteredTasks = Arrays.asList(task1);
        Map<String, Object> params = new HashMap<>();
        params.put("title", "任务1");
        params.put("completed", false);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(taskMapper.findAll(eq(params), any(PageRequest.class))).thenReturn(filteredTasks);
        when(taskMapper.count(params)).thenReturn(1);
        
        Page<Task> result = taskService.findAllTasks(params, pageable);
        
        assertEquals(1, result.getTotalElements());
        assertEquals("任务1", result.getContent().get(0).getTitle());
        assertFalse(result.getContent().get(0).getCompleted());
        verify(taskMapper).findAll(eq(params), any(PageRequest.class));
        verify(taskMapper).count(params);
    }
    
    @Test
    void testFindAllTasksWithNullParams() {
        List<Task> tasks = Arrays.asList(task1);
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, Object> emptyParams = new HashMap<>();
        
        when(taskMapper.findAll(eq(emptyParams), any(PageRequest.class))).thenReturn(tasks);
        when(taskMapper.count(emptyParams)).thenReturn(1);
        
        Page<Task> result = taskService.findAllTasks(null, pageable);
        
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(taskMapper).findAll(eq(emptyParams), any(PageRequest.class));
        verify(taskMapper).count(emptyParams);
    }

    @Test
    void testFindAll() {
        List<Task> tasks = Arrays.asList(task1, task2);
        Map<String, Object> emptyParams = new HashMap<>();
        // 使用与实现相同类型的Pageable对象
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        
        when(taskMapper.findAll(emptyParams, pageable)).thenReturn(tasks);
        
        List<Task> result = taskService.findAll();
        
        assertEquals(2, result.size());
        assertEquals("任务1", result.get(0).getTitle());
        // 使用eq()匹配器来匹配emptyParams，保持一致性
        verify(taskMapper).findAll(eq(emptyParams), any(PageRequest.class));
    }

    @Test
    void testFindById() {
        when(taskMapper.findById(1L)).thenReturn(task1);
        
        Task result = taskService.findById(1L);
        
        assertEquals(1L, result.getId());
        assertEquals("任务1", result.getTitle());
        verify(taskMapper).findById(1L);
    }

    @Test
    void testSaveNewTask() {
        // 准备测试数据
        Task task = new Task();
        task.setTitle("新任务");
        task.setDescription("这是一个新任务");
        task.setCompleted(false);
        
        // 配置mock行为
        when(taskMapper.insert(task)).thenReturn(1);
        
        // 执行测试
        Task savedTask = taskService.save(task);
        
        // 验证结果
        assertNotNull(savedTask);
        verify(taskMapper).insert(task);
    }
    
    @Test
    void testSaveExistingTask() {
        // 准备测试数据
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Updated Task");
        task.setDescription("Updated Description");
        task.setCompleted(true);
        
        // 执行测试
        when(taskMapper.update(task)).thenReturn(1);
        Task savedTask = taskService.save(task);
        
        // 验证结果
        verify(taskMapper).update(task);
        assertEquals(task, savedTask);
    }

    @Test
    void testUpdate() {
        Task updatedTask = new Task();
        updatedTask.setTitle("更新后的任务1");
        updatedTask.setDescription("更新后的描述");
        updatedTask.setCompleted(true);
        
        when(taskMapper.findById(1L)).thenReturn(task1);
        when(taskMapper.update(any(Task.class))).thenReturn(1);
        when(taskMapper.findById(1L)).thenReturn(updatedTask);
        
        Task result = taskService.update(1L, updatedTask);
        
        assertEquals(1L, result.getId());
        assertEquals("更新后的任务1", result.getTitle());
        assertTrue(result.getCompleted());
        verify(taskMapper).findById(1L);
        verify(taskMapper).update(any(Task.class));
    }

    @Test
    void testDelete() {
        when(taskMapper.delete(1L)).thenReturn(1);
        
        taskService.delete(1L);
        
        verify(taskMapper).delete(1L);
    }

    @Test
    void testDeleteBatch() {
        List<Long> ids = Arrays.asList(1L, 2L);
        
        when(taskMapper.delete(1L)).thenReturn(1);
        when(taskMapper.delete(2L)).thenReturn(1);
        
        taskService.deleteBatch(ids);
        
        verify(taskMapper).delete(1L);
        verify(taskMapper).delete(2L);
    }

    @Test
    void testDeleteBatch_EmptyList() {
        // 测试空列表的边界条件
        List<Long> emptyIds = Arrays.asList();
        // 空列表时不应该调用删除方法
        taskService.deleteBatch(emptyIds);
        verify(taskMapper, never()).delete(anyLong());
    }

    @Test
    void testDeleteBatch_NullList() {
        // 测试null列表的边界条件
        assertDoesNotThrow(() -> {
            taskService.deleteBatch(null);
        }, "传入null列表时应该能正常处理");
        verify(taskMapper, never()).delete(anyLong());
    }

    @Test
    void testUpdateStatusWithNullTask() {
        // 测试更新不存在任务状态的边界条件
        when(taskMapper.findById(anyLong())).thenReturn(null);
        
        Task result = taskService.updateStatus(1L, true);
        
        assertNull(result, "更新不存在任务的状态应该返回null");
        verify(taskMapper).findById(1L);
        verify(taskMapper, never()).update(any(Task.class));
    }

    @Test
    void testSaveWithNullTask() {
        // 测试保存null任务的边界条件
        assertThrows(NullPointerException.class, () -> {
            taskService.save(null);
        }, "保存null任务应该抛出NullPointerException");
    }

    @Test
    void testUpdateWithNullTask() {
        // 测试更新null任务的边界条件
        assertThrows(NullPointerException.class, () -> {
            taskService.update(1L, null);
        }, "更新null任务应该抛出NullPointerException");
    }

    @Test
    void testUpdateStatus() {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("任务1");
        updatedTask.setDescription("这是任务1的描述");
        updatedTask.setCompleted(true);
        
        when(taskMapper.findById(1L)).thenReturn(task1);
        when(taskMapper.update(any(Task.class))).thenReturn(1);
        
        Task result = taskService.updateStatus(1L, true);
        
        assertNotNull(result);
        assertTrue(result.getCompleted());
        verify(taskMapper).findById(1L);
        verify(taskMapper).update(any(Task.class));
    }
    
    @Test
    void testUpdateStatusTaskNotFound() {
        // 配置mock行为
        when(taskMapper.findById(1L)).thenReturn(null);
        
        // 执行测试
        Task updatedTask = taskService.updateStatus(1L, true);
        
        // 验证结果
        verify(taskMapper).findById(1L);
        verify(taskMapper, never()).update(any(Task.class));
        assertNull(updatedTask);
    }
}