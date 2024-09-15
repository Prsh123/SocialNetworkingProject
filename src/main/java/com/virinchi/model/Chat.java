package com.virinchi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Chat {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int sender;
	private int receiver;
	private String message;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String image;
	private LocalDateTime post_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSender() {
		return sender;
	}
	public void setSender(int sender) {
		this.sender = sender;
	}
	public int getReceiver() {
		return receiver;
	}
	public void setReciever(int reciever) {
		this.receiver = reciever;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public LocalDateTime getPost_time() {
		return post_time;
	}
	public void setPost_time(LocalDateTime post_time) {
		this.post_time = post_time;
	} 
}
