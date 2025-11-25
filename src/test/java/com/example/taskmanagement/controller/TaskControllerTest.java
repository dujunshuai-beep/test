package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.anyBoolean;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        // 初始化MockMvc，配置异常处理
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new Object() {
                    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
                    public org.springframework.http.ResponseEntity<String> handleRuntimeException(RuntimeException e) {
                        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                    }
                })
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
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
    void testGetAllTasks() throws Exception {
        List<Task> tasks = Arrays.asList(task1, task2);
        Page<Task> taskPage = new PageImpl<>(tasks);
        // 不指定具体参数，使用any匹配器
        Mockito.when(taskService.findAllTasks(Mockito.any(Map.class), Mockito.any(Pageable.class))).thenReturn(taskPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value("任务1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].title").value("任务2"));
    }
    
    @Test
    void testGetAllTasks_WithAllFilters() throws Exception {
        List<Task> tasks = Arrays.asList(task1);
        Page<Task> taskPage = new PageImpl<>(tasks);
        Mockito.when(taskService.findAllTasks(Mockito.any(Map.class), Mockito.any(Pageable.class))).thenReturn(taskPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                .param("title", "任务1")
                .param("description", "描述")
                .param("completed", "false")
                .param("startDate", "2023-01-01")
                .param("endDate", "2024-01-01")
                .param("page", "1")
                .param("size", "5")
                .param("sortBy", "title")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1));
    }
    
    @Test
    void testGetAllTasks_AscendingSort() throws Exception {
        List<Task> tasks = Arrays.asList(task1, task2);
        Page<Task> taskPage = new PageImpl<>(tasks);
        Mockito.when(taskService.findAllTasks(Mockito.any(Map.class), Mockito.any(Pageable.class))).thenReturn(taskPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetTaskById() throws Exception {
        Mockito.when(taskService.findById(1L)).thenReturn(task1);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("任务1"));
    }

    @Test
    void testGetTaskById_NotFound() {
        // 使用anyLong匹配器
        Mockito.when(taskService.findById(Mockito.anyLong())).thenThrow(new RuntimeException("Task not found with id: 999"));

        // 使用JUnit 5的assertThrows来捕获异常
        assertThrows(Exception.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/tasks/999")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is5xxServerError());
        });
    }

    @Test
    void testCreateTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("新任务");
        newTask.setDescription("这是一个新任务");
        newTask.setCompleted(false);

        Task createdTask = new Task();
        createdTask.setId(3L);
        createdTask.setTitle("新任务");
        createdTask.setDescription("这是一个新任务");
        createdTask.setCompleted(false);

        Mockito.when(taskService.save(newTask)).thenReturn(createdTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("新任务"));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("更新后的任务1");
        updatedTask.setDescription("更新后的描述");
        updatedTask.setCompleted(true);

        Mockito.when(taskService.update(1L, updatedTask)).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("更新后的任务1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed").value(true));
    }
    
    @Test
    void testUpdateTask_WithNonExistentId() throws Exception {
        Mockito.when(taskService.update(Mockito.eq(999L), Mockito.any(Task.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("")
                );
    }

    @Test
    void testDeleteTask() throws Exception {
        Mockito.doNothing().when(taskService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testDeleteBatch() throws Exception {
        List<Long> ids = Arrays.asList(1L, 2L);
        Mockito.doNothing().when(taskService).deleteBatch(ids);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ids)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testUpdateTaskStatus() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("任务1");
        updatedTask.setCompleted(true);

        Mockito.when(taskService.updateStatus(1L, true)).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/1/status")
                .param("completed", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed").value(true));
    }
    
    @Test
    void testUpdateTaskStatus_WithNonExistentId() throws Exception {
        Mockito.when(taskService.updateStatus(Mockito.eq(999L), Mockito.anyBoolean())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/999/status")
                .param("completed", "true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("")
                );
    }
    
    @Test
    void testUpdateTaskStatus_ToFalse() throws Exception {
        Mockito.when(taskService.updateStatus(1L, false)).thenReturn(task1);

        mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/1/status")
                .param("completed", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void testUpdateTaskStatus_MissingParameter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/1/status")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testGetAllTasksList() throws Exception {
        List<Task> tasks = Arrays.asList(task1, task2);
        Mockito.when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/all/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("任务1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("任务2"));
    }

    @Test
    void testDeleteBatch_EmptyList() throws Exception {
        // 测试批量删除空列表
        List<Long> emptyList = Arrays.asList();

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyList)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testGetAllTasks_WithFilters() throws Exception {
        List<Task> tasks = Arrays.asList(task1);
        Page<Task> taskPage = new PageImpl<>(tasks);
        // 使用any匹配器，因为实际参数是由控制器构建的
        Mockito.when(taskService.findAllTasks(Mockito.any(Map.class), Mockito.any(Pageable.class))).thenReturn(taskPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                .param("title", "任务1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value("任务1"));
    }
}