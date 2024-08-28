package com.example.example.service;

import com.example.example.BeanUtils;
import com.example.example.aop.CheckingUserForFindingOrDeleting;
import com.example.example.aop.CheckingUserForUpdating;
import com.example.example.entities.Role;
import com.example.example.entities.User;
import com.example.example.exception.EntityNotFoundException;
import com.example.example.model.Pagination;
import com.example.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll(Pagination page) {
        return userRepository.findAll(PageRequest.of(page.getPageNumber(), page.getPageSize())).getContent();
    }

    @Override
    @CheckingUserForFindingOrDeleting
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден", id)));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с именем {0} не найден", username)));
    }

    @Override
    public User save(User user, Role role) {
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        role.setUser(user);

        return userRepository.saveAndFlush(user);
    }

    @Override
    @CheckingUserForUpdating
    public User update(User user) {
        User existedUser = findById(user.getId());
        BeanUtils.copyNonNullProperties(user, existedUser);
        return userRepository.save(user);
    }

    @Override
    @CheckingUserForFindingOrDeleting
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
