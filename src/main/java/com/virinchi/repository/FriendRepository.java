package com.virinchi.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.virinchi.model.Friends;
import com.virinchi.model.User;

import jakarta.transaction.Transactional;


public interface FriendRepository extends JpaRepository<Friends, Integer>{

	 @Query("SELECT u FROM User u WHERE u.id != :userId AND u.id NOT IN :friendIds AND u.id NOT IN :requestedIds AND u.id NOT IN :requesterIds")
	    List<User> findAvailableUsers(
	        @Param("userId") Integer UserId,
	        @Param("friendIds") List<Integer> friendIds,
	        @Param("requestedIds") List<Integer> requestedIds,
	        @Param("requesterIds") List<Integer> requesterIds
	    );
	 @Query("SELECT f.friendId FROM Friends f WHERE f.userId = :userId")
	 List<Integer> findFriendIdByUserId(@Param("userId") Integer userId);
	 
	@Query("SELECT u FROM User u WHERE u.id != :userId AND u.id IN :friendIds")
	    
	List<User> findById(@Param("friendIds") List<Integer> friendIds,Integer userId);
	

	@Query("SELECT f1.friendId FROM Friends f1 " +
	           "WHERE f1.userId = :userId AND f1.friendId IN " +
	           "(SELECT f2.friendId FROM Friends f2 WHERE f2.userId = :friendId) " +
	           "AND f1.friendId != :userId")
	List<Integer> findMutualFriendIdById(@Param("userId") int userId, @Param("friendId") int friendId);
	
	@Query("SELECT u FROM User u WHERE u.id != :userId AND u.id != :friendId AND u.id IN :friendIds")
    
	List<User> findMutualsById(@Param("friendIds") List<Integer> friendIds,@Param("userId")Integer userId,@Param("friendId") Integer friendId);
	
	 @Modifying
	    @Transactional
	    @Query("DELETE FROM Friends f WHERE f.friendId = :friendId AND f.userId = :userId")
	    void deleteByFriendIdAndUserId(@Param("friendId") int friendId, @Param("userId") int userId);
}
