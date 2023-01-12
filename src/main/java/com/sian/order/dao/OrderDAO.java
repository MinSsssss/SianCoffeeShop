package com.sian.order.dao;

import java.util.List;
import java.util.Map;

import com.sian.cart.dto.CartProductDTO;
import com.sian.order.dto.OrderDTO;
import com.sian.order.dto.OrderDetailDTO;


public interface OrderDAO {

	
	public Long orderInsert(OrderDTO orderDTO);
	
	public void orderDetailInsert(OrderDetailDTO orderDetailDTO);
	
	public void orderCartDelete(Map<String,Object>map);
	
	public Long getOrderNo(String mem_id);
	
	public OrderDTO getOrder(Long order_no);
	
	public List<OrderDTO> getOrderList(String mem_id);
	
	public List<OrderDetailDTO> getOrderDetailList(Long order_no);
	
	public int orderDelete(Long order_id);
	
	public OrderDetailDTO getReviewView(Long order_detail_no);
}
