package com.example.example.controller;

import com.example.example.entities.Comment;
import com.example.example.exception.AccessDeniedException;
import com.example.example.mapper.CommentMapper;
import com.example.example.model.*;
import com.example.example.service.CommentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController
{
    private final CommentServiceImpl commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/news")
    public ResponseEntity<CommentListResponse> findAllByNewsId(@RequestParam Long newsId) {
        CommentFilter filter = new CommentFilter();
        filter.setNewsId(newsId);
        return ResponseEntity.ok(commentMapper.commentListToCommentListResponse(commentService.findAllByNewsId(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentMapper.commentToResponse(commentService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody @Valid UpsertCommentRequest request) {
        Comment comment = commentService.save(commentMapper.requestToComment(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.commentToResponse(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable("id") Long commentId, @RequestBody UpsertCommentRequest request) {
        try {
            Comment updatedComment = commentService.update(commentMapper.requestToComment(commentId, request));
            return ResponseEntity.ok(commentMapper.commentToResponse(updatedComment));
        } catch (AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestBody DeletingCommentRequest request) {
        try {
            commentService.deleteById(id, commentMapper.requestToCommentForDeleting(id, request));
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }
}
