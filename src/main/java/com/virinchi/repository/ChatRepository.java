package com.virinchi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.virinchi.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer>{
	@Query("SELECT c FROM Chat c WHERE (c.sender = :sender AND c.receiver = :receiver) OR (c.sender = :receiver AND c.receiver = :sender)")
	List<Chat> findAllBySenderAndReceiverOrReceiverAndSender(@Param("sender") int sender, @Param("receiver") int receiver);
}
