package com.example.example.service;


import com.example.example.entities.User;
import com.example.example.model.Pagination;

import java.util.List;

public interface UserService
{
    List<User> findAll(Pagination page);

    User findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);
}
