package com.sian.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.sian.category.service.CategoryService;
import com.sian.member.dto.MemberDTO;
import com.sian.member.service.MemberService;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MemberController {
	private final MemberService memberService;
	
	private final CategoryService categoryService;

	/*
	 * ALL
	 */
	@GetMapping("/")
	public String memberIndex(Model model)  {
		model.addAttribute("categoryList", categoryService.getCategoryList("product"));

		return "/index";
	}
	
	
	
	/*
	 * ADMIN ONLY
	 */
	
	@GetMapping("/admin")
	public String adminIndex(){
		return "/admin/index";
	}
	
	
	@GetMapping("/admin/member/memberList")
	public void memberList(Model model) {
		model.addAttribute("memberList",memberService.getMemberList());
	}
	
	/*
	 * MEMBER ONLY @PreAuthorize("hasRole('ROLE_MEMBER')")
	 */
	
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@GetMapping("/member/memberModify")
	public void memberModify() {

	}
	
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@GetMapping("/member/memberDrop")
	public void memberDrop() {

	}
	
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@PostMapping("/member/logout")
	public String logoutPost() {

		return "redirect:/member";
	}
	
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@PostMapping("/member/memberDropProc")
	public String memberDropProc(MemberDTO memberDTO, Authentication authentication, RedirectAttributes rttr)
			 {

		boolean pwdChk = false;
		pwdChk = memberService.pwdChk(memberDTO, authentication);

		if (pwdChk) {
			memberDTO.setMem_id(memberService.getId(authentication));
			if (memberService.memberDrop(memberDTO)) {

				
				return "redirect:/member";
			} else {
				

				return "redirect:/member/memberDrop";
			}

		} else {
			
			rttr.addFlashAttribute("msg", "??????????????? ???????????????.");
			return "redirect:/member/memberDrop";
		}
	}
	
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@PostMapping("/member/memberModify")
	public String memberModifyPwdChk(MemberDTO memberDTO, Authentication authentication, RedirectAttributes rttr)
			 {
		if (!memberService.pwdChk(memberDTO, authentication)) {
			rttr.addFlashAttribute("msg", "??????????????? ???????????????.");
			return "redirect:/member/memberModify";
		} else {

			return "redirect:/member/memberModifyNext";
		}

	}
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@GetMapping("/member/memberModifyNext")
	public void memberModifyNext() {

	}

	@PostMapping("/member/memberModifyNextProc")
	public String memberModifyNextProc(MemberDTO memberDTO)  {

		if (memberService.memberModify(memberDTO)) {

			return "redirect:/member";
		}

		return "redirect:/member/memberModifyNext";

	}
	@PreAuthorize("hasRole('ROLE_MEMBER')")
	@ResponseBody
	@PostMapping("/member/pwdChk")
	public int pwdChk(@RequestBody MemberDTO memberDTO, Authentication authentication)  {
		boolean result = false;
		result = memberService.pwdChk(memberDTO, authentication);

		if (result) {
			
			return 0;
		}
		
		return 1;
	}
	
	
	


	
}
