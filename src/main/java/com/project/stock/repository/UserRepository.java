package com.project.stock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.stock.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    
    
    @Query("SELECT u FROM User u WHERE " +
    	       "LOWER(u.firstname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    	       "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    	       "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    	List<User> searchUsers(@Param("keyword") String keyword);
}
