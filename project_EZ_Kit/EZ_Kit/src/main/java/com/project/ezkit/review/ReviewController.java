package com.project.ezkit.review;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.ezkit.user.User;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService review_Service;
	
	
	// 댓글 등록
	@RequestMapping(value = "/review/insertReview")
	public ModelAndView insertReview(Review r) {
		
		ModelAndView mav = new ModelAndView("/review/reviewJSON");
		
		review_Service.insertReview(r);
		ArrayList<Review> list = (ArrayList<Review>) review_Service.selectReviewByProductNum(r.getProduct_num());

		mav.addObject("list", list);
		
		return mav;
	}
	
	// 댓글 삭제
	@RequestMapping(value = "/review/deleteReview")
	public String deleteReview(RedirectAttributes redirect, @RequestParam(value = "review_num") int review_num) {
		
		
		Review r = review_Service.selectReviewByNum(review_num);
		redirect.addAttribute("product_num", r.getProduct_num());
		review_Service.deleteReview(review_num);
		
		
		return "redirect:/product/product_detail";
	}
	
	// 댓글 수정
	@RequestMapping(value = "/review/updateReview")
	public String updateReview(RedirectAttributes redirect, @RequestParam(value="review_num") int review_num, @RequestParam(value="review_content") String review_content) {
		
		Review r = review_Service.selectReviewByNum(review_num);
		r.setReview_content(review_content);
		review_Service.updateReview(r);
		
		redirect.addAttribute("product_num", r.getProduct_num());
		
		return "redirect:/product/product_detail";
	}
}
