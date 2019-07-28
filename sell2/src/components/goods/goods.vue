<template>
	<!-- 因为有一层不属于goods 是food商品详情页 所以还需要加上一层 -->
	<div>
		<div class="goods">
			<!-- 左侧菜单 menu -->
			<div class="menu-wrapper" ref="menuWrapper">
				<!-- 遍历左侧菜单 -->
				<ul>
					<li
						v-for="(item, index) in goods"
						@click="selectMenu(index, $event)"
						class="menu-item"
						:key="index"
						:class="{ current: currentIndex === index }"
						ref="menuList"
					>
						<span class="text border-1px">
							<!-- 如果 他的type大于0  就有对应图片 icon 就是显示特价那些icon -->
							<span class="icon" :class="classMap[item.type]" v-show="item.type > 0"></span>
							{{ item.name }}
						</span>
					</li>
				</ul>
			</div>
			<!-- 右侧商品栏 foods -->
			<div class="foods-wrapper" ref="foodsWrapper">
				<ul>
					<li v-for="(item, index) in goods" class="food-list" ref="foodList" :key="index">
						<!-- 每一类商品的标题 -->
						<h1 class="title">{{ item.name }}</h1>
						<ul>
							<!-- 每一类商品 -->
							<li v-for="(food, index2) in item.foods" @click="selectFood(food, $event)" class="food-item border-1px" :key="index2">
								<!-- 商品的图片 -->
								<div class="icon"><img :src="foodimage(food)" width="57" height="57" /></div>
								<!-- 商品的内容 -->
								<div class="content">
									<!-- 商品的名字和描述 -->
									<h2 class="name">{{ food.name }}</h2>
									<p class="desc">{{ food.description }}</p>
									<div class="extra">
										<span class="count">月售{{ food.sellCount }}份</span>
										<span>好评率{{ food.rating }}%</span>
									</div>
									<!-- 商品的价格 -->
									<div class="price">
										<!-- 现价 -->
										<span class="now">￥{{ food.price }}</span>
										<!-- 打折之前的价钱 有可能没有打折 -->
										<span class="old" v-show="food.oldPrice">￥{{ food.oldPrice }}</span>
									</div>
									<!-- 添加商品的按钮  -->
									<!-- 商品的按钮组件 -->
									<div class="cartcontroll-wrapper"><cartcontroll @add="addFood" :food="food"></cartcontroll></div>
								</div>
							</li>
						</ul>
					</li>
				</ul>
			</div>
			<!-- shopcart 购物车 给shopcart传入两个属性 配送费和起送费用-->
			<shopcart ref="shopcart" :selectFoods="selectFoods" :deliveryPrice="seller.deliveryPrice" :minPrice="seller.minPrice"></shopcart>
		</div>
		<food :food="selectedFood" ref="food" @add="addFood"></food>
	</div>
</template>

<script>
import BScroll from 'better-scroll';
import shopcart from 'components/shopcart/shopcart.vue';
import cartcontroll from 'components/cartcontroll/cartcontroll.vue';
import food from 'components/food/food.vue';
import { getStore, setStore, removeStore } from 'common/js/mUtils.js';
/* eslint-disable */
// 判断是否是开发环境
const debug = process.env.NODE_ENV !== 'production';
// get请求 正确的返回码
const ERROR_OK = 0;

export default {
	data() {
		return {
			goods: [],
			/* 为了知道每一个区间的高度 方便联级滚动 */
			listHeight: [],
			/* 右边实时的y值 还有左侧的索引做一个映射 */
			scrollY: 0,
			/* 获得被选中的商品,然后传给food组件 商品详情页  需要给food-item添加click事件*/
			selectedFood: {}
		};
	},
	mounted() {
		this.scroll = new BScroll(this.$refs.foodsWrapper, {
			// 下拉刷新
			pullDownRefresh: {
				// 下拉距离超过30px触发pullingDown事件
				threshold: 30,
				// 回弹停留在距离顶部20px的位置
				stop: 20
			}
		});
		this.scroll.on('pullingDown', () => {
			console.log('处理下拉刷新操作');
			const url = this.$store.state.goodsUrl;
			this.$http.get(url).then(response => {
				response = response.body;
				if (response.errno === ERROR_OK) {
					/* 注意这里必须这样写  不要这样写	this.goods = Object.assign({}, this.goods, response.data);
					 * 因为这样下面异步数据 可能请求不到数据  */
					this.goods = response.data;
					/* 因为vue更新dom对象是异步的 这里scorll插件需要修改dom对象的宽高
							因为还没有填充进入dom中 计算出来的宽高是错误的
							所以需要用到nextTick 这样才能正确的计算出menuWrap的高度 */
					this.$nextTick(() => {
						this._initScroll();
						this._calculateHeight();
					});
				}
			});
			setTimeout(() => {
				// 事情做完，需要调用此方法告诉 better-scroll 数据已加载，否则下拉事件只会执行一次
				this.scroll.finishPullDown();
			}, 2000);
		});
	},
	props: {
		/* 从App.vue传递过来的 seller 商家信息*/
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
		//必须要初始化滚动 不然当再次回来的时候 进行不会再滚动了
		this.$nextTick(() => {
			this.menuScroll = new BScroll(this.$refs.menuWrapper, {});
			this.menuScroll.refresh();
			this.foodsScroll = new BScroll(this.$refs.foodsWrapper, {});
			this.foodsScroll.refresh();
		});
	},
	created() {
		this.$store.state.headerShow = true;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo == null) {
			this.$router.replace('/login');
		}
		const url = this.$store.state.goodsUrl;
		this.$http.get(url).then(response => {
			response = response.body;
			if (response.errno === ERROR_OK) {
				/* 注意这里必须这样写  不要这样写	this.goods = Object.assign({}, this.goods, response.data);
				 * 因为这样下面异步数据 可能请求不到数据  */
				this.goods = response.data;
				/* 因为vue更新dom对象是异步的 这里scorll插件需要修改dom对象的宽高
						因为还没有填充进入dom中 计算出来的宽高是错误的
						所以需要用到nextTick 这样才能正确的计算出menuWrap的高度 */
				this.$nextTick(() => {
					this._initScroll();
					this._calculateHeight();
				});
			}
		});
		/* 定义一个classmap 就可以 因为对应接口数据里面的 type是数字 0,1,2,3 */
		this.classMap = ['decrease', 'discount', 'special', 'invoice', 'guarantee'];
	},
	computed: {
		/* 接下来左侧和右侧做映射
				需要用到vue的计算属性当我们滚动到 height1和 height2区间时
				这个currentIndex 就是i 就是让这个i的左侧菜单高亮  */
		currentIndex() {
			for (let i = 0; i < this.listHeight.length; i++) {
				let height1 = this.listHeight[i];
				let height2 = this.listHeight[i + 1];
				/* height2不为空 就是最后一片区域  */
				if (!height2 || (this.scrollY >= height1 && this.scrollY < height2)) {
					/* 跟随滚动到 相对应的位置  注意这里不是高亮 高亮在menuList的class里面的current*/
					this._followScroll(i);
					return i;
				}
			}
			return 0;
		},
		/* 在cartcontroll组件里面 修改了count的值 这个计算属性传给购物车 shopcart 得到该商品被选择的数量 */
		selectFoods() {
			let foods = [];
			this.goods.forEach(good => {
				good.foods.forEach(food => {
					if (food.count) {
						foods.push(food);
					}
				});
			});
			return foods;
		}
	},
	methods: {
		foodimage(food) {
			return this.$store.state.imgUrl + '/' + food.image;
		},
		/* 点击左侧菜单 跳转到对应的右侧商品 */
		selectMenu(index, event) {
			//console.log(index);
			/* 但是在pc页面下 出触发两次 不会被阻止的 解决办法 再添加个参数 event 就是这个点击事件 */
			/* 利用if判断 浏览器原生的是没有这个属性的 需要用BetterScroll派生  */
			if (!event._constructed) {
				return;
			}
			let foodList = this.$refs.foodList;
			let el = foodList[index];
			/* 滚动到对应的元素 在300ms内*/
			this.foodsScroll.scrollToElement(el, 300);
		},
		_initScroll() {
			/* 需要获取滚动的dom对象 注意需要使用 ref 引用标签 */
			this.menuScroll = new BScroll(this.$refs.menuWrapper, {
				/* 还需要在betterScroll初始化的时候传递属性 因为这个js插件会去掉其它默认的事件 */
				click: true
			});
			this.foodsScroll = new BScroll(this.$refs.foodsWrapper, {
				/* 这里传入参数 告诉Bscroll 滚动的时候实时的告诉我们位置 */
				click: true,
				probeType: 3
			});
			/* 监听scroll事件 修改scrollY值 ,下面需要用到计算属性currentIndex */
			this.foodsScroll.on('scroll', pos => {
				// 判断滑动方向，避免下拉时分类高亮错误（如第一分类商品数量为1时，下拉使得第二分类高亮）
				if (pos.y <= 0) {
					this.scrollY = Math.abs(Math.round(pos.y));
				}
			});
		},
		/* 从cartcontroll组件 添加商品 传过来的 */
		addFood(target) {
			/* 调用_drop 私有方法 */
			this._drop(target);
		},
		_drop(target) {
			// 体验优化,异步执行下落动画
			this.$nextTick(() => {
				/* 注意要给shopcart ref接口 */
				/*  shopcart 这里就是父组件调用子组件的drop方法 因为要一层一层的将target 就是点击的对象
				 * 	传递给购物车 用于获取初始位置和状态
				 * */
				this.$refs.shopcart.drop(target);
			});
		},
		/* 计算出 每一个区间的高度 并存在listHeight中 */
		_calculateHeight() {
			/* 找到右侧 foodlist  */
			let foodList = this.$refs.foodList;
			let height = 0;
			this.listHeight.push(height);
			/* 知道每一个区间的高度之后 push到listHeight里面 标识每一个区间的高度是多少 */
			for (var i = 0; i < foodList.length; i++) {
				let item = foodList[i];
				height += item.clientHeight;
				this.listHeight.push(height);
			}
		},
		/* 跟随滚动  menuScroll跟随滚动*/
		_followScroll(index) {
			let menuList = this.$refs.menuList;
			let el = menuList[index];
			this.menuScroll.scrollToElement(el, 300, 0, -100);
		},
		/* 点击商品 展开商品详情页 */
		selectFood(food, event) {
			if (!event._constructed) {
				return;
			}
			/* 修改这个被选中的商品,然后传给food组件 */
			this.selectedFood = food;
			/* 显示food组件 商品详细页 */
			this.$refs.food.show();
		}
	},
	components: {
		shopcart,
		cartcontroll,
		food
	}
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import "../../common/stylus/mixin.styl"

.goods
	display: flex
	position: absolute
	top: 180px
	bottom: 46px
	width: 100%
	overflow: hidden
	.menu-wrapper
		flex: 0 0 80px
		width: 80px
		background: #f3f5f7
		.menu-item
			display: table
			height: 54px
			width: 56px
			padding: 0 12px
			line-height: 14px
			&.current
				position: relative
				z-index: 10
				margin-top: -1px
				background: #fff
				font-weight: 700
				.text
					border-none()
			.icon
				display: inline-block
				vertical-align: top
				width: 12px
				height: 12px
				margin-right: 2px
				background-size: 12px 12px
				background-repeat: no-repeat
				&.decrease
					bg-image('decrease_3')
				&.discount
					bg-image('discount_3')
				&.guarantee
					bg-image('guarantee_3')
				&.invoice
					bg-image('invoice_3')
				&.special
					bg-image('special_3')
			.text
				display: table-cell
				width: 56px
				vertical-align: middle
				border-1px(rgba(7, 17, 27, 0.1))
				font-size: 12px
	.foods-wrapper
		flex: 1
		.title
			padding-left: 14px
			height: 26px
			line-height: 26px
			border-left: 2px solid #d9dde1
			font-size: 12px
			color: rgb(147, 153, 159)
			background: #f3f5f7
		.food-item
			display: flex
			margin: 18px
			padding-bottom: 18px
			border-1px(rgba(7, 17, 27, 0.1))
			&:last-child
				border-none()
				margin-bottom: 0
			.icon
				flex: 0 0 57px
				margin-right: 10px
			.content
				flex: 1
				.name
					margin: 2px 0 8px 0
					height: 14px
					line-height: 14px
					font-size: 14px
					color: rgb(7, 17, 27)
				.desc, .extra
					line-height: 10px
					font-size: 10px
					color: rgb(147, 153, 159)
				.desc
					line-height: 12px
					margin-bottom: 8px
				.extra
					.count
						margin-right: 12px
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
					right: 0
					bottom: 12px
</style>
