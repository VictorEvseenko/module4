package com.example.example.controller;

import com.example.example.entities.Comment;
import com.example.example.exception.AccessDeniedException;
import com.example.example.mapper.CommentMapper;
import com.example.example.model.*;
import com.example.example.service.CommentServiceImpl;
import com.example.example.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController
{
    private final CommentServiceImpl commentService;
    private final CommentMapper commentMapper;
    private final UserServiceImpl userService;

    @GetMapping("/news")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<CommentListResponse> findAllByNewsId(@RequestParam Long newsId) {
        CommentFilter filter = new CommentFilter();
        filter.setNewsId(newsId);
        return ResponseEntity.ok(commentMapper.commentListToCommentListResponse(commentService.findAllByNewsId(filter)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentMapper.commentToResponse(commentService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<CommentResponse> create(@RequestBody @Valid UpsertCommentRequest request) {
        Comment comment = commentService.save(commentMapper.requestToComment(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.commentToResponse(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable("id") Long commentId, @RequestBody UpsertCommentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            request.setUserId(userService.findByUsername(userDetails.getUsername()).getId());
            Comment updatedComment = commentService.update(commentMapper.requestToComment(commentId, request));
            return ResponseEntity.ok(commentMapper.commentToResponse(updatedComment));
        } catch (AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            commentService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }
}
