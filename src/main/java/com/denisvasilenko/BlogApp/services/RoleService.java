package com.denisvasilenko.BlogApp.services;

import com.denisvasilenko.BlogApp.models.Role;
import com.denisvasilenko.BlogApp.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Role getUserRole(){return roleRepository.findByName("ROLE_USER").get();}
}
