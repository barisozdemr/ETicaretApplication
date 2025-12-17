package com.ETicaretDB_restAPI.restAPI.service;

import com.ETicaretDB_restAPI.restAPI.model.dto.CommentAddDTO;
import com.ETicaretDB_restAPI.restAPI.model.dto.RatingDTO;
import com.ETicaretDB_restAPI.restAPI.model.entity.UserComment;
import com.ETicaretDB_restAPI.restAPI.repository.UserCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCommentService {

    private final UserCommentRepository userCommentRepository;

    public UserCommentService(UserCommentRepository userCommentRepository) {
        this.userCommentRepository = userCommentRepository;
    }

    public UserComment getCommentById(int userId, int productId) {
        return userCommentRepository.getCommentById(userId, productId);
    }

    public List<UserComment> getAllCommentsByProductId(int productId) {
        return userCommentRepository.getAllCommentsByProductId(productId);
    }

    public RatingDTO getRatingByProductId(int productId) {
        return userCommentRepository.getRatingByProductId(productId);
    }

    public boolean addComment(CommentAddDTO comment){
        System.out.println("comment.getUserId()= "+comment.getUserId());
        System.out.println("comment.getProductId()= "+comment.getProductId());
        System.out.println("comment.getRating()= "+comment.getRating());
        System.out.println("comment.getComment()= "+comment.getComment());

        int affected = userCommentRepository.addComment(
                comment.getUserId(),
                comment.getProductId(),
                comment.getRating(),
                comment.getComment()
        );

        return affected > 0;
    }

    public boolean deleteCommentById(int userId, int productId) {
        return 0 < userCommentRepository.deleteCommentById(userId, productId);
    }
}
