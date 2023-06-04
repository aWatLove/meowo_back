package ru.meowo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.meowo.model.Post;
import ru.meowo.model.Role;
import ru.meowo.model.UserTokenInfo;
import ru.meowo.payload.request.LikeRequest;
import ru.meowo.payload.request.LoginRequest;
import ru.meowo.payload.request.UpdateRequest;
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
    @PutMapping("/update")
    public ResponseEntity<?> updatePost(@RequestBody UpdateRequest updateRequest, HttpServletRequest request) {
        UserTokenInfo uti = new UserTokenInfo(jwtUtils.getInfoFromJwtToken(jwtUtils.extractJwtToken(request)));
        service.updateText(updateRequest);
        return ResponseEntity.ok("updated");
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/like")
    public ResponseEntity<?> likePost(@RequestBody String idPost, HttpServletRequest request) {
        UserTokenInfo uti = new UserTokenInfo(jwtUtils.getInfoFromJwtToken(jwtUtils.extractJwtToken(request)));

        if (service.like(idPost, uti.getId())) {
            return ResponseEntity.ok("liked");
        } else return ResponseEntity.ok("unliked");
    }

    // aggregations
    @GetMapping("/mostLikedUser")
    public ResponseEntity<?> getMostLiked() {
        return ResponseEntity.ok(service.mostLikedUsers());
    }

    @GetMapping("/likes/{id}")
    public ResponseEntity<?> getUsersLikes(@PathVariable String id) {
        return ResponseEntity.ok(service.usersLikes(id));
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopPosts() {
        return ResponseEntity.ok(service.topPosts());
    }

    //
    @GetMapping("/all")
    public ResponseEntity<?> getall() {
        return ResponseEntity.ok(service.getAllPost());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getId(@PathVariable String id) {
        return ResponseEntity.ok(service.selectByID(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getAllByName(@PathVariable String name, HttpServletRequest request) {
//        if (request.getHeader("Authorization") != null){
//            System.out.println("авторизация есть");
//        }
        return ResponseEntity.ok(service.selectByName(name));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("deleted");
    }

//    @GetMapping("/user")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    public String userAccess(@RequestBody String str, HttpServletRequest request) {
//        System.out.println(str);
//        String info = jwtUtils.getInfoFromJwtToken(jwtUtils.extractJwtToken(request));
//        System.out.println(info);
//
//        return "User Content.";
//    }
//
//    @GetMapping("/mod")
//    @PreAuthorize("hasRole('MODERATOR')")
//    public String moderatorAccess() {
//        return "Moderator Board.";
//    }
//
//    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String adminAccess() {
//        return "Admin Board.";
//    }
}
