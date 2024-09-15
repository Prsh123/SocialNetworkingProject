package com.virinchi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virinchi.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
	 @Query("SELECT p FROM Post p WHERE p.User_id = :userId OR p.User_id IN " +
	           "(SELECT f.friendId FROM Friends f WHERE f.userId = :userId) " +
	           "ORDER BY p.Post_time DESC") 
	    List<Post> findAllByUserAndFriends(@Param("userId") int userId);
	 
	 @Query("SELECT p FROM Post p WHERE p.User_id = :userId ORDER BY p.Post_time DESC") 
	 List<Post> findAllByUser(@Param("userId")  int userId);
	  
}

