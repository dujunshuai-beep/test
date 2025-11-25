package com.example.taskmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.taskmanagement.mapper")
public class TaskManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApiApplication.class, args);
    }

}