package com.ETicaretDB_restAPI.restAPI.controller;

import com.ETicaretDB_restAPI.restAPI.model.entity.User;
import com.ETicaretDB_restAPI.restAPI.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable("username") String userName) {
        return userService.getUserByUsername(userName);
    }

    @PostMapping("/update")
    public boolean updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/authorize/{username}/{password}")
    public User checkUser(@PathVariable("username") String username, @PathVariable("password") String password) {
        return userService.checkUser(username, password);
    }

    @GetMapping("/checkPassword/{username}/{password}")
    public boolean checkUserPassword(@PathVariable("username") String username, @PathVariable("password") String password){
        return userService.checkUserPassword(username, password);
    }

    @GetMapping("/checkUsernameInUse/{username}")
    public boolean usernameAlreadyInUse(@PathVariable("username") String username){
        return userService.usernameAlreadyInUse(username);
    }

    @PostMapping
    public boolean addUser(@RequestBody String userJson) {
        return userService.addUser(userJson);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") int userId) {
        userService.deleteUserById(userId);
    }
}
