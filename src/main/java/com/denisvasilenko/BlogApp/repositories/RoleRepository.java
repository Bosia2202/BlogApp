package com.denisvasilenko.BlogApp.repositories;

import com.denisvasilenko.BlogApp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);
}
