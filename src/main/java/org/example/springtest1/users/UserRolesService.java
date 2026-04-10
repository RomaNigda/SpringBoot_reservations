package org.example.springtest1.users;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.springtest1.Roles;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;

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
