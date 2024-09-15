package com.virinchi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.virinchi.model.User;
import com.virinchi.repository.ChatRepository;
import com.virinchi.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.virinchi.model.Chat;

@Controller
public class ChatController {

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private ChatRepository cRepo;

    @Autowired
    private ObjectMapper objectMapper;
 
    @GetMapping("/chat")
    public ResponseEntity<?> getChatHistory(@RequestParam("friend_id") String friendId, HttpServletRequest req) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        List<Chat> chatHistory = cRepo.findAllBySenderAndReceiverOrReceiverAndSender(user.getId(), Integer.parseInt(friendId));
        Optional<User> chatF = uRepo.findById(Integer.parseInt(friendId));System.out.println(chatF.get().getFname());
        User chatFriend = new User();
        chatFriend.setId(chatF.get().getId());
        chatFriend.setAvatar(chatF.get().getAvatar());
        chatFriend.setFname(chatF.get().getFname());
        chatFriend.setLname(chatF.get().getLname());
        
        
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.set("chatHistory", objectMapper.valueToTree(chatHistory));
        jsonResponse.set("chatFriend", objectMapper.valueToTree(chatFriend));
        // Return the JSON response
        return ResponseEntity.ok(jsonResponse.toString());
    }
    
    
    @PostMapping("/chat")
    public ResponseEntity<Map<String,String>> postChat( @RequestParam("message") String message,
            @RequestParam("friend_id") String friendId,
            @RequestPart(value = "image", required = false) MultipartFile imageFile, HttpServletRequest req) {
        //TODO: process POST request
    	Map<String, String> response = new HashMap<>();
    	HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
    	Chat chat = new Chat();
    	int FriendId = Integer.parseInt(friendId);
    	chat.setSender(user.getId());
    	chat.setReciever(FriendId);
    	chat.setMessage(message);
    	chat.setPost_time(LocalDateTime.now());
    	byte[] imgByte;
    	if(imageFile!=null) {
			try {
				imgByte = imageFile.getBytes();
				String imgString=Base64.getEncoder().encodeToString(imgByte);
				chat.setImage(imgString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		cRepo.save(chat);
		response.put("result", "OK");
        return ResponseEntity.ok(response);
    }
    
}
