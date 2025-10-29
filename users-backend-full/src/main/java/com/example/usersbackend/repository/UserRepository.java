package com.example.usersbackend.repository;

import com.example.usersbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The UserRepository interface provides methods for interacting with the User entity in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("""
           SELECT u FROM User u
           WHERE LOWER(u.firstName) LIKE %:query%
              OR LOWER(u.lastName) LIKE %:query%
              OR LOWER(u.ssn) LIKE %:query%
           """)
    List<User> freeTextSearch(@Param("query") String query);
}
