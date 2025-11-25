package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.mapper.TaskMapper;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Page<Task> findAllTasks(Map<String, Object> params, Pageable pageable) {
        // 构建查询参数
        Map<String, Object> queryParams = new HashMap<>();
        if (params != null) {
            queryParams.putAll(params);
        }
        
        // 查询数据
        List<Task> tasks = taskMapper.findAll(queryParams, pageable);
        
        // 查询总数
        int total = taskMapper.count(queryParams);
        
        return new PageImpl<>(tasks, pageable, total);
    }

    @Override
    public List<Task> findAll() {
        // 使用分页参数获取所有任务，但设置一个很大的页大小来获取所有数据
        Map<String, Object> params = new HashMap<>();
        // 使用实际的Pageable对象而不是unpaged()，避免UnsupportedOperationException
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        return taskMapper.findAll(params, pageable);
    }

    @Override
    public Task findById(Long id) {
        return taskMapper.findById(id);
    }

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            taskMapper.insert(task);
        } else {
            taskMapper.update(task);
        }
        return task;
    }

    @Override
    public Task update(Long id, Task task) {
        task.setId(id);
        taskMapper.update(task);
        return taskMapper.findById(id);
    }

    @Override
    public void delete(Long id) {
        taskMapper.delete(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            // 批量删除任务
            for (Long id : ids) {
                taskMapper.delete(id);
            }
        }
    }

    @Override
    public Task updateStatus(Long id, Boolean completed) {
        Task task = taskMapper.findById(id);
        if (task != null) {
            task.setCompleted(completed);
            taskMapper.update(task);
        }
        return task;
    }
}