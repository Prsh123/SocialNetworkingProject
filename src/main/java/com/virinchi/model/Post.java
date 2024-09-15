package com.virinchi.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private int User_id;
    private String Content;
    @Lob
	@Column(columnDefinition = "MEDIUMBLOB")
    private String Image;
    private LocalDateTime Post_time;
    @Transient
    private int likes;
    @Transient
    private boolean liked;

    // Define relationship to User (assuming User is another entity)
    @Transient
    private Optional<User> poster;

    // Transient annotation to skip commentList from database persistence
    @Transient
    private List<Comment> commentList;

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setPost_content(String post_content) {
        this.Content = post_content;
    }

    public LocalDateTime getPost_time() {
        return Post_time;
    }

    public void setPost_time(LocalDateTime post_time) {
        this.Post_time = post_time;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String content_image) {
        this.Image = content_image;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Optional<User> getPoster() {
        return poster;
    }

    public void setPoster(Optional<User> poster) {
        this.poster = poster;
    }

    @Transient
    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

	public int getUser_id() {
		return User_id;
	}

	public void setUser_id(int user_id) {
		User_id = user_id;
	}
}