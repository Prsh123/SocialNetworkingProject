package com.virinchi.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Friend_Request {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int requester;
	private int receiver;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRequester() {
		return requester;
	}
	public void setRequester(int requester) {
		this.requester = requester;
	}
	public int getReceiver() {
		return receiver;
	}
	public void setTo(int receiver) {
		this.receiver = receiver;
	}
	
}
