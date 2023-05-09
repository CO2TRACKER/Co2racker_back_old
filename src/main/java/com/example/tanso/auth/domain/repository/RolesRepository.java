package com.example.tanso.auth.domain.repository;

import com.example.tanso.auth.domain.model.Roles;
import com.example.tanso.auth.roles.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRole(ERole role);
}
