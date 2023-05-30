package ru.meowo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    /* crud */
    @GetMapping("/")
    public ResponseEntity<?> getPost(){
        return ResponseEntity.ok("getPost");
    }

    @PostMapping("/")
    public ResponseEntity<?> postPost(){
        return ResponseEntity.ok("postPost");
    }

    @PutMapping("/")
    public ResponseEntity<?> updatePost(){
        return ResponseEntity.ok("updatePost");
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deletePost(){
        return ResponseEntity.ok("deletePost");
    }

    /* reactions */
    @GetMapping("/like")
    public ResponseEntity<?> like(){
        return ResponseEntity.ok("like");
    }

    @GetMapping("/unlike")
    public ResponseEntity<?> unlike(){
        return ResponseEntity.ok("unlike");
    }

    @GetMapping("/complain")
    public ResponseEntity<?> complain(){
        return ResponseEntity.ok("complain");
    }

    /* aggregation functions */
    // популярные посты
    @GetMapping("/top")
    public ResponseEntity<?> getTop(){
        return ResponseEntity.ok("getTopPosts");
    }

    // свежие посты
    @GetMapping("/fresh")
    public ResponseEntity<?> getFresh(){
        return ResponseEntity.ok("getFreshPosts");
    }

    // посты с жалобами
    @GetMapping("/complained")
    public ResponseEntity<?> getComplained(){
        return ResponseEntity.ok("getComplainedPosts");
    }

    // лайкнутые посты
    @GetMapping("/liked")
    public ResponseEntity<?> get(){
        return ResponseEntity.ok("getLikedPosts");
    }

}
