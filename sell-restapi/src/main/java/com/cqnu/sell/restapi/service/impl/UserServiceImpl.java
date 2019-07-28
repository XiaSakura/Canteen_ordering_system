package com.cqnu.sell.restapi.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqnu.sell.manager.bean.TUser;
import com.cqnu.sell.manager.bean.TUserExample;
import com.cqnu.sell.manager.bean.TUserExample.Criteria;
import com.cqnu.sell.manager.dao.TUserMapper;
import com.cqnu.sell.restapi.service.UserService;
import com.xia.project.MD5Utils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TUserMapper userMapper;

	@Override
	public TUser loginOrReg(TUser user) {
		TUserExample example = new TUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andLoginacctEqualTo(user.getLoginacct());
		criteria.andUserpswdEqualTo(MD5Utils.digest(user.getUserpswd()));
		List<TUser> list = userMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else { 
			TUserExample example2 = new TUserExample();
			Criteria criteria2 = example2.createCriteria();
			criteria2.andLoginacctEqualTo(user.getLoginacct());
			List<TUser> list3 = userMapper.selectByExample(example2);
			// 说明用户密码输错了
			if (list3 != null && list3.size() > 0) {
				return null;
			}
			//下面说明该账号没有注册
			user.setCreatetime(new Date());
			user.setUsername(user.getLoginacct());
			user.setUserpswd(MD5Utils.digest(user.getUserpswd()));
			// 先使用默认的头像
			user.setAvatar("userAvatar/5a22164c3a_file_default_header.png");
			int i = userMapper.insert(user);
			if (i != 1) {
				return null;
			} else {
				TUserExample example3 = new TUserExample();
				example3.createCriteria().andLoginacctEqualTo(user.getLoginacct());
				List<TUser> list2 = userMapper.selectByExample(example3);
				if (list2 != null && list2.size() > 0) {
					return list2.get(0);
				}
			}
		}
		return null;
	}

}
