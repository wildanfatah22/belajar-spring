package org.example.belajar_spring.repository;

import org.example.belajar_spring.entity.Task;
import org.example.belajar_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {

    Optional<Task> findFirstByUserAndId(User user, String id);
}
