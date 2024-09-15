package com.virinchi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.virinchi.model.Likes;

import jakarta.transaction.Transactional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Integer>{
	
	List<Likes> findByPostId(int Post_Id);
	@Transactional
	int deleteByUserIdAndPostId(int UserId, int PostId);
}
