package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "获取任务列表", description = "支持分页、搜索和过滤的任务列表查询")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取任务列表",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String direction,
            @Parameter(description = "任务标题搜索关键词") @RequestParam(required = false) String title,
            @Parameter(description = "任务描述搜索关键词") @RequestParam(required = false) String description,
            @Parameter(description = "任务状态（true/false）") @RequestParam(required = false) Boolean completed,
            @Parameter(description = "开始日期（yyyy-MM-dd）") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期（yyyy-MM-dd）") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // 构建过滤参数
        Map<String, Object> params = new HashMap<>();
        if (title != null && !title.isEmpty()) params.put("title", title);
        if (description != null && !description.isEmpty()) params.put("description", description);
        if (completed != null) params.put("completed", completed);
        if (startDate != null) params.put("startDate", LocalDateTime.of(startDate, LocalTime.MIN));
        if (endDate != null) params.put("endDate", LocalDateTime.of(endDate, LocalTime.MAX));

        // 构建分页参数
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> tasks = taskService.findAllTasks(params, pageable);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "获取单个任务", description = "根据ID获取任务详情")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取任务详情"),
            @ApiResponse(responseCode = "404", description = "任务不存在")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@Parameter(description = "任务ID", example = "1") @PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "创建新任务", description = "创建一个新的任务")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "任务创建成功"),
            @ApiResponse(responseCode = "400", description = "请求参数错误")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> createTask(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "任务信息",
            required = true,
            content = @Content(schema = @Schema(implementation = Task.class, example = "{\"title\": \"学习Spring Boot\", \"description\": \"完成项目开发\", \"completed\": false}"))
    ) @RequestBody Task task) {
        Task createdTask = taskService.save(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "更新任务", description = "根据ID更新任务信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "任务更新成功"),
            @ApiResponse(responseCode = "404", description = "任务不存在"),
            @ApiResponse(responseCode = "400", description = "请求参数错误")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "任务ID", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "任务信息",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Task.class, example = "{\"title\": \"更新后的任务标题\", \"description\": \"更新后的任务描述\", \"completed\": true}"))
            ) @RequestBody Task task) {
        Task updatedTask = taskService.update(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "删除任务", description = "根据ID删除任务")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "任务删除成功"),
            @ApiResponse(responseCode = "404", description = "任务不存在")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTask(@Parameter(description = "任务ID", example = "1") @PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "批量删除任务", description = "一次性删除多个任务")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "任务删除成功"),
            @ApiResponse(responseCode = "400", description = "请求参数错误")
    })
    @DeleteMapping("/batch")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBatch(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "任务ID列表",
                    required = true,
                    content = @Content(schema = @Schema(example = "[1, 2, 3]"))
            ) @RequestBody List<Long> ids) {
        taskService.deleteBatch(ids);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "更新任务状态", description = "更新任务的完成状态")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "状态更新成功"),
            @ApiResponse(responseCode = "404", description = "任务不存在")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @Parameter(description = "任务ID", example = "1") @PathVariable Long id,
            @Parameter(description = "完成状态", example = "true") @RequestParam Boolean completed) {
        Task updatedTask = taskService.updateStatus(id, completed);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "获取所有任务（不分页）", description = "获取所有任务列表，不分页")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取任务列表")
    })
    @GetMapping("/all/list")
    public ResponseEntity<List<Task>> getAllTasksList() {
        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }
}