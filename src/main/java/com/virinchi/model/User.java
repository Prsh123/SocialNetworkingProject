package com.virinchi.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;

@Entity
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	 @Column(unique = true, nullable = false)
	private String email;
	private String fname;
	private String lname;
	private String username;
	private String password;
	private LocalDate Birthdate;
	private String Gender;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String avatar;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String cover;
	@Transient
	private int friend_no;
	@Transient
	private int mutual_no;
	@Transient
    private String friend;

	public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public int getFriend_no() {
		return friend_no;
	}
	public void setFriend_no(int friend_no) {
		this.friend_no = friend_no;
	}
	public int getMutual_no() {
		return mutual_no;
	}
	public void setMutual_no(int mutual_no) {
		this.mutual_no = mutual_no;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String passowrd) {
		this.password = passowrd;
	}
	public String getGender() {
		return Gender;
	}
	public void setGender(String sex) {
		this.Gender = sex;
	}
	public LocalDate getBirthdate() {
		return Birthdate;
	}
	public void setBirthdate(LocalDate birthDate) {
		this.Birthdate = birthDate;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
