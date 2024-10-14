package org.example.belajar_spring.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.belajar_spring.entity.Task;
import org.example.belajar_spring.entity.User;
import org.example.belajar_spring.model.WebResponse;
import org.example.belajar_spring.model.task.CreateTaskRequest;
import org.example.belajar_spring.model.task.TaskResponse;
import org.example.belajar_spring.model.task.UpdateTaskRequest;
import org.example.belajar_spring.repository.TaskRepository;
import org.example.belajar_spring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("moci");
        user.setName("moci");
        user.setPassword(BCrypt.hashpw("moci", BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000L);
        userRepository.save(user);

    }

    @Test
    void createTaskBadRequest() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();

        request.setTitle("");
        request.setDescription("Ada");
        request.setStatus(true);
        request.setStartDate(LocalDate.of(2024, 10, 15));
        request.setEndDate(LocalDate.of(2024, 10, 20));

        mockMvc.perform(
                post("/api/task")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void createTaskSuccess() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();

        request.setTitle("Mocicaaa");
        request.setDescription("Ada");
        request.setStatus(true);
        request.setStartDate(LocalDate.of(2024, 10, 15));
        request.setEndDate(LocalDate.of(2024, 10, 20));

        mockMvc.perform(
                post("/api/task")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TaskResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals("Mocicaaa", response.getData().getTitle());
            assertEquals("Ada", response.getData().getDescription());
            assertEquals(true, response.getData().getStatus());
            assertEquals(LocalDate.of(2024, 10, 15), response.getData().getStartDate());
            assertEquals(LocalDate.of(2024, 10, 20), response.getData().getEndDate());
        });
    }

    @Test
    void getTaskNotFound() throws Exception {
        mockMvc.perform(
                get("/api/task/1239s231")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void getTaskSuccess() throws Exception {
        User user = userRepository.findById("moci").orElseThrow();

        Task task = new Task();

        task.setId(UUID.randomUUID().toString());
        task.setUser(user);
        task.setTitle("Mocicaaa");
        task.setDescription("Ada");
        task.setStatus(true);
        task.setStartDate(LocalDate.of(2024, 10, 15));
        task.setEndDate(LocalDate.of(2024, 10, 20));

        taskRepository.save(task);

        mockMvc.perform(
                get("/api/task/" + task.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TaskResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(task.getId(), response.getData().getId());
            assertEquals(task.getTitle(), response.getData().getTitle());
            assertEquals(task.getDescription(), response.getData().getDescription());
            assertEquals(task.getStatus(), response.getData().getStatus());
            assertEquals(task.getStartDate(), response.getData().getStartDate());
            assertEquals(task.getEndDate(), response.getData().getEndDate());
        });
    }

    @Test
    void updateTaskBadRequest() throws Exception {
        UpdateTaskRequest request = new UpdateTaskRequest();

        request.setTitle("");
        request.setDescription("Ada");
        request.setStatus(true);
        request.setStartDate(LocalDate.of(2024, 10, 15));
        request.setEndDate(LocalDate.of(2024, 10, 20));

        mockMvc.perform(
                put("/api/task/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void updateTaskSuccess() throws Exception {
        User user = userRepository.findById("moci").orElseThrow();

        Task task = new Task();

        task.setId(UUID.randomUUID().toString());
        task.setUser(user);
        task.setTitle("Mocicaaa");
        task.setDescription("Ada");
        task.setStatus(true);
        task.setStartDate(LocalDate.of(2024, 10, 15));
        task.setEndDate(LocalDate.of(2024, 10, 20));

        taskRepository.save(task);

        CreateTaskRequest request = new CreateTaskRequest();

        request.setTitle("Mocicaaa231");
        request.setDescription("Ada");
        request.setStatus(false);
        request.setStartDate(LocalDate.of(2024, 10, 15));
        request.setEndDate(LocalDate.of(2024, 10, 20));

        mockMvc.perform(
                put("/api/task/" + task.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TaskResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(request.getTitle(), response.getData().getTitle());
            assertEquals(request.getDescription(), response.getData().getDescription());
            assertEquals(request.getStatus(), response.getData().getStatus());
            assertEquals(request.getStartDate(), response.getData().getStartDate());
            assertEquals(request.getEndDate(), response.getData().getEndDate());
        });
    }

    @Test
    void deleteTaskSuccess() throws Exception {
        User user = userRepository.findById("moci").orElseThrow();

        Task task = new Task();

        task.setId(UUID.randomUUID().toString());
        task.setUser(user);
        task.setTitle("Mocicaaa");
        task.setDescription("Ada");
        task.setStatus(true);
        task.setStartDate(LocalDate.of(2024, 10, 15));
        task.setEndDate(LocalDate.of(2024, 10, 20));

        taskRepository.save(task);

        mockMvc.perform(
                delete("/api/task/" + task.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals("OK", response.getData());
        });
    }

    @Test
    void deleteTaskNotFound() throws Exception {
        mockMvc.perform(
                get("/api/task/1239s231")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }
}