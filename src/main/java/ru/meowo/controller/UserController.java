package ru.meowo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/register")
    public ResponseEntity<?> register(){
        return ResponseEntity.ok("register");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(){
        return ResponseEntity.ok("login");
    }

    // User
    @GetMapping("/")
    public ResponseEntity<?> getUser(){
        return ResponseEntity.ok("get user");
    }

    @PutMapping("/")
    public ResponseEntity<?> updateUser(){
        return ResponseEntity.ok("get user");
    }

    /* Follows */
    @GetMapping("/follow")
    public ResponseEntity<?> follow(){
        return ResponseEntity.ok("follow");
    }

    @GetMapping("/unfollow")
    public ResponseEntity<?> unfollow(){
        return ResponseEntity.ok("unfollow");
    }

    @GetMapping("/getFollows")
    public ResponseEntity<?> getFollows(){
        return ResponseEntity.ok("getFollows");
    }

    @GetMapping("/getFollowers")
    public ResponseEntity<?> getFollowers(){
        return ResponseEntity.ok("getFollowers");
    }

}
