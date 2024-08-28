package com.example.example.service;


import com.example.example.entities.Role;
import com.example.example.entities.User;
import com.example.example.model.Pagination;

import java.util.List;

public interface UserService
{
    List<User> findAll(Pagination page);

    User findById(Long id);

    User findByUsername(String username);

    User save(User user, Role role);

    User update(User user);

    void deleteById(Long id);


}
