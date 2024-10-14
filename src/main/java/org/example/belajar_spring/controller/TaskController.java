package org.example.belajar_spring.controller;

import org.example.belajar_spring.entity.User;
import org.example.belajar_spring.model.PagingResponse;
import org.example.belajar_spring.model.WebResponse;
import org.example.belajar_spring.model.task.CreateTaskRequest;
import org.example.belajar_spring.model.task.SearchTaskRequest;
import org.example.belajar_spring.model.task.TaskResponse;
import org.example.belajar_spring.model.task.UpdateTaskRequest;
import org.example.belajar_spring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping(
            path = "/api/task",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> createTask(User user, @RequestBody CreateTaskRequest request) {
        TaskResponse response = taskService.createTask(user, request);
        return WebResponse.<TaskResponse>builder().data(response).build();
    }

    @GetMapping(
            path = "/api/task/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> getTask(User user, @PathVariable("taskId") String taskId) {
        TaskResponse response = taskService.getTask(user, taskId);
        return WebResponse.<TaskResponse>builder().data(response).build();
    }

    @PutMapping(
            path = "/api/task/{taskId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> updateTask(
            User user,
            @RequestBody UpdateTaskRequest request,
            @PathVariable("taskId") String taskId) {
        request.setId(taskId);
        TaskResponse response = taskService.updateTask(user, request);
        return WebResponse.<TaskResponse>builder().data(response).build();
    }

    @DeleteMapping(
            path = "/api/task/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteTask(User user, @PathVariable("taskId") String taskId) {
        taskService.delete(user, taskId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "api/task",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<TaskResponse>> searchTask(User user,
                                                      @RequestParam(value = "title", required = false) String title,
                                                      @RequestParam(value = "startDate", required = false) String startDate,
                                                      @RequestParam(value = "endDate", required = false) String endDate,
                                                      @RequestParam(value = "status", required = false) String status,
                                                      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        SearchTaskRequest request = SearchTaskRequest.builder()
                .page(page)
                .size(size)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .build();

        Page<TaskResponse> taskResponses = taskService.searchTask(user, request);
        return WebResponse.<List<TaskResponse>>builder()
                .data(taskResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(taskResponses.getNumber())
                        .totalPage(taskResponses.getTotalPages())
                        .size(taskResponses.getSize())
                        .build())
                .build();
    }
}
