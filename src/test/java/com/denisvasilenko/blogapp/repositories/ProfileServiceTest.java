package com.denisvasilenko.blogapp.repositories;

import com.denisvasilenko.blogapp.DTO.RegistrationDto.UserRegistrationRequest;
import com.denisvasilenko.blogapp.DTO.UserDto.UserInfoUpdateDTO;
import com.denisvasilenko.blogapp.config.PasswordEncoderConfig;
import com.denisvasilenko.blogapp.exceptions.userException.NotFoundUserException;
import com.denisvasilenko.blogapp.exceptions.userException.UserAlreadyExist;
import com.denisvasilenko.blogapp.models.User;
import com.denisvasilenko.blogapp.services.ProfileServices;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Log4j2
public class ProfileServiceTest {

    @Autowired
    private ProfileServices profileServices;

    @Autowired
    private PasswordEncoderConfig passwordEncoderConfig;

    @Test
    public void whenCreateAndSaveUser_thanShouldGetCurrentUserUsingMethodFindByIdAndDeleteThem() {
        UserRegistrationRequest userRequest = new UserRegistrationRequest("testUser", "12345");
        User expectedUser = profileServices.createUser(userRequest);
        User actualUser = profileServices.findUserById(expectedUser.getId());
        Assertions.assertEquals(expectedUser, actualUser);
        deleteTestUser();
    }

    @Test
    public void whenUserNameNotUnique_thanShouldGetNotUniqueUsernameException() {
        UserRegistrationRequest userRequest = new UserRegistrationRequest("testUser", "12345");
        profileServices.createUser(userRequest);
        Assertions.assertThrows(UserAlreadyExist.class, () -> profileServices.createUser(userRequest));
        deleteTestUser();
    }

    @Test
    public void whenUpdateUserChangingPassword_thanShouldGetUpdateUserWithNewPassword() {
        User oldUser = createTestUser();
        String newPassword = "newTestPassword";
        UserInfoUpdateDTO userInfoUpdateDTO = new UserInfoUpdateDTO(newPassword, null, null);
        User actualUser = profileServices.updateUser(oldUser, userInfoUpdateDTO);
        Assertions.assertEquals(oldUser.getId(), actualUser.getId());
        Assertions.assertEquals(oldUser.getUsername(), actualUser.getUsername());
        Assertions.assertNotEquals(oldUser.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(oldUser.getAvatarImg(), actualUser.getAvatarImg());
        Assertions.assertEquals(oldUser.getProfileDescription(), actualUser.getProfileDescription());
        deleteTestUser();
    }

    @Test
    public void whenUpdateUserChangingAvatar_thanShouldGetUpdateUserWithNewAvatar() {
        User oldUser = createTestUser();
        byte[] newAvatar = getImgBytesArray();
        UserInfoUpdateDTO userInfoUpdateDTO = new UserInfoUpdateDTO(null, newAvatar, null);
        User actualUser = profileServices.updateUser(oldUser, userInfoUpdateDTO);
        Assertions.assertEquals(oldUser.getId(), actualUser.getId());
        Assertions.assertEquals(oldUser.getUsername(), actualUser.getUsername());
        Assertions.assertEquals(oldUser.getPassword(), actualUser.getPassword());
        Assertions.assertArrayEquals(newAvatar,actualUser.getAvatarImg());
        Assertions.assertEquals(oldUser.getProfileDescription(), actualUser.getProfileDescription());
        deleteTestUser();
    }

    @Test
    public void whenUpdateUserChangingProfileDescription_thanShouldGetUpdateUserWithNewProfileDescription() {
        User oldUser = createTestUser();
        String newUserDescription = "newUserDescription";
        UserInfoUpdateDTO userInfoUpdateDTO = new UserInfoUpdateDTO(null, null, newUserDescription);
        User actualUser = profileServices.updateUser(oldUser, userInfoUpdateDTO);
        Assertions.assertEquals(oldUser.getId(), actualUser.getId());
        Assertions.assertEquals(oldUser.getUsername(), actualUser.getUsername());
        Assertions.assertEquals(oldUser.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(oldUser.getAvatarImg(),actualUser.getAvatarImg());
        Assertions.assertEquals(newUserDescription,actualUser.getProfileDescription());
        deleteTestUser();
    }

    @Test
    public void whenUseMethodFindUserByUserName_thanShouldGetUserWeAreLookingFor () {
       User expectedUser = createTestUser();
       String expectedUserUsername = expectedUser.getUsername();
       User actualUser = profileServices.findUserByUserName(expectedUserUsername);
       Assertions.assertEquals(expectedUser,actualUser);
       profileServices.deleteUser(actualUser);
    }

    @Test
    public void whenUseMethodFindUserByUserNameWhenUserDoesNotExist_thanShouldGetNotFoundUserException () {
        String usernameThatDoesNotExist = "testUser";
        Assertions.assertThrows(NotFoundUserException.class,() ->profileServices.findUserByUserName(usernameThatDoesNotExist));
    }

    @Test
    public void whenWeDeleteUserByUsername_thanShouldDeleteUserFromDataBase() {
       User expectedDeleteUser = createTestUser();
       String username = expectedDeleteUser.getUsername();
       profileServices.deleteUser(expectedDeleteUser);
       Assertions.assertThrows(NotFoundUserException.class,() -> profileServices.findUserByUserName(username));
    }

    private User createTestUser() {
        String username = "testUser";
        String password = "12345";
        UserRegistrationRequest userRequest = new UserRegistrationRequest(username, password);
        return profileServices.createUser(userRequest);
    }

    private void deleteTestUser() {
        User currentUser = profileServices.findUserByUserName("testUser");
        profileServices.deleteUser(currentUser);
    }
    private byte[] getImgBytesArray() {
        Path path = Paths.get("C:\\Users\\Denis\\Desktop\\898acf6ddef49b89be120c95c7935858.jpg");
        try {
            return Files.readAllBytes(path);
        } catch (IOException ioException){
            return null;
        }
    }
}
