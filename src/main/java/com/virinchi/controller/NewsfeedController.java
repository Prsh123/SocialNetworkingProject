package com.virinchi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.virinchi.model.Comment;
import com.virinchi.model.Likes;
import com.virinchi.model.Post;
import com.virinchi.model.User;
import com.virinchi.repository.CommentRepository;
import com.virinchi.repository.LikesRepository;
import com.virinchi.repository.PostRepository;
import com.virinchi.repository.UserRepository;

@Controller
public class NewsfeedController {
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private PostRepository pRepo;
	
	@Autowired
	private LikesRepository lRepo;
	@Autowired
	private CommentRepository cRepo;
	
	@GetMapping("/newsfeed")
	public String GetNewsfeed(Model model,HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		
		if(session.getAttribute("user")!=null) {
			List<Post> postList = pRepo.findAllByUserAndFriends(user.getId());
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
			session.setAttribute("post_list", postList);
			model.addAttribute("user", user);
			    
			return "newsfeed";

		}else {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		}

	}
	
	
	
}
