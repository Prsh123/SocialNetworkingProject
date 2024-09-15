package com.virinchi.controller;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.virinchi.model.Comment;
import com.virinchi.model.Likes;
import com.virinchi.model.Post;
import com.virinchi.model.User;
import com.virinchi.repository.CommentRepository;
import com.virinchi.repository.FriendRepository;
import com.virinchi.repository.FriendRequestRepository;
import com.virinchi.repository.LikesRepository;
import com.virinchi.repository.PostRepository;
import com.virinchi.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class FriendProfileController {
	
	@Autowired
	private FriendRequestRepository frRepo;
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private PostRepository pRepo;
	
	@Autowired
	private LikesRepository lRepo;
	
	@Autowired
	private CommentRepository cRepo;
	
	@Autowired
	private FriendRepository fRepo;
	
	@GetMapping("/friendtimeline")
    public String getFriendTimeline(@RequestParam("id") String friendId, Model model,HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		
		int friend_Id=  Integer.valueOf(friendId);
		if(user.getId()==friend_Id) {
			return "redirect:/timeline";
		}
		if(session.getAttribute("user")!=null) {
			List<Post> postList = pRepo.findAllByUser(friend_Id);
			for(Post post: postList) {
				int likecount=0;
				post.setLiked(false);
				List<Likes> likes = lRepo.findByPostId(post.getId());
				for(Likes like : likes) {
			    	likecount++;

			    	if(user.getId()==like.getUserId()) {
			    		post.setLiked(true); 
			    	}
			    }
				post.setLikes(likecount);
				List<Comment> cList = cRepo.findAllByPostIdOrderByPostTimeDesc(post.getId());
				for(Comment comment: cList) {
					Optional<User> commenter = uRepo.findById(comment.getUserId());
					comment.setCommenter(commenter);
				}
				Optional<User> poster= uRepo.findById(post.getUser_id());
				post.setPoster(poster);
				post.setCommentList(cList);
			}
			Optional<User> friendOptional = uRepo.findById(friend_Id);
			
			friendOptional.ifPresent(friend -> {
			    // Process the friend object
			    User userFriend = friend;
			    session.setAttribute("friend", userFriend);
			    // Now you can work with userFriend
			});
			List<Integer> friendIds = fRepo.findFriendIdByUserId(friend_Id);
	         List<User> fList = fRepo.findById(friendIds,friend_Id);
			boolean isFriend=false;
			for(User f: fList) {
				if(f.getId()==user.getId()) {
					isFriend=true;
				}
			}
			
			session.setAttribute("isFriend", isFriend);
			session.setAttribute("friend_timeline_list", postList);
			return "friend_timeline";
		}
		else {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		}
	}
	
	@GetMapping("/friendfriends")
	public String getFriendFriends(@RequestParam("id") String friendId, Model model,HttpServletRequest req) {

		HttpSession session = req.getSession();
		User u = (User) session.getAttribute("user");
		
		int friend_Id=  Integer.valueOf(friendId);

		if(session.getAttribute("user")!=null) {
			List<Integer> friendIds = fRepo.findFriendIdByUserId(friend_Id);
	         List<User> fList = fRepo.findById(friendIds, friend_Id);
	         int friend_no=0;
	         for(User f: fList) {
	        	 friend_no++;
	         }
	         List<Integer>MutualFriends=fRepo.findMutualFriendIdById(u.getId(), friend_Id);
	         List<User> mList = fRepo.findMutualsById(MutualFriends, u.getId(),friend_Id);
	         int mutual_no=0;
	         for(User m: mList) {
	        	 mutual_no++;
	         }
	         session.setAttribute("friend_friends_list", fList);
	        session.setAttribute("friend_friend_no", friend_no);
			session.setAttribute("mutual_friends_list", mList);
			session.setAttribute("mutual_friend_no", mutual_no);
			return "friend_friends";
		}
		else {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		}
	}
	
}
