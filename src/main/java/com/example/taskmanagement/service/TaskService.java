package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TaskService {
    /**
     * 分页查询任务列表
     */
    Page<Task> findAllTasks(Map<String, Object> params, Pageable pageable);
    
    // 获取所有任务列表
    List<Task> findAll();
    
    // 根据ID获取任务
    Task findById(Long id);
    
    // 创建任务
    Task save(Task task);
    
    // 更新任务
    Task update(Long id, Task task);
    
    // 删除任务
    void delete(Long id);
    
    // 批量删除任务
    void deleteBatch(List<Long> ids);
    
    // 更新任务状态
    Task updateStatus(Long id, Boolean completed);
}