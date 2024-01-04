package com.denisvasilenko.blogapp.repositories;

import com.amazonaws.services.connect.model.UserNotFoundException;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.services.ProfileServices;
import com.denisvasilenko.blogapp.services.RoleService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProfileServices profileServices;
    @Test
    public void whenCreateAndSaveUser_thanShouldGetCurrentUserUsingMethodFindById() {
        Long userId = 1L;
        String userName = "testUser";
        String password = "$2a$10$YfmxeAy6/ALyJnWHCScmiuQZnYj/KGW9FQCvpXbso91AnYotRTatm";
        String profileDescription = "Test profile description";
        User expectedUser = new User(userId,userName,password,null,profileDescription,null, List.of(roleService.getUserRole()));
        userRepository.save(expectedUser);
        Optional<User> actualUserOptional = userRepository.findById(userId);
        if (actualUserOptional.isPresent()) {
            User actualUser = actualUserOptional.get();
            Assertions.assertEquals(expectedUser,actualUser);
        }
        deleteTestUser();
    }

    @Transactional
    private void deleteTestUser() {
        User currentUser = userRepository.findByUsername("testUser").orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        profileServices.deleteUser(currentUser);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userRepository.findByUsername("testUser").orElseThrow(() -> new UserNotFoundException("User not found"));
        });
    }


}
