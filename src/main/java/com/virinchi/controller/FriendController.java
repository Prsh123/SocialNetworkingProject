package com.virinchi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.virinchi.model.Friend_Request;
import com.virinchi.model.Friends;
import com.virinchi.model.User;
import com.virinchi.repository.FriendRepository;
import com.virinchi.repository.FriendRequestRepository;
import com.virinchi.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendController {
	
	@Autowired
	private FriendRequestRepository frRepo;
	
	@Autowired
	private FriendRepository fRepo;
	
	@Autowired
	private UserRepository uRepo;
	
	@GetMapping("/friendrequest")
	public String getFriendRequests(HttpServletRequest req,Model model) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		if(user==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
		List<Integer> requesterIds = frRepo.findRequesterByReceiver(user.getId());
		List<User> FRList = uRepo.findAllById(requesterIds);
		int request_no=0;
        for(User f: FRList) {
       	 request_no++;
        }
        session.setAttribute("friend_request_no", request_no);
		session.setAttribute("friend_requests", FRList);
		return "requests";
	}
	
	@PostMapping("/friendrequest")
	public String postFriendDecision(HttpServletRequest req,Model model) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		if(user==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
		String action = req.getParameter("action");
		Friends f1 = new Friends();
		Friends f2 = new Friends();
		int friendId = Integer.parseInt(req.getParameter("friendId"));
		
		f1.setFriendId(friendId);
		f1.setUserId(user.getId());
		f2.setFriendId(user.getId());
		f2.setUserId(friendId);
		if(action.equals("accept")) {
			fRepo.save(f1);
			fRepo.save(f2);

			frRepo.deleteByRequesterAndReceiver(friendId,user.getId());
			return "redirect:/friendrequest";
			
			
		}
		else{
		    
			frRepo.deleteByRequesterAndReceiver(friendId,user.getId());
			return "redirect:/friendrequest";
		}
		
	}

	@GetMapping("/friendsuggestion")
	public String getfriendSuggestion(HttpServletRequest req,Model model) {
		HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if(user==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
        List<Integer> friendIds = fRepo.findFriendIdByUserId(user.getId());
        List<Integer> requestedIds = frRepo.findRequesterByReceiver(user.getId());
        List<Integer> requesterIds = frRepo.findReceiverByRequester(user.getId());
        List<User> fsList = fRepo.findAvailableUsers(user.getId(), friendIds, requestedIds, requesterIds);
        session.setAttribute("suggested_friends",fsList);
		return "friend_suggestion";
	}
	
	@PostMapping("sendRequest")
	public String postFriendRequest(@RequestParam("friendId") String friendId, HttpServletRequest req,Model model) {
		HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if(user==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
        Friend_Request fr = new Friend_Request();
        fr.setRequester(user.getId());
        fr.setTo(Integer.parseInt(friendId));
		frRepo.save(fr);
		return "redirect:/friendsuggestion";
	}
	
	 @GetMapping("/friendlist") 
	 public String getFriendList(HttpServletRequest req,Model model) {
		 
		 HttpSession session = req.getSession();
         User u = (User) session.getAttribute("user");
         if(u==null) {
 			model.addAttribute("error","You are not logged in. Login to access the page");
 			return "login";
 		
 		}
         List<Integer> friendIds = fRepo.findFriendIdByUserId(u.getId());
         List<User> fList = fRepo.findById(friendIds,u.getId());
         int friend_no=0;
         for(User f: fList) {
        	 friend_no++;
         }
         session.setAttribute("user_friend_no", friend_no);
         session.setAttribute("friend_list", fList);
         
	 	return "friend_list";
	 }
	
	 
	 @PostMapping("/unfriend")
	 public String postUnfriend(HttpServletRequest req, Model model,
			 @RequestParam("action") String action,
			 @RequestParam("friendId") String FriendId) {
		 HttpSession session = req.getSession();
	        User user = (User) session.getAttribute("user");
	        if(user==null) {
				model.addAttribute("error","You are not logged in. Login to access the page");
				return "login";
			
			}
	        int friendId = Integer.parseInt(FriendId);
	        fRepo.deleteByFriendIdAndUserId(friendId, user.getId());
	        fRepo.deleteByFriendIdAndUserId(user.getId(), friendId);
	        if(action.equals("timeline-friends")) {
	        	return "redirect:/timeline-friends";
	        }
	        else {
	        	return"redirect:/friendtimeline";
	        }
	 	
	 }
	 
	 
	 @PostMapping("/unfriendfriend")
	 public ResponseEntity<String> postUnfriend(HttpServletRequest req,
			 @RequestParam("friendId") String FriendId) {
		 HttpSession session = req.getSession();
	        User user = (User) session.getAttribute("user");
	        int friendId = Integer.parseInt(FriendId);
	        fRepo.deleteByFriendIdAndUserId(friendId, user.getId());
	        fRepo.deleteByFriendIdAndUserId(user.getId(), friendId);
	        return ResponseEntity.ok("Successfully unfriended");
	 }
	 
	
}
