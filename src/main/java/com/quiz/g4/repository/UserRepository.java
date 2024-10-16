package com.quiz.g4.repository;


import com.quiz.g4.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, CrudRepository<User, Integer> {
    User findByEmail(String email);

    User findUserByUserId(Integer userId);

    List<User> findByRoleRoleId(int roleId);

    Page<User> findByRoleRoleId(int roleId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (:expertName IS NULL OR u.fullName LIKE %:expertName%)" +
            " AND (:subjectId IS NULL OR u.subject.subjectId = :subjectId)" +
            " AND (:roleId IS NULL OR u.role.roleId = :roleId)")
    Page<User> searchExpert(String expertName, Integer subjectId, Integer roleId, Pageable pageable);

    List<User> findByRole(String role);

    @Query("SELECT u FROM User u WHERE u.role.roleName != 'ROLE_ADMIN'")
    List<User> findAllExceptRoles();

    @Query("SELECT COUNT(u) FROM User u")
    long countTotalUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = false")
    long countInactiveUsers();
}

