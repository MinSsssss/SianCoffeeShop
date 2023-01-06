package com.sian.member.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sian.member.dao.MemberDAO;
import com.sian.member.dto.AuthVO;
import com.sian.member.dto.MemberDTO;

import lombok.Setter;
@Service
public class MemberServiceImpl implements MemberService{
	@Setter(onMethod_ = @Autowired)
	private MemberDAO memberDAO;
	
	@Setter(onMethod_ = @Autowired)
	private BCryptPasswordEncoder passwordEncoder;

	@Setter(onMethod_ = @Autowired)
	private PasswordEncoder pswordEncoder;
	
	
	@Override
	public void register(MemberDTO memberDTO, AuthVO authVO)  {

		List<AuthVO> list = new ArrayList<>();
		authVO.setMem_id(memberDTO.getMem_id());
		list.add(authVO);

		memberDTO.setMem_pwd(passwordEncoder.encode(memberDTO.getMem_pwd()));
		memberDTO.setAuthList(list);
		memberDAO.insert(memberDTO);

	}
	@Override
	public int idChk(MemberDTO memberDTO)  {
		int result = memberDAO.idChk(memberDTO);
		return result;
	}


	@Override
	public String getId(Authentication authentication){
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		MemberDTO thisMem = memberDAO.read(userDetails.getUsername());
		String thisMemId = thisMem.getMem_id();
		return thisMemId;
	}

	@Override
	public boolean memberDrop(MemberDTO memberDTO)  {

		SecurityContextHolder.clearContext();
		return memberDAO.memberDrop(memberDTO) == 1;
	}
	
	//현재 사용자의 비밀번호를 가져옴
	@Override
	public String getPwd(Authentication authentication)  {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		MemberDTO thisMem = memberDAO.read(userDetails.getUsername());
		String thisMemId = thisMem.getMem_id();
		
		String thisMemPwd = memberDAO.getPwd(thisMemId);
		return thisMemPwd;
	}
	//getPwd() 로 사용자의 비밀번호를 가져와서 입력한 비밀번호와 비교.
	@Override
	public boolean pwdChk(MemberDTO memberDTO,Authentication authentication)  {

		if (passwordEncoder.matches(memberDTO.getMem_pwd(), this.getPwd(authentication))) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean memberModify(MemberDTO memberDTO)  {
		memberDTO.setMem_pwd(passwordEncoder.encode(memberDTO.getMem_pwd()));
		return memberDAO.memberModify(memberDTO) == 1;

	}
	
	@Override
	public List<MemberDTO> getMemberList()  {
		return memberDAO.getList();
		
	}
	


}