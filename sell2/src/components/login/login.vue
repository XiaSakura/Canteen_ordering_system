<template>
	<div class="login">
		<x-header :left-options="{ showBack: false }">登录或者注册</x-header>
		<group>
			<x-input title="账号" name="loginacct" placeholder="请输入账号" v-model="loginacct" keyboard="number" is-type="china-loginacct" required></x-input>
			<x-input title="密码" type="password" placeholder="请输入密码" v-model="password" :min="5" :max="16" required></x-input>
			<x-input title="验证码"><img slot="right-full-height" src="../../assets/code.jpg" /></x-input>
		</group>
		<p><span class="forget-pass">忘记密码?</span></p>
		<box gap="10px 25px">
			<x-button type="primary" @click.native="handleLogin">登录</x-button>
			<x-button>
				<img class="icon-wx" src="../../assets/icon-wx.png" />
				微信登录
			</x-button>
		</box>
	</div>
</template>

<script>
import { XInput, Box, Group, XButton, XHeader, Alert } from 'vux';
import { getStore, setStore, removeStore } from 'common/js/mUtils.js';

export default {
	name: 'login',
	data() {
		return {
			loginacct: '', //用户手机号
			password: '', //用户密码
			list: [{ key: 'gd', value: '广东' }, { key: 'gx', value: '广西' }],
			favorite: 0
		};
	},
	created() {
		this.$store.state.headerShow = false;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo != null) {
			this.$store.state.userInfo = this.userInfo;
			this.$router.replace('/goods');
		}
	},
	activated() {
		this.$store.state.headerShow = false;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo != null) {
			this.$store.state.userInfo = this.userInfo;
			this.$router.replace('/goods');
		}
	},
	components: {
		XInput,
		XButton,
		Group,
		Box,
		XHeader,
		Alert
	},
	methods: {
		handleLogin() {
			if (this.loginacct.length<5) {
				this.$vux.alert.show({
					title: '账号必须大于等于5位',
					content: '请重新输入',
					onHide() {}
				});
				return;
			}
			const url = this.$store.state.loginUrl;
			this.$http.post(url, { loginacct: this.loginacct, userpswd: this.password }).then(
				response => {
					response = response.body;
					if (response.success) {
						this.userInfo = response.data;
						this.$store.state.userInfo = this.userInfo;
						setStore('userInfo', this.userInfo);
						this.$vux.alert.show({
							title: '成功登录',
							content: '欢迎你',
							onHide() {}
						});
						this.$router.replace({
							path: '/goods'
						});
					} else {
						this.$vux.alert.show({
							title: '登录或者注册失败',
							content: '请检查账号',
							onHide() {}
						});
					}
				},
				res => {
					this.$vux.alert.show({
						title: '登录或者注册失败',
						content: '请检查账号',
						onHide() {}
					});
				}
			);
		},
		onChange(val) {
			console.log(val);
		}
	}
};
</script>

<style scoped="scoped" lang="less">
@color666: #666666;
@color393a31: #393a31;
.input-box {
	background-color: #ffffff;
}
.login {
	> p:first-of-type {
		padding: 20px 15px 40px;
		float: right;
		color: @color666;
		font-size: 14px;
	}
	> p:last-of-type {
		width: 100%;
		text-align: center;
		color: @color393a31;
		font-size: 14px;
		position: absolute;
		bottom: 25px;
	}
}
.icon-wx {
	width: 18px;
	height: 14px;
	margin-right: 6px;
}
</style>
