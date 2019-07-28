package com.cqnu.sell.restapi.service;

import java.util.List;

import com.cqnu.sell.manager.bean.OrderDto;

public interface OrderService {


	List<OrderDto> getOrderByUid(Integer id);

	Boolean insertOrder(OrderDto orderDto);

}
