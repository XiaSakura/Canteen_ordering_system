<template>
	<div>
		<div v-show="$store.state.headerShow" >
			<v-header :seller="seller"></v-header>
			<!--border-1px 在base.styl对这条线做了一个缩放-->
			<!-- <div class="tab border-1px">
				<div class="tab-item"><router-link to="/goods">商品</router-link></div>
				<div class="tab-item"><router-link to="/ratings">评论</router-link></div>
				<div class="tab-item"><router-link to="/seller">商家</router-link></div>
				<div class="tab-item"><router-link to="/order">订单</router-link></div>
				<div class="tab-item"><router-link to="/my">我的</router-link></div>
			</div> -->
			<tab class="tab" bar-active-color="#668599" >
				<tab-item class="tab-item"><router-link to="/goods">商品</router-link></tab-item>
				<tab-item class="tab-item"><router-link to="/ratings">评论</router-link></tab-item>
				<tab-item class="tab-item"><router-link to="/seller">商家</router-link></tab-item>
				<tab-item class="tab-item"><router-link to="/order">订单</router-link></tab-item>
				<tab-item class="tab-item"><router-link to="/my">我的</router-link></tab-item>
			</tab>
		</div>
		<!--<div class="content"></div>-->
		<!--利用路由来添加内容 添加 header.vue rating.vue seller.vue 这些内容-->
		<keep-alive><router-view :seller="seller"></router-view></keep-alive>
	</div>
</template>

<script>
/* eslint-disable */
import { Tab, TabItem, Sticky, Divider, XButton, Swiper, SwiperItem } from 'vux';
import header from 'components/header/header.vue';
import { urlParse } from 'common/js/util';
// 判断是否是开发环境
const debug = process.env.NODE_ENV !== 'production';
// get请求 正确的返回码
const ERROR_OK = 0;
export default {
	// 定义seller变量
	data() {
		return {
			seller: {
				id: (() => {
					/* urlParse调用 util.js里面的方法 得到url里面的id */
					let queryParam = urlParse();
					return queryParam.id;
				})()
			}
		};
	},
	methods:{
	},
	components: {
		'v-header': header,
		Tab,
		TabItem,
		Swiper,
		SwiperItem
	},
	created() {
		const url = this.$store.state.sellerUrl;
		this.$http.get(url).then(response => {
			response = response.body;
			if (response.errno === ERROR_OK) {
				/* 这是es6的语法相当于对seller对象进行了扩展 给seller添加了id的属性 否则其它组件获得的seller没有id */
				this.seller = Object.assign({}, this.seller, response.data);
			}
		});
	},
	methods: {}
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
@import "~common/stylus/mixin.styl";
.tab
	display:flex
	width: 100%
	.tab-item
		flex:1
		text-align: center
		& > a
			display: block
			font-size: 14px
			color: rgb(77, 85, 93)
			&.active
				color: rgb(240, 20, 20)
</style>
