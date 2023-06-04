package ru.meowo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.meowo.model.Post;
import ru.meowo.model.UserTokenInfo;
import ru.meowo.payload.request.UpdateRequest;
import ru.meowo.security.jwt.JwtUtils;
import ru.meowo.service.post.PostServiceImp;

import java.util.Date;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PostServiceImp service;

    /* crud */
    @GetMapping("/user/{name}")
    public ResponseEntity<?> getAllPostsByName(@PathVariable String name) {
        return ResponseEntity.ok(service.selectByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable String id) {
        return ResponseEntity.ok(service.selectByID(id));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/")
    public ResponseEntity<?> postPost(@RequestBody String textPost, HttpServletRequest request) {
        UserTokenInfo uti = new UserTokenInfo(jwtUtils.getInfoFromJwtToken(jwtUtils.extractJwtToken(request)));

        service.insert(new Post(uti.getId(), uti.getUsername(), new Date(), textPost));
        return ResponseEntity.ok("posted");
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/")
    public ResponseEntity<?> updatePost(@RequestBody UpdateRequest updateRequest, HttpServletRequest request) {
        service.updateText(updateRequest);
        return ResponseEntity.ok("updated");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("deleted");
    }


    /* reactions */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/like/{idPost}")
    public ResponseEntity<?> likePost(@PathVariable String idPost, HttpServletRequest request) {
        UserTokenInfo uti = new UserTokenInfo(jwtUtils.getInfoFromJwtToken(jwtUtils.extractJwtToken(request)));

        if (service.like(idPost, uti.getId())) {
            return ResponseEntity.ok("liked");
        } else return ResponseEntity.ok("unliked");
    }

/*
    @GetMapping("/complain")
    public ResponseEntity<?> complain(){
        return ResponseEntity.ok("complain");
    }
 */

    /* aggregation functions */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/likes/{id}")
    public ResponseEntity<?> getUsersLikes(@PathVariable String id) {
        return ResponseEntity.ok(service.usersLikes(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/likes/top")
    public ResponseEntity<?> getMostLiked() {
        return ResponseEntity.ok(service.mostLikedUsers());
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopPosts() {
        return ResponseEntity.ok(service.topPosts());
    }
}
