package com.cqnu.sell.restapi.controller.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqnu.sell.manager.bean.AJAXResult;
import com.cqnu.sell.manager.bean.SellerDto;
import com.cqnu.sell.restapi.service.SellerService;

@RestController
public class SellerController {
	@Autowired
	private SellerService sellerService;
	
	@RequestMapping("/seller")
	public Object index() {
		//查询出seller的所有信息 可以定义一个dto 数据传输对象 用于返回给页面
		AJAXResult result=new AJAXResult();
		SellerDto sellerDto= sellerService.getSellerDto();
		result.setData(sellerDto);
		result.setErrno(0);
		result.setSuccess(true);
		return result;
	}
	
}
