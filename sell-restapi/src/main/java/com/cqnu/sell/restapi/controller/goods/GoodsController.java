package com.cqnu.sell.restapi.controller.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqnu.sell.manager.bean.AJAXResult;
import com.cqnu.sell.manager.bean.GoodsDto;
import com.cqnu.sell.restapi.service.GoodsService;

@RestController
public class GoodsController {
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping("/goods")
	public Object index() {
		//查询出ratings的所有信息 可以定义一个dto 数据传输对象 用于返回给页面
		AJAXResult result=new AJAXResult();
		List<GoodsDto> goodsDtos= goodsService.getGoodsDtos();
		result.setData(goodsDtos);
		result.setErrno(0);
		result.setSuccess(true);
		return result;
	}
}
