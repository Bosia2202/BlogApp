package com.denisvasilenko.blogapp.services;

import com.denisvasilenko.blogapp.models.Role;
import com.denisvasilenko.blogapp.repositories.RoleRepository;
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
