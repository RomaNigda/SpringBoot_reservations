package org.example.springtest1.users.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.springtest1.users.db.Roles;
import org.example.springtest1.users.db.UserEntity;
import org.example.springtest1.users.db.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
public class UserRolesService {
    private final UserRepository repository;

    public UserRolesService(UserRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public void addRoleToUser(@AuthenticationPrincipal UserEntity user, Roles role){
        UserEntity userEntity = repository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userEntity.getRoles().add(role);
        repository.save(userEntity);
    }
}
