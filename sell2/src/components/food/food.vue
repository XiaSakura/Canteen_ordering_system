<template>
	<!-- 进入详情页的动画 -->
	<transition name="move">
		<div v-show="showFlag" class="food" ref="food">
			<!-- 商品内容 -->
			<div class="food-content">
				<!-- 商品大图 -->
				<div class="image-header">
					<img :src="foodimage(food)" />
					<!-- 返回按钮 -->
					<div class="back" @click="hide"><i class="icon-arrow_lift"></i></div>
				</div>
				<!-- 商品内容 -->
				<div class="content">
					<!-- 标题 -->
					<div class="title">{{ food.name }}</div>
					<!-- 月售和好评率 -->
					<div class="detail">
						<span class="sell-count">月售{{ food.sellCount }}</span>
						<span class="rating">好评率{{ food.rating }}</span>
					</div>
					<!-- 价格 -->
					<div class="price">
						<!-- 现价 -->
						<span class="now">￥{{ food.price }}</span>
						<!-- 打折之前的价钱 有可能没有打折 -->
						<span class="old" v-show="food.oldPrice">￥{{ food.oldPrice }}</span>
					</div>
					<!-- 购物车按钮 加入购物车-->
					<div class="cartcontroll-wrapper"><cartcontroll @add="addFood" :food="food"></cartcontroll></div>
					<!-- 这有这两种情况下 购物车才会显示 没有count属性或者count为0的时候 才会显示 加入购物车-->
					<!-- 这里需要加动画原因  -->
					<!-- 因为这个加入购物车的按钮 点击之后立马被隐藏了,获取不到元素,因此就得不到小球的起始位置了. 解决办法可以加一个动画 让他不会被立马隐藏 -->
					<transition name="fade"><div @click.stop.prevent="addFirst" class="buy" v-show="!food.count || food.count === 0">加入购物车</div></transition>
				</div>
				<split v-show="food.info"></split>
				<!-- 商品介绍 -->
				<div class="info" v-show="food.info">
					<h1 class="title">商品信息</h1>
					<p class="text">{{ food.info }}</p>
				</div>
				<split></split>
				<!-- 商品评论的组件 -->
				<div class="rating">
					<h1 class="title">商品评价</h1>
					<ratingselect
						:foodid="food.foodid"
						:selectType="selectType"
						:onlyContent="onlyContent"
						:ratings="food.ratings"
						:desc="desc"
						@select="selectRating"
						@toggle="toggleContent"
					></ratingselect>
					<!-- 评价列表 -->
					<div class="rating-wrapper border-1px">
						<!-- 有两种情况 有评论的情况和没有评论的情况  -->
						<ul v-show="food.ratings && food.ratings.length">
							<li v-for="(rating, index) in food.ratings" :key="index" class="rating-item" v-show="needShow(rating.rateType, rating.text)">
								<!-- 用户模块 头像和用户名 -->
								<div class="user">
									<span class="name">{{ rating.username }}</span>
									<img class="avatar" :src="ratingAvatar(rating)" width="12px" height="12px" />
								</div>
								<!-- 发表时间 注意这里是时间戳 需要经过转换成时间字符串 -->
								<!-- fliter的写法 加上一条| 再写filter的名字  -->
								<div class="time">{{ rating.rateTime | formatDate }}</div>
								<!-- 评论内容 -->
								<p class="text">
									<span :class="{ 'icon-thumb_up': rating.rateType === 0, 'icon-thumb_down': rating.rateType === 1 }"></span>
									{{ rating.text }}
								</p>
							</li>
						</ul>
						<!-- 没有评论的请情况 -->
						<div class="no-rating" v-show="!food.ratings || !food.ratings.length">暂无评价</div>
					</div>
				</div>
			</div>
		</div>
	</transition>
</template>

<script>
/* eslint-disable */
import BScroll from 'better-scroll';
import cartcontroll from 'components/cartcontroll/cartcontroll.vue';
import Vue from 'vue';
import split from 'components/split/split.vue';
import ratingselect from 'components/ratingselect/ratingselect.vue';
import { formatDate } from 'common/js/date';
/* ratingselect组件有说明 */
const POSITIVE = 0;
const NEGATIVE = 1;
const ALL = 2;

export default {
	props: {
		food: {
			type: Object
		}
	},
	data() {
		/* 需要观测这个变量 */
		return {
			showFlag: false,
			/* 这里为了方便追踪变量 也需要定义这几个常量 这些变量都是在ratingselect*/
			selectType: 2,
			onlyContent: true,
			/* 这里定义并修改描述 不然默认是 全部,满意,不满意 */
			desc: {
				all: '全部',
				positive: '推荐',
				negative: '吐槽'
			}
		};
	},
	methods: {
		foodimage(food) {
			return this.$store.state.imgUrl + '/' + food.image;
		},
		ratingAvatar(rating) {
			return this.$store.state.imgUrl + '/' + rating.avatar;
		},
		/* 这个方法需要在父组件里面调用,显示food详情页 */
		show() {
			this.showFlag = true;
			/* 初始化变量 */
			this.selectType = ALL;
			this.onlyContent = true;
			/* 初始化Better-scroll插件 */
			this.$nextTick(function() {
				if (!this.scroll) {
					this.scroll = new BScroll(this.$refs.food, {
						click: true
					});
				} else {
					this.scroll.refresh();
				}
			});
		},
		hide() {
			this.showFlag = false;
		},
		addFirst(event) {
			if (!event.constructor) {
				return;
			}
			/* 加上这个 就可以做抛物线动画 */
			/* 子组件可以使用 $emit 触发父组件的自定义事件。注意在goods组件里面是 @add="addFood" 触发小球动画*/
			this.$emit('add', event.target);
			Vue.set(this.food, 'count', 1);
		},
		/* 从cartcontroll里面传过来 调用goods组件 */
		addFood(target) {
			/* 子组件可以使用 $emit 触发父组件的自定义事件。注意在goods组件里面是 @add="addFood" 触发小球动画*/
			this.$emit('add', target);
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
		},
		/* type和text 是否有内容和评价类型 判断这条评论是否显示
		 * 判断是否是需要的评价类型 才显示评论*/
		needShow(type, text) {
			/* 当需要内容 但是没有内容的时候 不显示 */
			if (this.onlyContent && !text) {
				return false;
			}
			if (this.selectType === ALL) {
				return true;
			} else {
				return type === this.selectType;
			}
		}
	},
	filters: {
		formatDate(time) {
			let date = new Date(time);
			/* 这里调用js  date.js里面的formateDate方法*/
			return formatDate(date, 'yyyy-MM-dd hh:mm');
		}
	},
	components: {
		cartcontroll,
		split,
		ratingselect
	}
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
	@import "../../common/stylus/mixin.styl";
.food
	position:fixed;
	left:0;
	top:0;
	bottom:48px;
	z-index:30;
	width:100%;
	background:#fff;
	transform:translate3d(0,0,0);
	/* 先给进入和离开添加transition  */
	&.move-enter-active,&.move-leave-active
		transition:all 0.5s linear;
	&.move-enter, &.move-leave-active
		transform: translate3d(100%, 0, 0);
	.image-header
		/* img相对于header绝对布局 */
		position:relative;
		/* 因为这个图片太大了 加载需要时间这里先设置height为0 */
		width:100%;
		height:0;
		/* 这里使用了padding-top 相当于就是width的高度 就是一个宽高相等的容器 */
		padding-top:100%;
		img
			position:absolute;
			top:0;
			left:0;
			width:100%;
			height:100%;
		.back
			position: absolute;
			top:10px;
			left:0
			/* 为了扩大可以点击的范围所以 这样设计的 */
			.icon-arrow_lift
				display:inline-block;
				/* 因为上面的back 没有写宽高 所以这里的padding 相当于扩大了点击范围 */
				padding:10px;
				font-size:20px;
				color:#fff;
	.content
		position:relative;
		padding:18px;
		.title
			margin-bottom:8px;
			font-size:14px;
			font-weight:700;
			color:rgb(7,17,27);
			line-height:14px;
		.detail
			margin-bottom: 18px;
			line-height: 10px;
			height: 10px;
			font-size: 0;
			.sell-count, .rating
				font-size: 10px;
				color: rgb(147, 153, 159);
			.sell-count
				margin-right: 12px;
		.price
			font-weight: 700
			line-height: 24px
			.now
				margin-right: 8px
				font-size: 14px
				color: rgb(240, 20, 20)
			.old
				text-decoration: line-through
				font-size: 10px
				color: rgb(147, 153, 159)
		.cartcontroll-wrapper
			position: absolute
			right: 12px
			bottom: 12px
		.buy
			position: absolute
			right: 18px
			bottom: 18px
			z-index: 10
			height: 24px
			line-height: 24px
			padding: 0 12px
			box-sizing: border-box
			border-radius: 12px
			font-size: 10px
			color: #fff
			background: rgb(0, 160, 220)
			opacity: 1
			&.fade-enter-active,&.fade-leave-active
				transition:all 0.2s;
			&.fade-enter,&.fade-leave-active
				opacity:0
				z-index: -1
	.info
		padding:18px;
		.title
			margin-bottom:6px;
			font-size:14px;
			color:rgb(7,17,27);
			line-height:14px;
		.text
			line-height: 24px
			padding: 0 8px
			font-size: 12px
			color: rgb(77, 85, 93);
	.rating
		padding-top:18px;
		.title
			line-height: 14px
			margin-left: 18px
			font-size: 14px
			color: rgb(7, 17, 27)
		.rating-wrapper
			padding: 0 18px
			.rating-item
				position: relative
				padding: 16px 0
				border-1px(rgba(7, 17, 27, 0.1));
				.user
					position: absolute
					right: 0
					top: 16px
					line-height: 12px
					font-size: 0
					.name
						display: inline-block
						margin-right: 6px
						vertical-align: top
						font-size: 10px
						color: rgb(147, 153, 159)
					.avatar
						display: inline-block
						border-radius: 50%
				.time
					margin-bottom: 6px
					line-height: 12px
					font-size: 10px
					color: rgb(147, 153, 159)
				.text
					line-height: 16px
					font-size: 12px
					color: rgb(7, 17, 27)
					.icon-thumb_up, .icon-thumb_down
						margin-right: 4px
						line-height: 16px
						font-size: 12px
					.icon-thumb_up
						color: rgb(0, 160, 220)
					.icon-thumb_down
						color: rgb(147, 153, 159)
			.no-rating
				padding: 16px 0
				font-size: 12px
				color: rgb(147, 153, 159)
</style>
