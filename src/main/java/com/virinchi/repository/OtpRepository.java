package com.virinchi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.virinchi.model.Otp;

import jakarta.transaction.Transactional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Integer>{
	@Modifying
	@Transactional
	void deleteByEmail(String email);
	
	Otp findByEmail(String email);
}
