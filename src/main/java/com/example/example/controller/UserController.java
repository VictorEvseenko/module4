package com.example.example.controller;

import com.example.example.entities.Role;
import com.example.example.entities.RoleType;
import com.example.example.exception.AccessDeniedException;
import com.example.example.mapper.UserMapper;
import com.example.example.entities.User;
import com.example.example.model.*;
import com.example.example.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController
{
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserListResponse> findAll(Pagination page) {
        return ResponseEntity.ok(userMapper.userListToUserResponseList(userService.findAll(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(userMapper.userToResponse(userService.findById(id)));
        } catch(AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UpsertUserRequest request, @RequestParam RoleType roleType) {
        User newUser = userService.save(userMapper.requestToUser(request), Role.from(roleType));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponse(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") Long userId, @RequestBody UpsertUserRequest request) {
        try {
            User updatedUser = userService.update(userMapper.requestToUser(userId, request));
            return ResponseEntity.ok(userMapper.userToResponse(updatedUser));
        } catch(AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch(AccessDeniedException ex) {
            ErrorResponse err = new ErrorResponse("Отказано в доступе!");
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }
}