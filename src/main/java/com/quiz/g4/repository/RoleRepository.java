package com.quiz.g4.repository;

import com.quiz.g4.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT r FROM Role r WHERE r.roleName IN ('ROLE_MARKETING', 'ROLE_EXPERT')")
    List<Role> findRolesForUserCreation();
    @Query("SELECT r FROM Role r WHERE r.roleName = :name")
    Optional<Role> findByName(@Param("name") String name);
    Role findRoleByRoleId(Integer roleId);
}
