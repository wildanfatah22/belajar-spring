package org.example.belajar_spring.service;


import org.example.belajar_spring.entity.User;
import org.example.belajar_spring.model.user.*;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    public void register(RegisterUserRequest request);

    @Transactional
    public TokenResponse login(LoginUserRequest request);

    @Transactional(readOnly = true)
    public UserResponse getUser(User user);

    @Transactional
    public UserResponse updateUser(User user, UpdateUserRequest request);

    @Transactional
    public void logout(User user);

}
