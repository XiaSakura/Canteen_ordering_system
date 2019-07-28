<template>
	<!-- 订单页 -->
	<div class="ratings" ref="ratings">
		<!-- 订单内容 -->
		<div class="ratings-content">
			<!-- 左边是商家的综合评分 右边是服务评分 -->
			<div class="overview">
				<div class="overview-left">
					<h1 class="score">{{ seller.score }}</h1>
					<div class="title">综合评分</div>
					<div class="rank">高于周边商家{{ seller.rankRate }}%</div>
				</div>
				<div class="overview-right">
					<!-- score-wrapper -->
					<!-- 包括标题,星级,分数 -->
					<div class="scroe-wrapper">
						<span class="title">服务态度</span>
						<!-- 复用星星组件 -->
						<star :size="36" :score="seller.serviceScore"></star>
						<span class="score">{{ seller.serviceScore }}</span>
					</div>
					<div class="scroe-wrapper">
						<span class="title">商品评分</span>
						<!-- 复用星星组件 -->
						<star :size="36" :score="seller.foodScore"></star>
						<span class="score">{{ seller.foodScore }}</span>
					</div>
					<!-- 送达时间 -->
					<div class="delivery-wrapper">
						<span class="title">平均等待时间</span>
						<span class="delivery">{{ seller.deliveryTime }}</span>
					</div>
				</div>
			</div>
			<split></split>
			<!-- 复用ratingselect组件 组件包含订单类型和只查看内容-->
			<!-- 用户订单列表 -->
			<div class="rating-wrapper" v-show="this.ratings">
				<ul>
					<li v-for="(rating, index) in ratings" :key="index" class="rating-item">
						<!-- 头像是固定的 在左边 所以可以用到flex布局 -->
						<!--  -->
						<div class="avatar"><img :src="$store.state.imgUrl+'/'+seller.avatar" width="28px" height="28px" /></div>
						<!-- flex布局 订单内容在右边-->
						<div class="content">
							<h1 class="name">{{ seller.name }}</h1>
							<div class="star-wrapper">
								<x-button v-show="rating.iscomplete" mini>订单完成</x-button>
								<x-button v-show="!rating.iscomplete" mini type="primary" @click.native="showOrder($event, rating.state)">查看订单</x-button>
							</div>
							<!-- 订单内容 -->
							<div class="recommend" style="margin-top: 20px;" v-show="rating.foodName && rating.foodName.length">
								<span class="icon-thumb_up"></span>
								<span class="item" v-for="(item, index) in rating.foodName" :key="index">{{ item }}</span>
							</div>
							<!-- 订单时间 使用绝对定位-->
							<div class="time">点餐时间 &nbsp; {{ rating.createtime | formatDate }}</div>
							<!-- 评论按钮 发表评论 -->
						</div>
					</li>
				</ul>
			</div>
			<div v-transfer-dom>
				<popup v-model="orderShow" v-show="orderShow" height="300px">
					<div class="popup0">
						<group title="订单内容">
							<div v-show="this.state == 0">
								<flow ref="flow">
									<flow-state state="0" title="未接收订单" is-done><span slot="未接收订单"></span></flow-state>
									<flow-line tip="正在执行"></flow-line>
									<flow-state state="1" title="接收订单"></flow-state>
									<flow-line></flow-line>
									<flow-state state="2" title="厨师正在做"></flow-state>
									<flow-line></flow-line>
									<flow-state state="3" title="正在上菜"></flow-state>
									<flow-line></flow-line>
									<flow-state state="4" title="请到窗口取菜"></flow-state>
								</flow>
							</div>
							<div v-show="this.state == 1">
								<flow ref="flow">
									<flow-state state="0" title="未接收订单" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="1" title="接收订单" is-done><span slot="接收订单"></span></flow-state>
									<flow-line tip="正在执行"></flow-line>
									<flow-state state="2" title="厨师正在做"></flow-state>
									<flow-line></flow-line>
									<flow-state state="3" title="正在上菜"></flow-state>
									<flow-line></flow-line>
									<flow-state state="4" title="请到窗口取菜"></flow-state>
								</flow>
							</div>
							<div v-show="this.state == 2">
								<flow ref="flow">
									<flow-state state="0" title="未接收订单" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="1" title="接收订单" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="2" title="厨师正在做" is-done><span slot="厨师正在做"></span></flow-state>
									<flow-line tip="正在执行"></flow-line>
									<flow-state state="3" title="正在上菜"></flow-state>
									<flow-line></flow-line>
									<flow-state state="4" title="请到窗口取菜"></flow-state>
								</flow>
							</div>
							<div v-show="this.state == 3">
								<flow ref="flow">
									<flow-state state="0" title="未接收订单" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="1" title="接收订单" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="2" title="厨师正在做" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="3" title="正在上菜" is-done><span slot="正在上菜"></span></flow-state>
									<flow-line tip="正在执行"></flow-line>
									<flow-state state="4" title="请到窗口取菜"></flow-state>
								</flow>
							</div>
							<div v-show="this.state == 4">
								<flow ref="flow">
									<flow-state state="0" title="未接收订单" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="1" title="接收订单" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="2" title="厨师正在做" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="3" title="正在上菜" is-done></flow-state>
									<flow-line is-done></flow-line>
									<flow-state state="4" title="请到窗口取菜" is-done><span slot="请到窗口取菜"></span></flow-state>
								</flow>
							</div>
						</group>
					</div>
				</popup>
			</div>
		</div>
	</div>
</template>

<script>
/* eslint-disable */
import star from 'components/star/star.vue';
import split from 'components/split/split.vue';
import { formatDate } from 'common/js/date';
import BScroll from 'better-scroll';
import { TransferDom, Popup, Group, XButton, XInput, Flow, FlowState, FlowLine } from 'vux';
import { getStore, setStore, removeStore } from 'common/js/mUtils.js';
/* 这些参数用于 ratingsselect组件 */
const POSITIVE = 0;
const NEGATIVE = 1;
const ALL = 2;

// 判断是否是开发环境
const debug = process.env.NODE_ENV !== 'production';
// get请求 正确的返回码
const ERROR_OK = 0;

export default {
	props: {
		seller: {
			type: Object
		}
	},
	directives: {
		TransferDom
	},
	data() {
		return {
			ratings: [],
			/* 这些参数用于ratingselect组件 */
			selectType: ALL,
			onlyContent: true,
			orderShow: false,
			state: 0
		};
	},
	activated() {
		this.$store.state.headerShow = true;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo == null) {
			this.$router.replace('/login');
		}
		const url = this.$store.state.orderUrl+"?id="+ this.userInfo.id;;
		this.$http.get(url).then(response => {
			response = response.body;
			if (response.success) {
				this.ratings = response.data;
				for (let i = 0; i <this.ratings.length; i++) {
					if (this.ratings[i].state==4) {
						this.$vux.alert.show({
							title: '请取餐',
							content: '请你到指定窗口取餐',
							onHide() {}
						});
					}
				}
				this.$nextTick(() => {
					this.scroll = new BScroll(this.$refs.ratings, {
						click: true
					});
				});
			} else {
				this.$vux.alert.show({
					title: '暂无订单',
					content: '请你点餐',
					onHide() {}
				});
			}
		});
	},
	created() {
		this.$store.state.headerShow = true;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo == null) {
			this.$router.replace('/login');
		}
		const url = this.$store.state.apiUrl+"/order?id="+ this.userInfo.id;
		this.$http.get(url).then(response => {
			response = response.body;
			if (response.success) {
				this.ratings = response.data;
				this.$nextTick(() => {
					this.scroll = new BScroll(this.$refs.ratings, {
						click: true
					});
				});
			} else {
				this.$vux.alert.show({
					title: '暂无订单',
					content: '请你点餐',
					onHide() {}
				});
			}
		});
	},
	filters: {
		formatDate(time) {
			let date = new Date(time);
			/* 这里调用js  date.js里面的formateDate方法*/
			return formatDate(date, 'yyyy-MM-dd hh:mm');
		}
	},
	methods: {
		//显示订单内容
		showOrder(event, state) {
			this.orderShow = true;
			this.state = state;
		},
		/* 从子组件里面传来 ratingselect组件 修改selectType,并刷新scroll */
		selectRating(type) {
			this.selectType = type;
			this.$nextTick(() => {
				this.scroll.refresh();
			});
		},
		toggleContent() {
			this.onlyContent = !this.onlyContent;
			this.$nextTick(() => {
				this.scroll.refresh();
			});
		}
	},
	components: {
		star,
		XButton,
		split,
		Group,
		Popup,
		XInput,
		TransferDom,
		Flow,
		FlowState,
		FlowLine
	}
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl";
	.ratings
		position:absolute;
		top:174px;
		bottom:0;
		width: 100%;
		overflow: hidden;
		.overview
			display:flex;
			padding:18px 0px;
			.overview-left
				flex: 0 0 137px;
				padding: 6px 0;
				width: 137px;
				border-right: 1px solid rgba(7, 17, 27, 0.1);
				text-align: center;
				/* 这里还加了给小屏幕的手机做了适配的 因为小屏幕手机没有这么宽 */
				@media only screen and (max-height:320px)
					flex:0 0 120px;
					width:120px;
				.score
					margin-bottom:6px;
					font-size:24px;
					line-height:28px;
					color:rgb(255,153,0);
				.title
					margin-bottom:8px;
					line-height:12px;
					color:rgb(7,17,27);
					font-size:12px;
				.rank
					line-height:10px;
					font-size:10px;
					color:rgb(147,153,159);
			.overview-right
				flex:1;
				padding:6px 0 6px 24px;
				@media only screen and (max-height:320px)
					padding-left:6px;
				.scroe-wrapper
					margin-bottom:8px;
					font-size:0;
					.title
						display: inline-block
						line-height: 18px
						vertical-align: top
						font-size: 12px
						color: rgb(7, 17, 27)
					.star
						display: inline-block
						margin: 0 12px
						vertical-align: top
					.score
						display: inline-block
						line-height: 18px
						vertical-align: top
						font-size: 12px
						color: rgb(255, 153, 0)
				.delivery-wrapper
					font-size: 0
					.title
						line-height: 18px
						font-size: 12px
						color: rgb(7, 17, 27)
					.delivery
						margin-left: 12px
						font-size: 12px
						color: rgb(147, 153, 159)
		.rating-wrapper
			padding:0 18px;
			.rating-item
				display:flex;
				padding:18px 0;
				border-1px(rgba(7, 17, 27, 0.1));
				.avatar
					flex:0 0 28px;
					width:28px;
					margin-right:12px;
					img
						border-radius:50%;
				.content
					position:relative
					flex:1;
					.name
						margin-bottom: 4px
						line-height: 12px
						font-size: 10px
						color: rgb(7, 17, 27)
					.star-wrapper
						float:right;
						margin-bottom: 6px
						font-size: 0
						.delivery
							display: inline-block
							vertical-align: top
							line-height: 12px
							font-size: 10px
							color: #000;
					.text
						margin-bottom: 8px
						line-height: 18px
						color: rgb(7, 17, 27)
						font-size: 12px
					.recommend
						line-height: 16px
						font-size: 0
						.icon-thumb_up, .item
							display: inline-block
							margin: 0 8px 4px 0
							font-size: 9px
						.icon-thumb_up
							color: rgb(0, 160, 220)
						.item
							padding: 0 6px
							border: 1px solid rgba(7, 17, 27, 0.1)
							border-radius: 1px
							color: rgb(147, 153, 159)
							background: #fff
					.time
						position: absolute
						top: 0
						right: 0
						line-height: 12px
						font-size: 10px
						color: rgb(147, 153, 159)
</style>
