package com.virinchi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.virinchi.model.User;
import com.virinchi.repository.FriendRepository;
import com.virinchi.repository.FriendRequestRepository;
import com.virinchi.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class SearchController {
	
	@Autowired
	private FriendRequestRepository frRepo;
	
	@Autowired 
	private UserRepository uRepo;
	
	@Autowired
	private FriendRepository fRepo;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@GetMapping("/searchfriend")
	public String getSearchFriend(@RequestParam("search-friend") String searchQuery, Model model, HttpServletRequest req) {
		
		HttpSession session  = req.getSession();
		 User user = (User) session.getAttribute("user");
		if(user==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
		List<User> results = null;
		if(searchQuery.equals("")) {
			 return "search";
		 }
		results = uRepo.searchByKeyword(searchQuery);  
		 
		 for(User ur: results) {
			 List<Integer> mList=fRepo.findMutualFriendIdById(user.getId(), ur.getId());
			 int mutual=0;
			 String friend="nofriend";
			 for(int m :mList) {
				 mutual++;
			 }
			 ur.setMutual_no(mutual);
			 List<Integer> list = frRepo.findReceiverByRequester(user.getId());
			 for(int l : list) {
				 if(l==ur.getId()) {
					 friend="requested";
				 }
			 }
			 List<Integer> friends = fRepo.findFriendIdByUserId(user.getId());
			 for(int f: friends) {
				 if(f==ur.getId()) {
					 friend="friend";
				 }
			 }
			 if(ur.getId()==user.getId()) {
				 friend="self";
			 }
			 ur.setFriend(friend); 
		 }
		 model.addAttribute("results",results);
		 return "search";
	
	}
	
	@GetMapping("/searchfriendajax")
	public ResponseEntity<?> getFriendAjax(@RequestParam("search-friend") String searchQuery, Model model, HttpServletRequest req) {
		
		HttpSession session  = req.getSession();
		 User user = (User) session.getAttribute("user");
		List<User> results = null;
				
		 
		 if(searchQuery.equals("")) {
			 ObjectNode jsonResponse = objectMapper.createObjectNode();
		        jsonResponse.set("results", objectMapper.valueToTree(results));
		        
				
				return ResponseEntity.ok(jsonResponse.toString()); 
		 }
		 results =uRepo.searchByKeyword(searchQuery);  
		 for(User ur: results) {
			 List<Integer> mList=fRepo.findMutualFriendIdById(user.getId(), ur.getId());
			 int mutual=0;
			 String friend="nofriend";
			 for(int m :mList) {
				 mutual++;
			 }
			 ur.setMutual_no(mutual);
			 List<Integer> list = frRepo.findReceiverByRequester(user.getId());
			 for(int l : list) {
				 if(l==ur.getId()) {
					 friend="requested";
				 }
			 }
			 List<Integer> friends = fRepo.findFriendIdByUserId(user.getId());
			 for(int f: friends) {
				 if(f==ur.getId()) {
					 friend="friend";
				 }
			 }
			 if(ur.getId()==user.getId()) {
				 friend="self";
			 }
			 ur.setFriend(friend); 
		 }
		 ObjectNode jsonResponse = objectMapper.createObjectNode();
	        jsonResponse.set("results", objectMapper.valueToTree(results));
	        
			
			return ResponseEntity.ok(jsonResponse.toString());
	}
	
	
}
