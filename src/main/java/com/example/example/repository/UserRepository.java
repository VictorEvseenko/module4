package com.example.example.repository;

import com.example.example.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsername(String username);
}
