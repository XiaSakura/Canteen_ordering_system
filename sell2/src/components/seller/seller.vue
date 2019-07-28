<template>
	<!-- 商家页 -->
	<div class="seller" ref="seller">
		<!-- 商家内容 -->
		<div class="seller-content">
			<!-- 概览 包括商家名字和描述 -->
			<div class="overview">
				<!-- 商家名字 -->
				<div class="title">{{ seller.name }}</div>
				<!-- 商家的描述 星级 月售多少单 -->
				<div class="desc">
					<star :size="36" :score="seller.score"></star>
					<!-- 排名 ratingCount-->
					<span class="text">排名{{ seller.ratingCount }}</span>
					<!-- 月售多少单 -->
					<span class="text">月售{{ seller.sellCount }}单</span>
				</div>
				<!-- 利用flex布局 起送价,商家配送,平均配送时间 -->
				<ul class="remark">
					<li class="block">
						<h2>平均点餐价格</h2>
						<div class="content">
							<span class="stress">{{ seller.deliveryPrice }}</span>
							元
						</div>
					</li>
					<li class="block">
						<h2>平均点餐时间</h2>
						<div class="content">
							<span class="stress">{{ seller.deliveryTime }}</span>
							分钟
						</div>
					</li>
				</ul>
				<!-- 收藏按钮 绝对定位 -->
				<div class="favorite" @click="toggleFavorite">
					<span class="icon-favorite" :class="{ active: favorite }"></span>
					<span class="text">{{ favoriteText }}</span>
				</div>
			</div>
			<split></split>
			<!-- 商家公告和活动 -->
			<div class="bulletin">
				<h1 class="title">公告和活动</h1>
				<div class="content-wrapper">
					<p class="content">{{ seller.bulletin }}</p>
				</div>
				<!-- 支持的活动 -->
				<ul v-if="seller.supports" class="supports">
					<!-- 遍历出商家支持的活动 -->
					<li class="support-item border-1px" v-for="(item, index) in seller.supports" :key="index">
						<!-- 活动图标 -->
						<span class="icon" :class="classMap[seller.supports[index].type]"></span>
						<!-- 活动文字 -->
						<span class="text">{{ seller.supports[index].description }}</span>
					</li>
				</ul>
			</div>
			<split></split>
			<!-- 商家实景图 -->
			<div class="pics">
				<h1 class="title">商家实景</h1>
				<!-- 图片 这里还实现了滚动效果 ul外面有层 pic-wrapper-->
				<!-- 原因是 要被横向滚动  利用wrapper滚动视口的大小 当图片数量超过视口大小的时候 就可以横向滚动 * 图片本身超出视口大小是要被隐藏的 * 内层的宽度要大于外层的宽度 才会被滚动 -->
				<div class="pic-wrapper" ref="picWrapper">
					<ul class="pic-list" ref="picList">
						<li class="pic-item" v-for="(pic, index) in seller.pics" :key="index"><img :src="pic" width="120px" height="90px" /></li>
					</ul>
				</div>
			</div>
			<split></split>
			<!-- 商家信息 -->
			<div class="info">
				<h1 class="title">商家信息</h1>
				<ul>
					<li class="info-item" v-for="(info, index) in seller.infos" :key="index">{{ info }}</li>
				</ul>
			</div>
		</div>
	</div>
</template>

<script>
/* eslint-disable */
import star from 'components/star/star.vue';
import split from 'components/split/split.vue';
import BScroll from 'better-scroll';
import { saveToLocal, loadFromLocal } from 'common/js/store.js';
import { getStore, setStore, removeStore } from 'common/js/mUtils.js';
export default {
	props: {
		seller: {
			type: Object
		}
	},
	activated() {
		this.$store.state.headerShow = true;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo == null) {
			this.$router.replace('/login');
		}
		this.$nextTick(function() {
			/* 初始化滚动 */
			this._initScroll();
			this._initPics();
		});
	},
	data() {
		return {
			favorite: (() => {
				return loadFromLocal(this.seller.id, 'favorite', false);
			})()
		};
	},
	computed: {
		/* 根据favorite 显示文字 已收藏还是未收藏 */
		favoriteText() {
			return this.favorite ? '已收藏' : '未收藏';
		}
	},
	created() {
		this.$store.state.headerShow = true;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo == null) {
			this.$router.replace('/login');
		}
		/* 定义一个classmap 就可以 因为对应接口数据里面的 type是数字 0,1,2,3 */
		this.classMap = ['decrease', 'discount', 'special', 'invoice', 'guarantee'];
	},
	/* 观测数据 发生改变时触发 */
	/* mounted和watch 当视图来回切换 例如商家和商品之间进行切换 watch是不会被触发的 但是mounted会被触发  */
	watch: {
		seller() {
			this.$nextTick(function() {
				/* 初始化滚动 */
				this._initScroll();
				this._initPics();
			});
		}
	},
	/* 当html完全挂载到页面后会执行的事件钩子*/
	mounted() {
		this.$nextTick(function() {
			/* 初始化滚动 */
			this._initScroll();
			this._initPics();
		});
	},
	methods: {
		_initScroll() {
			/* 判断有没有初始化 防止多次初始化 */
			if (!this.scroll) {
				this.scroll = new BScroll(this.$refs.seller, {
					click: true
				});
			} else {
				this.scroll.refresh();
			}
		},
		/* 初始化图片横向滚动 */
		_initPics() {
			if (this.seller.pics) {
				let picWidth = 120;
				let margin = 6;
				/* 由于这里是横向滚动 所以要计算的是宽度 所以在这里 先要设置ul的宽度 picList就是ul
				 * 减去最后一个的margin*/
				let width = (picWidth + margin) * this.seller.pics.length - margin;

				/* 设置ul的宽度 要大于外层的宽度 */
				this.$refs.picList.style.width = width + 'px';
				this.$nextTick(function() {
					if (!this.picScroll) {
						this.picScroll = new BScroll(this.$refs.picWrapper, {
							scrollX: true,
							/* eventPassthrough目的就是让我们在横向滚动的时候 忽略垂直方向的滚动  */
							eventPassthrough: 'vertical'
						});
					} else {
						this.picScroll.refresh();
					}
				});
			}
		},
		/* 收藏或者不收藏 */
		toggleFavorite(event) {
			if (!event.constructor) {
				return;
			}
			this.favorite = !this.favorite;
			/* 利用localStore存储 */
			saveToLocal(this.seller.id, 'favorite', this.favorite);
		}
	},
	components: {
		star,
		split,
		BScroll
	}
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl";
	.seller
		position:absolute;
		top:174px;
		bottom:0;
		width: 100%;
		overflow: hidden;
		.overview
			position: relative
			padding: 18px
			.title
				margin-bottom: 8px
				line-height: 14px
				color: rgb(7, 17, 27)
				font-size: 14px
			.desc
				padding-bottom: 18px
				border-1px(rgba(7, 17, 27, 0.1))
				font-size: 0
				.star
					display: inline-block
					margin-right: 8px
					vertical-align: top
				.text
					display: inline-block
					margin-right: 12px
					line-height: 18px
					vertical-align: top
					font-size: 10px
					color: rgb(77, 85, 93)
			.remark
				display: flex
				padding-top: 18px
				.block
					flex: 1
					text-align: center
					border-right: 1px solid rgba(7, 17, 27, 0.1)
					&:last-child
						border:none;
					h2
						margin-bottom: 4px
						line-height: 10px
						font-size: 10px
						color: rgb(147, 153, 159)
					.content
						line-height: 24px
						font-size: 10px
						color: rgb(7, 17, 27)
						.stress
							font-size: 24px;
			.favorite
				position: absolute
				width: 50px
				right: 11px
				top: 18px
				text-align: center
				.icon-favorite
					display: block
					margin-bottom: 4px
					line-height: 24px
					font-size: 24px
					color: #d4d6d9
					&.active
						color:rgb(240,20,20)
				.text
					line-height: 10px
					font-size: 10px
					color: rgb(77, 85, 93)
		.bulletin
			padding: 18px 18px 0 18px
			.title
				margin-bottom: 8px
				line-height: 14px
				color: rgb(7, 17, 27)
				font-size: 14px
			.content-wrapper
				padding: 0 12px 16px 12px
				border-1px(rgba(7, 17, 27, 0.1))
				.content
					line-height: 24px
					font-size: 12px
					color: rgb(240, 20, 20)
			.supports
				.support-item
					padding: 16px 12px
					border-1px(rgba(7, 17, 27, 0.1))
					font-size: 0
					&:last-child
						border-none();
				.icon
					display: inline-block
					width: 16px
					height: 16px
					vertical-align: top
					margin-right: 6px
					background-size: 16px 16px
					background-repeat: no-repeat
					&.decrease
						bg-image('decrease_4')
					&.discount
						bg-image('discount_4')
					&.guarantee
						bg-image('guarantee_4')
					&.invoice
						bg-image('invoice_4')
					&.special
						bg-image('special_4')
				.text
					line-height: 16px
					font-size: 12px
					color: rgb(7, 17, 27)
		.pics
			padding: 18px;
			.title
				margin-bottom: 12px
				line-height: 14px
				color: rgb(7, 17, 27)
				font-size: 14px
			.pic-wrapper
				width: 100%
				overflow: hidden
				white-space: nowrap
				.pic-list
					font-size:0;
					.pic-item
						display: inline-block
						margin-right: 6px
						width: 120px
						height: 90px
					&:last-child
						margin:0
		.info
			padding: 18px 18px 0 18px
			color: rgb(7, 17, 27);
			.title
				padding-bottom: 12px
				line-height: 14px
				border-1px(rgba(7, 17, 27, 0.1))
				font-size: 14px
			.info-item
				padding: 16px 12px
				line-height: 16px
				border-1px(rgba(7, 17, 27, 0.1))
				font-size: 12px
				&:last-child
					border-none();
</style>
