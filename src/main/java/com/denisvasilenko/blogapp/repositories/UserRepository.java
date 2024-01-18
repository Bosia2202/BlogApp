package com.denisvasilenko.blogapp.repositories;

import com.denisvasilenko.blogapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findByUsername(String username);

  List<User> findAllUsersByUsernameContainingIgnoreCase(String username);
  void deleteByUsername(String name);
}
