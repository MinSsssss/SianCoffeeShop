package com.sian.product.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ProductDTO {
	private int product_no;
	
	private int category_no;
	
	private String category_name;
	
	private String product_name;
	
	private int product_price;
	
	private String product_detail;
	
//	private String product_image1;
//	

	
	private Date product_regdate;
	
	private Date product_updateDate;
	
	private Long product_hit;
	
	private List<ProductAttachDTO> attachList;
	
	private String product_thumb_img;
	
	private String product_s_thumb_img;
	//큰이미지
	private List<String> product_imgs;
	//작은이미지
	private List<String> product_s_imgs;
}
