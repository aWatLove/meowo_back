package ru.meowo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.meowo.model.Post;
import ru.meowo.model.Role;
import ru.meowo.model.UserTokenInfo;
import ru.meowo.payload.request.LoginRequest;
import ru.meowo.repository.PostRepository;
import ru.meowo.security.jwt.JwtUtils;
import ru.meowo.service.post.PostServiceImp;

import java.util.Date;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PostServiceImp service;

    @Autowired
    PostRepository repository;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/post")
    public ResponseEntity<?> post(@RequestBody String textPost, HttpServletRequest request) {
        UserTokenInfo uti = new UserTokenInfo(jwtUtils.getInfoFromJwtToken(jwtUtils.extractJwtToken(request)));

        service.insert(new Post(uti.getId(), uti.getUsername(), new Date(), textPost));
        return ResponseEntity.ok("inserted");
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update")
    public ResponseEntity<?> updatePost(@RequestBody String id, String newText, HttpServletRequest request) {
        UserTokenInfo uti = new UserTokenInfo(jwtUtils.getInfoFromJwtToken(jwtUtils.extractJwtToken(request)));
        System.out.println(id);
        System.out.println(newText);
        Post post = repository.findPostById(id).orElse(null);
        System.out.println(post);
        post.setText(newText);
        repository.updatePostByAndId(uti.getId(), post);

        return ResponseEntity.ok(repository.findPostById(id).orElse(null));
    }


    @GetMapping("/all")
    public ResponseEntity<?> getall() {
        return ResponseEntity.ok(service.getAllPost());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getId(@PathVariable String id){
        return ResponseEntity.ok(service.selectByID(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getAllByName(@PathVariable String name, HttpServletRequest request){
//        if (request.getHeader("Authorization") != null){
//            System.out.println("авторизация есть");
//        }
        return ResponseEntity.ok(service.selectByName(name));
    }



    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess(@RequestBody String str, HttpServletRequest request) {
        System.out.println(str);
        String info = jwtUtils.getInfoFromJwtToken(jwtUtils.extractJwtToken(request));
        System.out.println(info);

        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
