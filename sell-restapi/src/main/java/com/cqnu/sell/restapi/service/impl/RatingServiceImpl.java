package com.cqnu.sell.restapi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqnu.sell.manager.bean.RatingsDto;
import com.cqnu.sell.manager.bean.TFoodRating;
import com.cqnu.sell.manager.bean.TSellerRating;
import com.cqnu.sell.manager.bean.TUser;
import com.cqnu.sell.manager.dao.TFoodRatingMapper;
import com.cqnu.sell.manager.dao.TSellerRatingMapper;
import com.cqnu.sell.manager.dao.TUserMapper;
import com.cqnu.sell.restapi.service.RatingService;

@Service
public class RatingServiceImpl implements RatingService {
	@Autowired
	private TSellerRatingMapper sellerRatingMapper;
	@Autowired
	private TUserMapper userMapper;
	@Autowired
	private TFoodRatingMapper foodRatingMapper;

	@Override
	public RatingsDto getRatingsDto() {
		RatingsDto ratingsDto = new RatingsDto();
		List<TSellerRating> list = sellerRatingMapper.selectByExample(null);
		if (list != null && list.size() > 0) {
			for (TSellerRating sellerRating : list) {
				// 找到对应的用户
				TUser user = userMapper.selectByPrimaryKey(sellerRating.getUserid());
				ratingsDto.setUsername(user.getUsername());
				ratingsDto.setRateTime(sellerRating.getRatetime());
				ratingsDto.setScore(sellerRating.getScore());
				ratingsDto.setRateType(sellerRating.getRatetype());
				ratingsDto.setText(sellerRating.getText());
				ratingsDto.setAvatar(user.getAvatar());
				List<String> recommends = null;
				if (sellerRating.getRecommend() != null && sellerRating.getRecommend().trim() != "") {
					String[] split = sellerRating.getRecommend().split(",");
					recommends = new ArrayList<>();
					for (String string : split) {
						recommends.add(string);
					}
				}
				ratingsDto.setRecommend(recommends);
			}
		}
		return ratingsDto;
	}

	@Override
	public List<RatingsDto> getRatingsDtos() {
		List<RatingsDto> ratingsDtos = new ArrayList<>();
		List<TSellerRating> list = sellerRatingMapper.selectByExample(null);
		if (list != null && list.size() > 0) {
			for (TSellerRating sellerRating : list) {
				RatingsDto ratingsDto = new RatingsDto();
				// 找到对应的用户
				TUser user = userMapper.selectByPrimaryKey(sellerRating.getUserid());
				ratingsDto.setUsername(user.getUsername());
				ratingsDto.setRateTime(sellerRating.getRatetime());
				ratingsDto.setScore(sellerRating.getScore());
				ratingsDto.setRateType(sellerRating.getRatetype());
				ratingsDto.setText(sellerRating.getText());
				ratingsDto.setAvatar(user.getAvatar());
				List<String> recommends = null;
				if (sellerRating.getRecommend() != null && sellerRating.getRecommend().trim() != "") {
					String[] split = sellerRating.getRecommend().split(",");
					recommends = new ArrayList<>();
					for (String string : split) {
						recommends.add(string);
					}
				}
				ratingsDto.setRecommend(recommends);
				ratingsDtos.add(ratingsDto);
			}
		}
		return ratingsDtos;
	}

	@Override
	public Boolean insertFoodRating(TSellerRating sellerRating, Integer foodid) {
		int selective = 0;
		try {
			TFoodRating record = new TFoodRating();
			record.setFoodid(foodid);
			record.setRatetype(sellerRating.getRatetype());
			record.setRatetime(new Date());
			record.setText(sellerRating.getText());
			record.setUserid(sellerRating.getUserid());
			selective = foodRatingMapper.insertSelective(record);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return selective == 1 ? true : false;
	}

	@Override
	public Boolean insertSellerRating(TSellerRating sellerRating) {
		int selective = 0;
		try {
			sellerRating.setRatetime(new Date());
			selective = sellerRatingMapper.insertSelective(sellerRating);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selective == 1 ? true : false;
	}

}
