package com.denisvasilenko.BlogApp.repositories;

import com.denisvasilenko.BlogApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUsername(String name);
    public void deleteByUsername(String name);
}
