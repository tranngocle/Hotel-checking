package com.lake.lakeSidehotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lake.lakeSidehotel.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String role);


    boolean existsByName(String role);
}