package com.cqnu.sell.restapi.controller.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqnu.sell.manager.bean.AJAXResult;
import com.cqnu.sell.manager.bean.TUser;
import com.cqnu.sell.restapi.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping("/login")
	public Object index(@RequestBody TUser user) {
		AJAXResult result=new AJAXResult();
		if (user.getLoginacct()==null&&user.getLoginacct().trim().equals("")&&user.getUserpswd()==null) {
			result.setSuccess(false);
			result.setMsg("请检查输入是否正确");
			return result;
		}
		if (user!=null) {
			TUser tuser=userService.loginOrReg(user);
			if (tuser!=null) {
				result.setSuccess(true);
				result.setMsg("登录成功");
				result.setData(tuser);
			}else {
				result.setSuccess(false);
				result.setMsg("登录或者注册失败,请检查账号密码");
			}
		}
		
		return result;
	}
}
