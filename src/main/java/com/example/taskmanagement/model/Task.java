package com.example.taskmanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Task {
    @Schema(example = "1")
    private Long id;
    
    @Schema(example = "示例任务标题")
    private String title;
    
    @Schema(example = "这是一个示例任务描述，用于演示Swagger文档中的示例值功能。")
    private String description;
    
    @Schema(example = "false")
    private Boolean completed;
    
    @Schema(example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(example = "2023-01-01T10:00:00")
    private LocalDateTime updatedAt;
}