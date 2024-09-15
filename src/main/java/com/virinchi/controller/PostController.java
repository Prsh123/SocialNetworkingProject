package com.virinchi.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.virinchi.model.Comment;
import com.virinchi.model.Likes;
import com.virinchi.model.Post;
import com.virinchi.model.User;
import com.virinchi.repository.CommentRepository;
import com.virinchi.repository.LikesRepository;
import com.virinchi.repository.PostRepository;
import com.virinchi.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
	@Autowired
	private UserRepository uRepo;
	@Autowired
	private PostRepository pRepo;
	@Autowired
	private CommentRepository cRepo;
	@Autowired
	private LikesRepository lRepo;
	
	
	@PostMapping("contentpost")
	public String postContent(@RequestParam("post-content") String postContent,
	        @RequestParam("post-image") MultipartFile postImage,
	        @RequestParam("sourcePage") String sourcePage,
	        HttpServletRequest request,
	        Model model) {
		
		HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("user");
	    if(user==null) {
			model.addAttribute("error","You are not logged in. Login to access the page");
			return "login";
		
		}
	    Post post = new Post();
	    post.setPost_content(postContent);
	    post.setUser_id(user.getId());
	    post.setPost_time(LocalDateTime.now());
	    byte[] imgByte;
		try {
			imgByte = postImage.getBytes();
			String imgString=Base64.getEncoder().encodeToString(imgByte);
			post.setImage(imgString);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pRepo.save(post);
		if ("newsfeed".equals(sourcePage)) {
            return "redirect:/newsfeed";  // Redirect to page1's servlet
        } else if ("timeline".equals(sourcePage)) {
            return "redirect:/timeline";  // Redirect to page2's servlet
        } else {
        	return "login";
        }

	}
	

	@PostMapping("/commentpost")
	public ResponseEntity<Map<String, String>> postComment(@RequestParam("post-content") String commentContent,
            @RequestParam("post-id") int postId,
            HttpServletRequest request) {
		Map<String, String> response = new HashMap<>();
		HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("user");
		System.out.println(commentContent);
		Comment comment = new Comment();
		comment.setUserId(user.getId());
		comment.setContent(commentContent);
		comment.setPostId(postId);
		comment.setPostTime(LocalDateTime.now());
		if(cRepo.save(comment)!=null) {
			response.put("result", "OK");
        }else {
            response.put("result", "FAILED");
        }
		 return ResponseEntity.ok(response);
	}
	
	@PostMapping("/like")
	public ResponseEntity<Map<String, Object>> postLike(@RequestParam("post-id") String post_id,
            HttpServletRequest request) {
		//TODO: process POST request
		Map<String, Object> response = new HashMap<>();
		int postId=Integer.parseInt(post_id);
		int likecount=0;
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Likes like = new Likes();
		like.setPostId(postId);
		like.setUserId(user.getId());
		
		if(lRepo.save(like)!=null) {
			List<Likes> likes = lRepo.findByPostId(postId);
			for(Likes l : likes) {
		    	likecount++;
		    }
			response.put("likecount", likecount);
			response.put("result", "OK");
        }else {
            response.put("result", "FAILED");
        }
		 return ResponseEntity.ok(response);
	}
	
	@PostMapping("/unlike")
	public ResponseEntity<Map<String, Object>> getUnline(@RequestParam("post-id") String post_id,
            HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		int postId=Integer.parseInt(post_id);
		int likecount=0;
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Likes like = new Likes();
		like.setPostId(postId);
		like.setUserId(user.getId());
		
		if(lRepo.deleteByUserIdAndPostId(user.getId(), postId)!=0) {
			List<Likes> likes = lRepo.findByPostId(postId);
			for(Likes l : likes) {
		    	likecount++;
		    }
			response.put("likecount", likecount);
			response.put("result", "OK");
        }else {
            response.put("result", "FAILED");
        }
		 return ResponseEntity.ok(response);
	}
	
	
	
	
	
}
