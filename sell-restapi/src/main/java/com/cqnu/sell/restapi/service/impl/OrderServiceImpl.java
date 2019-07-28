package com.cqnu.sell.restapi.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqnu.sell.manager.bean.OrderDto;
import com.cqnu.sell.manager.bean.TFoodOrder;
import com.cqnu.sell.manager.bean.TFoodOrderExample;
import com.cqnu.sell.manager.bean.TFoods;
import com.cqnu.sell.manager.bean.TOrder;
import com.cqnu.sell.manager.bean.TOrderExample;
import com.cqnu.sell.manager.bean.TOrderExample.Criteria;
import com.cqnu.sell.manager.dao.TFoodOrderMapper;
import com.cqnu.sell.manager.dao.TFoodsMapper;
import com.cqnu.sell.manager.dao.TOrderMapper;
import com.cqnu.sell.restapi.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private TOrderMapper orderMapper;
	@Autowired
	private TFoodOrderMapper foodOrderMapper;
	@Autowired
	private TFoodsMapper foodsMapper;

	@Override
	public List<OrderDto> getOrderByUid(Integer id) {
		List<OrderDto> orderDtos = new ArrayList<>();
		TOrderExample example = new TOrderExample();
		example.createCriteria().andUseridEqualTo(id);
		List<TOrder> list = orderMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			for (TOrder tOrder : list) {
				OrderDto orderDto = new OrderDto();
				orderDto.setCreatetime(tOrder.getCreatetime());
				orderDto.setIscomplete(tOrder.getIscomplete());
				orderDto.setState(tOrder.getState());
				TFoodOrderExample example2 = new TFoodOrderExample();
				example2.createCriteria().andOidEqualTo(tOrder.getId());
				List<TFoodOrder> list2 = foodOrderMapper.selectByExample(example2);
				List<String> foodsName = new ArrayList<>();
				for (TFoodOrder foodOrder : list2) {
					TFoods foods = foodsMapper.selectByPrimaryKey(foodOrder.getFid());
					foodsName.add(foods.getName());
				}
				orderDto.setFoodName(foodsName);
				orderDtos.add(orderDto);
			}
		}
		// 排序
		Collections.sort(orderDtos, new Comparator<OrderDto>() {
			@Override
			public int compare(OrderDto o1, OrderDto o2) {
				o1.getCreatetime().after(o2.getCreatetime());
				return -1;
			}
		});
		return orderDtos;
	}

	@Override
	public Boolean insertOrder(OrderDto orderDto) {
		TOrder record = new TOrder();
		// 设置订单的初始状态 未接收订单
		record.setState(0);
		Date date = new Date();
		record.setCreatetime(date);
		record.setSellerid(1);
		record.setIscomplete(false);
		record.setUserid(orderDto.getUserid());
		orderMapper.insertSelective(record);

		// 查出对应order的id
		TOrderExample example = new TOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andCreatetimeEqualTo(date);
		criteria.andUseridEqualTo(orderDto.getUserid());
		criteria.andIscompleteEqualTo(false);
		List<TOrder> list = orderMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			TOrder order = list.get(0);
			Integer oid = order.getId();
			TFoodOrder foodOrder = new TFoodOrder();
			foodOrder.setOid(oid);
			// 插入中间表 数据 去掉最后的逗号
			String foods = orderDto.getFoods();
			String[] split = foods.split(",");
			// 先分割出来 商品
			for (int i = 0; i < split.length; i++) {
				// 再分割出来 数量 商品&数量 例如3&2
				String[] strings = split[i].split("&");
				if (strings.length == 2) {
					int foodid = Integer.parseInt(strings[0]);
					int count = Integer.parseInt(strings[1]);
					// 循环插入中间表 几次就代表 这个商品被选了几次
					foodOrder.setFid(foodid);
					for (int j = 0; j < count; j++) {
						foodOrderMapper.insertSelective(foodOrder);
					}
				}else {
					return false;
				}
			}
			return true;
		}

		return false;
	}

}
