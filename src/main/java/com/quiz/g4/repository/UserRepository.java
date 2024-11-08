package com.quiz.g4.repository;


import com.quiz.g4.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, CrudRepository<User, Integer> {
    User findByEmail(String email);

    User findUserByUserId(Integer userId);

    List<User> findByRoleRoleId(int roleId);

    Page<User> findByRoleRoleId(int roleId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (:expertName IS NULL OR u.fullName LIKE %:expertName%)" +
            " AND (:roleId IS NULL OR u.role.roleId = :roleId)" +
            " AND (u.isActive = true)")
    Page<User> searchExpert(String expertName, Integer roleId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role.roleName = :role")
    List<User> findByRole(@Param("role") String role);

    @Query("SELECT u FROM User u WHERE u.role.roleName NOT IN ('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    List<User> findAllExceptRoles();

    @Query("SELECT COUNT(u) FROM User u")
    long countTotalUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = false")
    long countInactiveUsers();

    //    @Query("SELECT u FROM User u WHERE u.role.roleName = :role")
//    Page<User> findByRole(@Param("role") String role, Pageable pageable);
//
//    @Query("SELECT u FROM User u WHERE u.role.roleName NOT IN :roles")
//    Page<User> findAllExceptRoles(Pageable pageable, String... roles);
    @Query("SELECT u FROM User u WHERE u.role.roleName = :roleName")
    Page<User> findByRoleRoleName(@Param("roleName") String roleName, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role.roleName = :roleName")
    long countByRoleRoleName(@Param("roleName") String roleName);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role.roleName = :roleName AND u.isActive = :isActive")
    long countByRoleRoleNameAndIsActive(@Param("roleName") String roleName, @Param("isActive") boolean isActive);
}

