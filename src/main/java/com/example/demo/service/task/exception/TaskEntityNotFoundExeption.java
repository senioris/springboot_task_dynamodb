package com.example.demo.service.task.exception;

public class TaskEntityNotFoundExeption extends RuntimeException{
    private String taskId;

    public TaskEntityNotFoundExeption(String taskId) {
        super("TaskEntity (id = " + taskId + ") is not found." );
    }
}
