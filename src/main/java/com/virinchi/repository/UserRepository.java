package com.virinchi.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virinchi.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByEmailAndPassword(String email, String password);
	User findByEmailAndPassword(String email, String password);
	@Transactional
	@Modifying
    @Query("UPDATE User u SET u.avatar = :avatar WHERE u.id = :id")
    void updateAvatarById(int id, String avatar);
	
	@Transactional
	@Modifying
    @Query("UPDATE User u SET u.cover = :cover WHERE u.id = :id")
    void updateCoverById(int id, String cover);
	
	@Transactional
	@Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePasswordById(int id, String password);
	
	@Transactional
	@Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePasswordByEmail(String email, String password);
	
	@Query("SELECT u FROM User u WHERE u.fname LIKE %:keyword% OR u.lname LIKE %:keyword%")
    List<User> searchByKeyword(@Param("keyword") String keyword);
}
