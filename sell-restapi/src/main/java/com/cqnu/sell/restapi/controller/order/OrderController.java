package com.cqnu.sell.restapi.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqnu.sell.manager.bean.AJAXResult;
import com.cqnu.sell.manager.bean.OrderDto;
import com.cqnu.sell.restapi.service.OrderService;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * 根据用户名 查出该用户的订单
	 * 
	 * @return
	 */
	@RequestMapping("/order")
	public Object index(Integer id) {
		AJAXResult result = new AJAXResult();
		List<OrderDto> orderDto = orderService.getOrderByUid(id);
		if (orderDto != null && orderDto.size() > 0) {
			result.setMsg("成功");
			result.setErrno(0);
			result.setSuccess(true);
			result.setData(orderDto);
		} else {
			result.setMsg("失败");
			result.setSuccess(false);
		}
		return result;
	}

	@RequestMapping("/submitOrder")
	public Object submitOrder(@RequestBody OrderDto orderDto) {
		AJAXResult result = new AJAXResult();
		if (orderDto.getFoods()==null&&orderDto.getFoods().trim().equals("")&&orderDto.getUserid()==null) {
			result.setMsg("失败");
			result.setSuccess(false);
			return result;
		}
		Boolean flag= orderService.insertOrder(orderDto);
		if (flag) {
			result.setMsg("成功");
			result.setSuccess(true);
		} else {
			result.setMsg("失败");
			result.setSuccess(false);
		}
		return result;
	}

}
