package org.example.belajar_spring.service.impl;

import jakarta.persistence.criteria.Predicate;
import org.example.belajar_spring.entity.Task;
import org.example.belajar_spring.entity.User;
import org.example.belajar_spring.model.task.CreateTaskRequest;
import org.example.belajar_spring.model.task.SearchTaskRequest;
import org.example.belajar_spring.model.task.TaskResponse;
import org.example.belajar_spring.model.task.UpdateTaskRequest;
import org.example.belajar_spring.repository.TaskRepository;
import org.example.belajar_spring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskResponse createTask(User user, CreateTaskRequest request) {
        validationService.validate(request);

        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setUser(user);

        validateDates(request.getStartDate(), request.getEndDate());

        taskRepository.save(task);

        return toTaskResponse(task);
    }

    @Override
    public TaskResponse updateTask(User user, UpdateTaskRequest request) {
        validationService.validate(request);

        Task task = taskRepository.findFirstByUserAndId(user, request.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());

        validateDates(request.getStartDate(), request.getEndDate());

        taskRepository.save(task);

        return toTaskResponse(task);
    }

    private TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .build();
    }

    @Override
    public TaskResponse getTask(User user, String id) {
        Task task = taskRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        return toTaskResponse(task);
    }

    @Override
    public void delete(User user, String taskId) {
        Task task = taskRepository.findFirstByUserAndId(user, taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        taskRepository.delete(task);
    }

    @Override
    public Page<TaskResponse> searchTask(User user, SearchTaskRequest request) {
        Specification<Task> specification = (root, query, builder) -> {
            // karena query dinamis, jadi disimpan dalam list
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if (Objects.nonNull(request.getTitle())) {
                predicates.add(builder.like(root.get("title"), "%" + request.getTitle() + "%"
                ));
            }
            if (Objects.nonNull(request.getStartDate())) {
                predicates.add(builder.like(root.get("startDate"), "%" + request.getStartDate() + "%"
                ));
            }
            if (Objects.nonNull(request.getEndDate())) {
                predicates.add(builder.like(root.get("endDate"), "%" + request.getEndDate() + "%"
                ));
            }
            if (Objects.nonNull(request.getStatus())) {
                predicates.add(builder.like(root.get("status"), "%" + request.getStatus() + "%"
                ));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Task> tasks = taskRepository.findAll(specification, pageable);
        List<TaskResponse> taskResponses = tasks.getContent()
                .stream()
                .map(this::toTaskResponse)
                .toList();

        // PageImpl karena bukan interface
        return new PageImpl<>(taskResponses, pageable, tasks.getTotalElements());
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = LocalDate.now();

        // Validasi jika startDate lebih besar daripada endDate
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        // Validasi jika startDate atau endDate berada di masa lampau
        if (startDate.isBefore(currentDate) || endDate.isBefore(currentDate)) {
            throw new IllegalArgumentException("Start date and end date cannot be in the past.");
        }
    }
}
