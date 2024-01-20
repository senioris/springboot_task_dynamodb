package com.example.demo.dynamodb.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dynamodb.model.Task;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Component
public class TaskRepository {
    final DynamoDbEnhancedClient client;
    final DynamoDbTable<Task> table;

    @Autowired
    public TaskRepository(DynamoDbEnhancedClient client) {
        this.client = client;
        this.table = client.table("test", TableSchema.fromBean(Task.class));
    }    

    public void save(Task test) {
        table.putItem(test);
    }

    public Optional<Task> select(String id) {
        QueryConditional keyEqual = QueryConditional.keyEqualTo(b -> b.partitionValue(id));

        QueryEnhancedRequest tableQuery = QueryEnhancedRequest.builder()
            .queryConditional(keyEqual)
            .build();
        PageIterable<Task> pagedResults = table.query(tableQuery);
        
        return pagedResults.items().stream().findFirst();
    }

    public void update(Task task) {
        table.updateItem(task);
    }

    public void delete(String taskId) {
        var task = new Task(taskId, null);
        table.deleteItem(task);
    }

    public List<Task> selectList(long offset, int limit) {
        var iteratable = table.scan();
        return iteratable.items().stream().skip(offset).limit(limit).toList();
    }
}