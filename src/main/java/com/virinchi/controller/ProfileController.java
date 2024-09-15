package com.virinchi.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.virinchi.model.Comment;
import com.virinchi.model.Likes;
import com.virinchi.model.Post;
import com.virinchi.model.User;
import com.virinchi.repository.CommentRepository;
import com.virinchi.repository.FriendRepository;
import com.virinchi.repository.LikesRepository;
import com.virinchi.repository.PostRepository;
import com.virinchi.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProfileController {
	
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
	
	@GetMapping("/editprofile")
	public String getProfile() {
		return "editprofile";
	}
	
	@GetMapping("/editpassword")
	public String getEditPassword(@ModelAttribute User user, Model model, HttpServletRequest req) {
		
		if(user==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
		return "editpassword";
	}
	@PostMapping("/editpassword")
	public String postEditPassword(@RequestParam("new_password") String new_password,
			@RequestParam("current_password") String current_password,
			@RequestParam("confirm_password") String confirm_password,
	        HttpServletRequest request,Model m) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("user");
		if(u==null) {
			m.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
		if(new_password.equals(confirm_password)) {
			String current_Password=DigestUtils.shaHex(current_password);
			String new_Password=DigestUtils.shaHex(new_password);
			if(current_Password.equals(u.getPassword())) {
				
				uRepo.updatePasswordById(u.getId(), new_Password);
				session.invalidate();
				return "login";
			}else {
				m.addAttribute("error","Password incorrect");
			}
		}else {
			m.addAttribute("error","Password incorrect");
		}
		return "editpassword";
		
		
	}
	
	
	@PostMapping("/editprofile")
	public String postProfile(@ModelAttribute User user, Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		User u = (User) session.getAttribute("user");
		if(user==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
		u.setFname(user.getFname());
		u.setLname(user.getLname());
		u.setBirthdate(user.getBirthdate());
		u.setGender(user.getGender());
		u.setUsername(user.getUsername());
		
        uRepo.save(u); // Save updated user to database
        return "editprofile";
	}
	
	
	@PostMapping("/uploadprofilepic")
	public String postProfilePic(@RequestPart(value = "userProfilePhoto", required = false) MultipartFile imageFile, HttpServletRequest req, Model model) {
		
		HttpSession session = req.getSession();
		User u = (User) session.getAttribute("user");
		if(u==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
		byte[] imgByte;
    	if(imageFile!=null) {
			try {
				imgByte = imageFile.getBytes();
				String imgString=Base64.getEncoder().encodeToString(imgByte);
				uRepo.updateAvatarById(u.getId(), imgString);
				u.setAvatar(imgString);
				session.setAttribute("user", u);
				return "redirect:/editprofile";
			} catch (IOException e) {
				
				e.printStackTrace();
			}
    	}
    	
    		model.addAttribute("error","Error in profile pic change");
    		return "redirect:/editprofile";
    	
		
	}
	
	
	@PostMapping("/uploadCoverpic")
	public String postCoverPic(@RequestPart(value = "userCoverPhoto", required = false) MultipartFile imageFile, HttpServletRequest req, Model model) {

		HttpSession session = req.getSession();
		User u = (User) session.getAttribute("user");
		if(u==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
		byte[] imgByte;
    	if(imageFile!=null) {
			try {
				imgByte = imageFile.getBytes();
				String imgString=Base64.getEncoder().encodeToString(imgByte);
				uRepo.updateCoverById(u.getId(), imgString);
				u.setCover(imgString);
				session.setAttribute("user", u);
				return "redirect:/editprofile";
			} catch (IOException e) {
				
				e.printStackTrace();
			}
    	}
    	
    		model.addAttribute("error","Error in Cover pic change");
    		return "redirect:/editprofile";
	}
	
	
	@GetMapping("/timeline")
	public String getTimeline(Model model,HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		if(user==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}

		if(session.getAttribute("user")!=null) {
			List<Post> postList = pRepo.findAllByUser(user.getId());
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
			session.setAttribute("timeline_list", postList);
			model.addAttribute("user", user);
			    
			return "timeline";

		}else {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		}
	}
	
	
	@GetMapping("/timeline-friends")
	public String getTimelineFriend(HttpServletRequest req,Model model) {
		HttpSession session = req.getSession();
		if(session.getAttribute("user")==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}

		return "timeline_friends";
	}
	
	
	
	
}
