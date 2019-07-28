<template>
	<!-- 因为 还需要给购物车详细页添加一个 背景图片 所以添加了一个div层 -->
	<div>
		<!-- 购物车组件 -->
		<div class="shopcart">
			<!-- 购物车主要内容 因为还要加购物车的动画 所以这样分结构 -->
			<div class="content" @click="toggleList">
				<!-- 左侧购物车图标和配送费用等 -->
				<div class="content-left">
					<!-- logo -->
					<div class="logo-wrapper">
						<div class="logo" :class="{ highlight: totalCount > 0 }"><span class="icon-shopping_cart" :class="{ highlight: totalCount > 0 }"></span></div>
						<!-- 当用户选择了商品后 购物车图标会进行改变 并显示出选择了几件商品 -->
						<div class="num" v-show="totalCount > 0">{{ totalCount }}</div>
					</div>
					<!-- 价格 -->
					<div class="price" :class="{ highlight: totalCount > 0 }">￥{{ totalPrice }}元</div>
				</div>
				<!-- 右侧支付按钮 -->
				<div class="content-right" @click.stop.prevent="pay">
					<div class="pay" :class="payClass">{{ payDesc }}</div>
				</div>
			</div>
			<!-- 装小球的容器 用于制作小球从按钮做抛物线的动画 -->
			<div class="ball-container">
				<div v-for="(ball, index) in balls" :key="index">
					<!-- 三个动画 进入之前 进入时 和进入后 -->
					<transition name="drop" @before-enter="beforeDrop" @enter="dropping" @after-enter="afterDrop">
						<div class="ball" v-show="ball.show">
							<!-- 内层做x轴的运动 -->
							<div class="inner inner-hook"></div>
						</div>
					</transition>
				</div>
			</div>
			<!-- 购物车详细  还要加上一个折叠fold的动画-->
			<transition name="fold">
				<div class="shopcart-list" v-show="listShow">
					<!-- 列表头 -->
					<div class="list-header">
						<div class="title">购物车</div>
						<span class="empty" @click="empty">清空</span>
					</div>
					<!-- 列表内容 显示的是已被选择的商品 -->
					<div class="list-content" ref="listContent">
						<ul>
							<li class="food" v-for="(food, index) in selectFoods" :key="index">
								<!-- 商品名字 -->
								<span class="name">{{ food.name }}</span>
								<!-- 商品价格 -->
								<div class="price">
									<span>￥{{ food.price * food.count }}</span>
								</div>
								<!-- 按钮控件 -->
								<div class="cartcontroll-wrapper">
									<!-- 触发抛物线动画  -->
									<cartcontroll @add="addFood" :food="food"></cartcontroll>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</transition>
		</div>
		<!-- 打开详细页面的蒙层 背景图片 -->
		<transition name="fade"><div class="list-mask" @click="hideList" v-show="listShow"></div></transition>
	</div>
</template>

<script>
/* eslint-disable */
import BScroll from 'better-scroll';
import cartcontroll from 'components/cartcontroll/cartcontroll.vue';
import { getStore, setStore, removeStore } from 'common/js/mUtils.js';

export default {
	data() {
		return {
			/* 初始化小球数组用于做抛物线运动  初始状态都是false  都是隐藏的*/
			balls: [
				{
					show: false
				},
				{
					show: false
				},
				{
					show: false
				},
				{
					show: false
				},
				{
					show: false
				}
			],
			/* 跟踪正在下落的小球 */
			dropBalls: [],
			/* 购物车详细页默认是折叠的 */
			fold: true
		};
	},
	props: {
		deliveryPrice: {
			type: Number,
			default: 0
		},
		minPrice: {
			type: Number,
			default: 0
		},
		/* 从goods组件里面传过来的告诉我们选择了多少商品  */
		selectFoods: {
			type: Array,
			/* 如果他的类型是一个数组 那么他的defaul就是一个函数  */
			default() {
				return [
					{
						price: 0,
						count: 1
					}
				];
			}
		}
	},
	methods: {
		/* 从cartcontroll里面一层一层传递过来的 */
		drop(el) {
			/* 拿到 balls 为false的球 做一个动画就可以了 */
			for (var i = 0; i < this.balls.length; i++) {
				let ball = this.balls[i];
				if (!ball.show) {
					ball.show = true;
					ball.el = el;
					this.dropBalls.push(ball);
					return;
				}
			}
		},
		/* 跳落之前的动作 得到小球的初始位置 一开始就把小球设置在点击处 */
		beforeDrop(el) {
			/* 得到所有小球 */
			let count = this.balls.length;
			while (count--) {
				let ball = this.balls[count];
				/* 得到一个为show的小球,对他进行抛物线的运动 */
				if (ball.show) {
					/* 注意这个小球的el 是从drop这个方法里面加入的 */
					/* 得到小球的初始位置 */
					let rect = ball.el.getBoundingClientRect();
					/* 其实小球的初始位置是在按钮的左上方  */
					let x = rect.left - 32;
					let y = -(window.innerHeight - rect.top - 22);
					/* 这个都是外层 设置外层是按照y轴运动 做曲线运动*/

					/* 设置初始值 */
					el.style.display = '';
					el.style.webkitTransform = `translate3d(0,${y}px,0)`;
					el.style.transform = `translate3d(0,${y}px,0)`;
					/* 让内层按照 x轴运动 记得给内层加上inner-hook的class x轴做匀速直线运动*/
					let inner = el.getElementsByClassName('inner-hook')[0];
					inner.style.webkitTransform = `translate3d(${x}px,0,0)`;
					inner.style.transform = `translate3d(${x}px,0,0)`;
				}
			}
		},
		/* enter 进入的时候 就是进入购物车的时候  */
		dropping(el, done) {
			/* eslint-disable no-unused-vars */
			/* 还需要写dropping 进入的时候 主动触发浏览器 重绘 还需要加上注解 因为这个rf我们是不用的 */
			let rf = el.offsetHeight;
			this.$nextTick(function() {
				el.style.webkitTransform = 'translate3d(0,0,0)';
				el.style.transform = 'translate3d(0,0,0)';
				let inner = el.getElementsByClassName('inner-hook')[0];
				inner.style.webkitTransform = 'translate3d(0,0,0)';
				inner.style.transform = 'translate3d(0,0,0)';
				el.addEventListener('transitionend', done);
			});
		},
		/* 当整个下落完成后 将小球隐藏 */
		afterDrop(el) {
			/* dropBalls里面的show都是true的 */
			let ball = this.dropBalls.shift();
			if (ball) {
				ball.show = false;
				el.style.display = 'none';
			}
		},
		/* 点击购物车展开详细页 */
		toggleList() {
			/* 当没有选择任何商品的时候 不展开 */
			if (!this.totalCount) {
				return;
			}
			this.fold = !this.fold;
		},
		/* 清空购物车 */
		empty() {
			/* 遍历所有被选择的food  */
			this.selectFoods.forEach(food => {
				food.count = 0;
			});
		},
		/* 点击list-mask 隐藏购物车详细页 */
		hideList() {
			this.fold = true;
		},
		pay(event) {
			if (!event.constructor) {
				return;
			}
			if (this.totalPrice <= 0) {
				return;
			}
			//添加订单
			const url = this.$store.state.submitOrderUrl;
			var foodids = new Array();

			this.selectFoods.forEach(food => {
				foodids.push(food.foodid + '&' + food.count);
			});

			this.userInfo = getStore('userInfo');
			if (this.userInfo != null) {
				this.userInfo = JSON.parse(this.userInfo);
			}
			let data = {
				foods: foodids.toString(),
				userid: this.userInfo.id
			};

			this.$http.post(url, data).then(
				response => {
					response = response.body;
					if (response.success) {
						this.$vux.alert.show({
							title: '成功点餐',
							content: '请跳转到订单页面',
							onHide() {}
						});
					} else {
						this.$vux.alert.show({
							title: '点餐失败',
							content: '请检查是否支付成功',
							onHide() {}
						});
					}
				},
				response => {
					this.$vux.alert.show({
						title: '点餐错误',
						content: '请重新点餐',
						onHide() {}
					});
				}
			);
		},
		addFood(target) {
			this.drop(target);
		}
	},
	computed: {
		/* 计算出总价 根据selectFoods数组 */
		totalPrice() {
			let total = 0;
			this.selectFoods.forEach(food => {
				total += food.price * food.count;
			});
			return total;
		},
		/* 计算出用户选择商品的总数 */
		totalCount() {
			let count = 0;
			this.selectFoods.forEach(food => {
				count += food.count;
			});
			return count;
		},
		/* 计算出总价是否小于派送费用 支付按钮的结构*/
		payDesc() {
			if (this.totalPrice == 0) {
				return `请您点餐`;
			} else {
				return '去支付';
			}
		},
		/* 计算出 pay的样式  */
		payClass() {
			if (this.totalPrice <= 0) {
				return 'not-enough';
			} else {
				return 'enough';
			}
		},
		/* 用于 购物车详细页是否展开 */
		listShow() {
			/* 这里代表 如果没有任何商品的时候 就是折叠状态 */
			if (!this.totalCount) {
				this.fold = true;
				return false;
			}
			let show = !this.fold;
			/* 给详细页初始化 滚动插件 betterScroll */
			if (show) {
				this.$nextTick(function() {
					/* 防止多次 初始化 需要进行判断有没有scroll */
					if (!this.scroll) {
						this.scroll = new BScroll(this.$refs.listContent, {
							click: true
						});
					} else {
						/* refresh是重新计算高度差 进行滚动 */
						this.scroll.refresh();
					}
				});
			}
			return show;
		}
	},
	components: {
		cartcontroll
	}
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl";

.shopcart
	/* fixed布局 一值固定在视口 下面 */
	position:fixed;
	left:0;
	bottom:0;
	z-index:50;
	width:100%;
	height:48px;
	.content
		/* 因为右侧的按钮是固定的 左侧是自适应 所以用到flex */
		display:flex;
		background:#141d27;
		font-size: 0;
		color: rgba(255, 255, 255, 0.4);
		.content-left
			flex:1;
			/* 左排列布局 */
			.logo-wrapper
				display:inline-block;
				position:relative;
				top:-10px;
				margin:0 12px;
				padding:6px;
				width:56px;
				height:56px;
				/* 设置成盒子模型 设置成⚪ 注意这里并不是图片出来了 而是利用box
				画出来的一个圆 到时候logo定位到这个位置 */
				box-sizing:border-box;
				vertical-align:top;
				border-radius:50%;
				background:#141d27;
				.logo
					width:100%;
					height:100%;
					border-radius:50%;
					text-align:center;
					background:#2b343c;
					&.highlight
						background:rgb(0,160,220);
					.icon-shopping_cart
						line-height:44px;
						font-size:24px;
						color:#80858a;
						&.highlight
							color:#fff;
				.num
					position:absolute;
					top:0;
					right:0;
					text-align:center;
					width:24px;
					height:16px;
					font-size:9px;
					font-weight:700;
					color:#fff;
					line-height:16px;
					border-radius: 16px;
					background: rgb(240, 20, 20);
					box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.4);
			.price
				display: inline-block
				vertical-align: top
				margin-top: 12px
				line-height: 24px
				padding-right: 12px
				box-sizing: border-box
				border-right: 1px solid rgba(255, 255, 255, 0.1)
				font-size: 16px
				font-weight: 700
				&.highlight
					color:#fff;
			.desc
				display: inline-block;
				vertical-align: top;
				margin: 12px 0 0 12px;
				line-height: 24px;
				font-size: 12px;

		.content-right
			flex:0 0 105px;
			width:105px;
			.pay
				height:48px;
				line-height:48px;
				color:rgba(255,255,255,0.4);
				font-size:12px;
				font-weight:700;
				text-align: center;
				background: #2b333b;
				&.enough
					background:#00b43c;
					color:#fff;
				&.not-enough
					background: #2b333b;
	.ball-container
		.ball
			/* 这个小球一开始是在 购物车里面的 */
			position:fixed;
			left: 32px;
			bottom: 22px;
			z-index:200;
			/* 做的是bezier的曲线运动 */
			transition: all 0.4s cubic-bezier(0.49, -0.29, 0.75, 0.41);
			.inner
				/* 定义小球的宽高 */
				width:16px;
				height:16px;
				border-radius:50%;
				background: rgb(0, 160, 220);
				transition: all 0.4s linear;
	.shopcart-list
		/* 使用绝对定位 直接定位到购物车 相对于最外层的shopcart */
		position:absolute;
		top:0;
		left:0;
		/* 防止覆盖掉购物车的 层 */
		z-index:-1;
		width:100%;
		/* 平移动画 一开始是在最下面*/
		transform: translate3d(0, -100%, 0);
		/* 给进入和离开的时候 添加动画 */
		&.fold-enter-active, &.fold-leave-active
			transition: all 0.5s;
		&.fold-enter, &.fold-leave-active
			transform: translate3d(0, 0, 0);
		.list-header
			height:40px;
			line-height:40px;
			padding:0 18px;
			background: #f3f5f7;
			border-bottom: 1px solid rgba(7, 17, 27, 0.1);
			.title
				float: left
				font-size: 14px
				color: rgb(7, 17, 27)
			.empty
				float: right
				font-size: 12px
				color: rgb(0, 160, 220);
		/* 列表内容 */
		.list-content
			padding: 0 18px
			max-height: 217px
			overflow: hidden
			background: #fff
			.food
				position: relative
				padding: 12px 0
				box-sizing: border-box
				border-1px(rgba(7, 17, 27, 0.1));
				.name
					line-height:24px;
					font-size:14px;
					color: rgb(7, 17, 27);
				.price
					position:absolute;
					right:90px;
					bottom: 12px;
					line-height: 24px;
					font-size: 14px;
					font-weight: 700;
					color: rgb(240, 20, 20);
				.cartcontroll-wrapper
					position:absolute;
					right:0;
					bottom:6px;
.list-mask
	/* 装满视口 */
	position:fixed;
	top: 0
	left: 0
	width: 100%
	height: 100%
	/* 不能把购物车遮住 */
	z-index: 40
	backdrop-filter: blur(10px);
	opacity: 1
	background: rgba(7, 17, 27, 0.6)
	&.fade-enter-active, &.fade-leave-active
		transition: all 0.5s
	&.fade-enter, &.fade-leave-active
		opacity: 0
		background: rgba(7, 17, 27, 0)
</style>
