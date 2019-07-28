package com.cqnu.sell.restapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqnu.sell.manager.bean.SellerDto;
import com.cqnu.sell.manager.bean.TSeller;
import com.cqnu.sell.manager.bean.TSupports;
import com.cqnu.sell.manager.dao.TSellerMapper;
import com.cqnu.sell.manager.dao.TSupportsMapper;
import com.cqnu.sell.restapi.service.SellerService;

@Service
public class SellerServiceImpl implements SellerService {
	@Autowired
	private TSellerMapper sellerMapper;
	@Autowired
	private TSupportsMapper supportsMapper;
	
	
	@Override
	public SellerDto getSellerDto() {
		SellerDto sellerDto=new SellerDto();
		
		List<TSeller> list = sellerMapper.selectByExample(null);
		if (list!=null&&list.size()>0) {
			TSeller seller=list.get(0);
			sellerDto.setName(seller.getName());
			sellerDto.setDescription(seller.getDescription());
			sellerDto.setDeliveryTime(seller.getDeliverytime());
			sellerDto.setScore(seller.getScore());
			sellerDto.setServiceScore(seller.getServicescore());
			sellerDto.setFoodScore(seller.getFoodscore());
			sellerDto.setRankRate(seller.getRankrate());
			sellerDto.setMinPrice(seller.getMinprice());
			sellerDto.setDeliveryPrice(seller.getDeliveryprice());
			sellerDto.setRatingCount(seller.getRatingcount());
			sellerDto.setSellCount(seller.getSellcount());
			sellerDto.setBulletin(seller.getBulletin());
			List<TSupports> supports = supportsMapper.selectByExample(null);
			sellerDto.setSupports(supports);
			
			sellerDto.setAvatar(seller.getAvatar());
			//seller.getPics();
			//sellerDto.setPics(pics);
			String infos = seller.getInfos();
			//分割"" 
			String[] split = infos.split(" ");
			List<String> infoList=new ArrayList<>();
			for (String string : split) {
				infoList.add(string);
			}
			sellerDto.setInfos(infoList);
		}
		
		return sellerDto;
	}
	
	
}
