package com.example.demo.dynamodb.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dynamodb.repository.TaskRepository;
import com.example.demo.dynamodb.model.Task;
import com.example.demo.service.task.TaskEntity;
import com.example.demo.service.task.exception.TaskEntityNotFoundExeption;

@Service
public class TaskService {
    TaskRepository repository;

    @Autowired
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskEntity find(String taskId) {
        return repository.select(taskId)
            .map(record -> new TaskEntity(record.getId(), record.getTitle()))
            .orElseThrow(() -> new TaskEntityNotFoundExeption(taskId));
    }

    public List<TaskEntity> find(long offset, int limit) {
        var recordList = repository.selectList(offset, limit);
        var entityList = recordList.stream().map(record -> {
            var entity = new TaskEntity(record.getId(), record.getTitle());
            return entity;
        }).collect(Collectors.toList());

        return entityList;
    }

    public TaskEntity create(String title) {
        var insertData = new Task();
        var id = UUID.randomUUID().toString();
        insertData.setId(id);
        insertData.setTitle(title);

        repository.save(insertData);

        return new TaskEntity(id, title);
    }

    public void update(String taskId, String title) {
        repository.select(taskId)
            .orElseThrow(() -> new TaskEntityNotFoundExeption(taskId));
        repository.update(new Task(taskId, title));
    }

    public void delete(String taskId) {
        repository.select(taskId)
            .orElseThrow(() -> new TaskEntityNotFoundExeption(taskId));
        repository.delete(taskId);
    }
}
