package org.example.belajar_spring.service;

import org.example.belajar_spring.entity.User;
import org.example.belajar_spring.model.task.CreateTaskRequest;
import org.example.belajar_spring.model.task.SearchTaskRequest;
import org.example.belajar_spring.model.task.TaskResponse;
import org.example.belajar_spring.model.task.UpdateTaskRequest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface TaskService {
    @Transactional
    public TaskResponse createTask(User user, CreateTaskRequest request);

    @Transactional
    public TaskResponse updateTask(User user, UpdateTaskRequest request);

    @Transactional(readOnly = true)
    public TaskResponse getTask(User user, String id);

    @Transactional
    public void delete(User user, String taskId);

    @Transactional(readOnly = true)
    public Page<TaskResponse> searchTask(User user, SearchTaskRequest request);
}
