package com.denisvasilenko.blogapp.controlers;

import com.denisvasilenko.blogapp.DTO.UserDto.ResetPasswordDTO;
import com.denisvasilenko.blogapp.DTO.UserDto.UserInfoDto;
import com.denisvasilenko.blogapp.DTO.UserDto.UserInfoUpdateDTO;
import com.denisvasilenko.blogapp.exceptions.ExceptionDto;
import com.denisvasilenko.blogapp.exceptions.userException.NotFoundUserException;
import com.denisvasilenko.blogapp.exceptions.userException.ResetPasswordException;
import com.denisvasilenko.blogapp.exceptions.userException.UserAlreadyExistException;
import com.denisvasilenko.blogapp.exceptions.userException.UserDeletionException;
import com.denisvasilenko.blogapp.services.ProfileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/user")
@RestController
public class UserController {
   private final ProfileServices profileServices;
    @Autowired
    public UserController(ProfileServices profileServices) {
        this.profileServices = profileServices;
    }

    @GetMapping("/{userName}")
    public ResponseEntity <UserInfoDto> userInfoForAnotherUser(@PathVariable ("userName") String userName) {
        UserInfoDto userInfo = profileServices.userInfo(userName);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity <UserInfoDto> userInfo(Principal principal) {
        UserInfoDto userInfo = profileServices.userInfo(principal.getName());
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @PatchMapping("/info")
    public ResponseEntity<String> updateUser(Principal principal, @RequestBody UserInfoUpdateDTO userInfoUpdateDTO) {
        profileServices.updateUser(principal.getName(), userInfoUpdateDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/info")
    public ResponseEntity<String> deleteUser(Principal principal) {
        profileServices.deleteUserByUsername(principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(Principal principal, @RequestBody ResetPasswordDTO resetPasswordDTO){
        profileServices.resetPassword(principal.getName(),resetPasswordDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ExceptionDto> ResetPasswordException(ResetPasswordException exception){
        ExceptionDto response = new ExceptionDto(exception.getMessage(),
                exception.getTimestamp());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ExceptionDto> notFoundUserExceptionResponseEntity(NotFoundUserException exception){
        ExceptionDto response = new ExceptionDto(exception.getMessage(),
                exception.getTimestamp());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ExceptionDto> userDeletionExceptionResponseEntity(UserDeletionException exception){
        ExceptionDto response = new ExceptionDto(exception.getMessage(),
                exception.getTimestamp());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
