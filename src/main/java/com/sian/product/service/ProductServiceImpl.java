package com.sian.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sian.cart.service.CartService;
import com.sian.common.page.Criteria;
import com.sian.member.service.MemberService;
import com.sian.product.dao.ProductAttachDAO;
import com.sian.product.dao.ProductDAO;
import com.sian.product.dto.ProductAttachDTO;
import com.sian.product.dto.ProductDTO;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
	
	private final ProductDAO productDAO;
	
	private final ProductAttachDAO productAttachDAO;
	
	@Override
	public List<ProductDTO> memberProductList(int category_no) {
		
		return productDAO.memberProductList(category_no);
	}
	@Override
	public int getTotal() {
		
		return productDAO.getTotal();
	}
	@Override
	public List<ProductDTO> getListPaging(Criteria cri) {
		
		return productDAO.getListPaging(cri);
	}
	
	@Override
	public ProductDTO getProduct(int product_no) {
		
		return productDAO.getProduct(product_no);
	}
	@Override
	public int getProductNo(String product_name){
		
		return productDAO.getProductNo(product_name);
	}
	
	@Transactional
	@Override
	public void productRegister(ProductDTO productDTO)  {
		
		productDTO.setProduct_thumb_img(productDTO.getProduct_s_thumb_img().replace("s_", ""));
		
		productDAO.insert(productDTO);
		
		
		if(productDTO.getAttachList() == null || productDTO.getAttachList().size() <= 0) {
			return;
		}
		productDTO.getAttachList().forEach(attach -> {
			attach.setProduct_no(productDTO.getProduct_no());
			productAttachDAO.insert(attach);
		});
		
	}

	@Override
	public List<ProductDTO> getProductList()  {
	
		return productDAO.getList();
	}
	@Override
	public List<ProductAttachDTO> getAttachList(int product_no) {
		
		return productAttachDAO.findByProduct_no(product_no);
	}
	@Transactional
	@Override
	public void productModify(ProductDTO productDTO) {
		if(productDTO.getAttachList() == null || productDTO.getAttachList().size() <= 0) {
			return;
		}
		System.out.println("productNO : " + productDTO.getProduct_no());
		getAttachList(productDTO.getProduct_no())
	 	.forEach(attach->{
	 				productAttachDAO.delete(attach.getUuid());
	 			}
	 		);
		
//		productDTO.getAttachList().forEach(attach -> {
//			attach.setProduct_no(productDTO.getProduct_no());
//			productAttachDAO.delete(attach.getUuid());
//			System.out.println("uuid : " + attach.getUuid());
//		});
		
		productDTO.getAttachList().forEach(attach -> {
			attach.setProduct_no(productDTO.getProduct_no());
			productAttachDAO.insert(attach);
		});
		
		productDAO.productModify(productDTO);
	}
	
	
	
	
	

}
