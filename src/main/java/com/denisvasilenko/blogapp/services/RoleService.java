package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.exceptions.RoleDoesNotExistException;
import com.denisvasilenko.blogapp.models.Role;
import com.denisvasilenko.blogapp.repositories.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Role getUserRole() {
        try {
        return roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RoleDoesNotExistException("ROLE_USER"));
        }
        catch (RoleDoesNotExistException roleDoesNotExistException) {
         log.error("CRITICAL ERROR!!!!!", roleDoesNotExistException);
         System.exit(1);
         return null;
        }
    }
}
