package com.cqnu.sell.restapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqnu.sell.manager.bean.FoodsDto;
import com.cqnu.sell.manager.bean.GoodsDto;
import com.cqnu.sell.manager.bean.RatingsDto;
import com.cqnu.sell.manager.bean.TFoods;
import com.cqnu.sell.manager.bean.TGoods;
import com.cqnu.sell.manager.bean.TGoodsFoods;
import com.cqnu.sell.manager.bean.TGoodsFoodsExample;
import com.cqnu.sell.manager.dao.RatingsMapper;
import com.cqnu.sell.manager.dao.TFoodsMapper;
import com.cqnu.sell.manager.dao.TGoodsFoodsMapper;
import com.cqnu.sell.manager.dao.TGoodsMapper;
import com.cqnu.sell.restapi.service.GoodsService;
@Service
public class GoodsServiceImpl implements GoodsService {
	@Autowired
	private TGoodsMapper goodsMapper;
	@Autowired
	private TFoodsMapper foodsMapper;
	@Autowired
	private TGoodsFoodsMapper goodsFoodsMapper;
	@Autowired
	private RatingsMapper ratingsMapper;
	@Override
	public List<GoodsDto> getGoodsDtos() {
		List<GoodsDto> goodsDtos = new ArrayList<>();
		List<TGoods> list = goodsMapper.selectByExample(null);
		if (list != null & list.size() > 0) {
			for (TGoods goods : list) {
				GoodsDto goodsDto = new GoodsDto();
				goodsDto.setName(goods.getName());
				goodsDto.setType(goods.getType());
				// 查询出对应foods
				TGoodsFoodsExample example = new TGoodsFoodsExample();
				example.createCriteria().andGoodidEqualTo(goods.getId());
				List<TGoodsFoods> list2 = goodsFoodsMapper.selectByExample(example);
				List<FoodsDto> foods = new ArrayList<>();
				if (list2 != null & list2.size() > 0) {
					for (TGoodsFoods goodsFoods : list2) {
						FoodsDto foodsDto=new FoodsDto();
						// 根据foodid去找出 food 放到dto里面
						TFoods food = foodsMapper.selectByPrimaryKey(goodsFoods.getFoodid());
						foodsDto.setName(food.getName());
						foodsDto.setPrice(food.getPrice());
						foodsDto.setOldPrice(food.getOldprice());
						foodsDto.setDescription(food.getDescription());
						foodsDto.setSellCount(food.getSellcount());
						foodsDto.setRating(food.getRating());
						foodsDto.setInfo(food.getInfo());
						foodsDto.setFoodid(food.getId());
						foodsDto.setIcon(food.getIcon());
						foodsDto.setImage(food.getImage());
						//根据foodid 找到相应的RatingsDto
						List<RatingsDto> ratingsDtos=ratingsMapper.getRatingsDtoByFoodId(food.getId());
						foodsDto.setRatings(ratingsDtos);
						foods.add(foodsDto);
					}
				}
				goodsDto.setFoods(foods);
				goodsDtos.add(goodsDto);
			}
		}

		return goodsDtos;
	}
}
