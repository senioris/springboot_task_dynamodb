package com.example.demo.controller.task;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.TasksApi;
import com.example.demo.dynamodb.service.TaskService;
import com.example.demo.model.PageDto;
import com.example.demo.model.TaskDto;
import com.example.demo.model.TaskForm;
import com.example.demo.model.TaskListDto;
import com.example.demo.service.task.TaskEntity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskController implements TasksApi{
    private final TaskService taskService;

    @Override
    public ResponseEntity<TaskDto> showTask(String taskId) {
        var entity = taskService.find(taskId);

        var body = toTaskDto(entity);
        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<TaskDto> createTask(TaskForm taskForm) {
        var entity = taskService.create(taskForm.getTitle());

        var body = toTaskDto(entity);
        return ResponseEntity
            .created(URI.create("/tasks/" + body.getId()))
            .body(body);
    }

    @Override
    public ResponseEntity<TaskListDto> listTasks(Long offset, Integer limit) {
        var entityList = taskService.find(offset, limit);
        var dtoList = createTaskDtoList(entityList);

        var body = new TaskListDto();
        body.setResults(dtoList);

        var page = new PageDto();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setSize(entityList.size());
        body.setPage(page);

        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<Void> updateTask(String taskId, TaskForm taskForm) {
        taskService.update(taskId, taskForm.getTitle());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteTask(String taskId) {
        taskService.delete(taskId);
        return ResponseEntity.noContent().build();
    }

    private static List<TaskDto> createTaskDtoList(List<TaskEntity> entities) {
        return entities.stream().map(TaskController::toTaskDto)
            .collect(Collectors.toList());
    }

    private static TaskDto toTaskDto(TaskEntity entity) {
        var dto = new TaskDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        return dto;
    }
}
