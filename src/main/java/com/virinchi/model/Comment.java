package com.virinchi.model;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Comment {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String Content;

	private LocalDateTime postTime; 

	private int userId;  
	private int postId;  
	public LocalDateTime getPostTime() {
		return postTime;
	}
	public void setPostTime(LocalDateTime postTime) {
		this.postTime = postTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}

	@Transient
	private Optional<User> Commenter;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		this.Content = content;
	}

	public Optional<User> getCommenter() {
		return Commenter;
	}
	public void setCommenter(Optional<User> commenter) {
		Commenter = commenter;
	}

	
}
