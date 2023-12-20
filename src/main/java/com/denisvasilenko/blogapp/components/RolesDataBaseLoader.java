package com.denisvasilenko.blogapp.components;

import com.denisvasilenko.blogapp.models.Role;
import com.denisvasilenko.blogapp.repositories.RoleRepository;
import com.denisvasilenko.blogapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RolesDataBaseLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    @Autowired
    public RolesDataBaseLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.count()==0) {
            roleRepository.save(new Role(1,"ROLE_USER"));
            roleRepository.save(new Role(2,"ROLE_ADMIN"));
        }
    }
}
