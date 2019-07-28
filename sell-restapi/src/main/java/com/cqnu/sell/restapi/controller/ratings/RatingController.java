package com.cqnu.sell.restapi.controller.ratings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqnu.sell.manager.bean.AJAXResult;
import com.cqnu.sell.manager.bean.RatingsDto;
import com.cqnu.sell.manager.bean.TSellerRating;
import com.cqnu.sell.restapi.service.RatingService;

@RestController
public class RatingController {
	@Autowired
	private RatingService ratingService;

	@RequestMapping("/ratings")
	public Object index() {
		// 查询出ratings的所有信息 可以定义一个dto 数据传输对象 用于返回给页面
		AJAXResult result = new AJAXResult();
		List<RatingsDto> ratingsDtos = ratingService.getRatingsDtos();
		result.setData(ratingsDtos);
		result.setErrno(0);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 用户发表评论接口
	 * 
	 * @return
	 */
	@RequestMapping("/comment")
	public Object comment(@RequestBody TSellerRating sellerRating) {
		AJAXResult result = new AJAXResult();
		Boolean flag = false;
		
		// 判断是用户是给商家评论还是给商品评论 利用foodid
		if (sellerRating.getFoodid() != null) {
			flag = ratingService.insertFoodRating(sellerRating, sellerRating.getFoodid());
		} else {
			flag = ratingService.insertSellerRating(sellerRating);
		}
		if (flag) {
			result.setSuccess(true);
			result.setMsg("发表成功");
		}else {
			result.setSuccess(false);
			result.setMsg("发表失败");
		}
		return result;
	}

}
