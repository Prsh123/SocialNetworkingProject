package com.virinchi.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virinchi.model.Friend_Request;

import jakarta.transaction.Transactional;


@Repository
public interface FriendRequestRepository extends JpaRepository<Friend_Request, Integer>{
	@Query("SELECT fr.requester FROM Friend_Request fr WHERE fr.receiver = :requestId")
    List<Integer> findRequesterByReceiver(@Param("requestId") Integer requestId);
	
	@Query("SELECT fr.receiver FROM Friend_Request fr WHERE fr.requester = :requestId")
    List<Integer> findReceiverByRequester(@Param("requestId") Integer requestId);
	@Transactional
	void deleteByRequesterAndReceiver(int requesterId, int User_id);
}
