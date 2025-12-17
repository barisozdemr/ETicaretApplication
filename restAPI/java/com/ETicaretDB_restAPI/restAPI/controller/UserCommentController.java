package com.ETicaretDB_restAPI.restAPI.controller;

import com.ETicaretDB_restAPI.restAPI.model.dto.CommentAddDTO;
import com.ETicaretDB_restAPI.restAPI.model.dto.RatingDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserComment;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserCommentKey;
import com.ETicaretDB_restAPI.restAPI.service.UserCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class UserCommentController {

    private final UserCommentService userCommentService;

    public UserCommentController(UserCommentService service) {
        this.userCommentService = service;
    }

    @GetMapping("/{userId}/{productId}")
    public UserComment getCommentById(@PathVariable int userId, @PathVariable int productId) {
        return userCommentService.getCommentById(userId, productId);
    }

    @GetMapping("/{productId}")
    public List<UserComment> getAllCommentsByProductId(@PathVariable int productId) {
        List<UserComment> comments = userCommentService.getAllCommentsByProductId(productId);

        return comments;
    }

    @GetMapping("/rating/{productId}")
    public RatingDTO getRatingByProductId(@PathVariable int productId) {
        return userCommentService.getRatingByProductId(productId);
    }


    @PostMapping
    public boolean addComment(@RequestBody CommentAddDTO comment) {
        return userCommentService.addComment(comment);
    }

    @DeleteMapping("/{userId}/{productId}")
    public boolean deleteById(@PathVariable int userId, @PathVariable int productId) {
        return userCommentService.deleteCommentById(userId, productId);
    }
}
