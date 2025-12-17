package com.ETicaretDB_restAPI.restAPI.service;

import com.ETicaretDB_restAPI.restAPI.model.entity.User;
import com.ETicaretDB_restAPI.restAPI.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(int userId) {
        return userRepository.getUserById(userId);
    }

    public User getUserByUsername(String userName) {
        return userRepository.getUserByUsername(userName);
    }

    public boolean updateUser(User user) {
        System.out.println("user.getUserID() = "+user.getUserID());
        System.out.println("user.getUserName() = "+user.getUserName());
        System.out.println("user.getEmail() = "+user.getEmail());
        System.out.println("user.getPassword() = "+user.getPassword());
        int affected = userRepository.updateUser(
                user.getUserID(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword());

        return affected > 0;
    }

    public User checkUser(String username, String password) {
        User user = userRepository.getUserByUsername(username);

        if(user == null){
            return null;
        }

        if(user.getPassword().equals(password)){
            return user;
        }
        else{
            return null;
        }
    }

    public boolean checkUserPassword(String username, String password){
        User user = userRepository.getUserByUsername(username);

        if(user.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    public boolean usernameAlreadyInUse(String username){
        if(userRepository.countByUserName(username) > 0){
            return true;
        }
        return false;
    }

    public boolean addUser(String userJson) {

        JSONObject json = new JSONObject(userJson);

        String username = json.getString("username");
        String email = json.getString("email");
        String password = json.getString("password");

        if (userRepository.countByUserName(username) > 0) {
            return false; // kullanıcı adı zaten var
        }

        if (userRepository.countByEmail(email) > 0) {
            return false; // email zaten var
        }

        int affected = userRepository.addUser(
                username,
                email,
                password
        );

        return affected > 0;
    }

    public boolean deleteUserById(int userId) {
        return userRepository.deleteUserById(userId) > 0;
    }
}
