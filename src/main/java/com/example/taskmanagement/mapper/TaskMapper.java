package com.example.taskmanagement.mapper;

import com.example.taskmanagement.model.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskMapper {
    // 获取所有任务，支持分页、搜索和过滤
    List<Task> findAll(@Param("params") Map<String, Object> params,
                      Pageable pageable);
    
    // 根据ID获取任务
    Task findById(Long id);
    
    // 插入任务
    int insert(Task task);
    
    // 更新任务
    int update(Task task);
    
    // 删除任务
    int delete(Long id);
    
    // 获取任务总数，支持条件查询
    int count(@Param("params") Map<String, Object> params);
}