package com.sian.mapper;

import java.util.List;

import com.sian.domain.ProductDTO;


public interface ProductMapper {
	public void insert(ProductDTO productDTO) throws Exception;
	
	public List<ProductDTO> getList() throws Exception;

	public List<ProductDTO> memberProductList(int category_no) throws Exception;
}