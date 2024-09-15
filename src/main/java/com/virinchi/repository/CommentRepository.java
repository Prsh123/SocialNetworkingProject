package com.virinchi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.virinchi.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

	List<Comment> findAllByPostIdOrderByPostTimeDesc(int postId);
}
