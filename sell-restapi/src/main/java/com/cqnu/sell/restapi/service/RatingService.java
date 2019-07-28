package com.cqnu.sell.restapi.service;

import java.util.List;

import com.cqnu.sell.manager.bean.RatingsDto;
import com.cqnu.sell.manager.bean.TSellerRating;

public interface RatingService {

	RatingsDto getRatingsDto();

	List<RatingsDto> getRatingsDtos();

	Boolean insertFoodRating(TSellerRating sellerRating, Integer foodid);

	Boolean insertSellerRating(TSellerRating sellerRating);

}
