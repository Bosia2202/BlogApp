package com.denisvasilenko.BlogApp.repositories;

import com.denisvasilenko.BlogApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findByUsername(String name);
    public void deleteByUsername(String name);
}
