<template>
	<!-- cartcontroll 就是添加商品的小按钮  -->
	<div class="cartcontroll">
		<!-- 使用动画 给减按钮 -->
		<transition name="move">
			<!-- 减按钮 -->
			<div class="cart-decrease" v-show="food.count > 0" @click.stop.prevent="decreaseCart">
				<!-- 这里因为后面要添加滚动的动画 所以用了内层 -->
				<span class="inner icon-remove_circle_outline"></span>
			</div>
		</transition>
		<!-- count 当前购买个数 -->
		<div class="cart-count" v-show="food.count > 0">{{ food.count }}</div>
		<!-- 加按钮 -->
		<div class="cart-add icon-add_circle" @click.stop.prevent="addCart"></div>
	</div>
</template>

<script>
/* eslint-disable */
/* 因为这个food.count在 food里面其实是没有的 所以 动态改变是不会生效的 所以需要用到vue的接口 */
import Vue from 'vue';

export default {
	props: {
		/* 需要从goods组件里面 获得商品对象  注意food对象中并没有count属性 是我们通过addCart方法进行添加的*/
		food: {
			type: Object
		}
	},
	methods: {
		addCart(event) {
			if (!event._constructed) {
				return;
			}
			if (!this.food.count) {
				/* 给food对象里面添加count属性 */
				Vue.set(this.food, 'count', 1);
			} else {
				this.food.count++;
			}
			/* 当点击添加的时候 派发一个事件 让父组件知道*/
			this.$emit('add', event.target);
		},
		decreaseCart(event) {
			if (!event._constructed) {
				return;
			}
			if (this.food.count) {
				this.food.count--;
			}
		}
	}
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
.cartcontroll
	font-size:0;
	.cart-decrease
		display:inline-block;
		padding:6px;
		opacity: 1
		transform: translate3d(0, 0, 0);
		.inner
			display:inline-block;
			line-height:24px;
			font-size:24px;
			color:rgb(0, 160, 220);
			transition: all 0.4s linear
			transform:rotate(0);
			/* 给整个动画过程添加transition效果 */
		&.move-enter-active,&.move-leave-active
			transition: all 0.4s linear;
		&.move-enter, &.move-leave-active  //进入前和离开后
			opacity: 0; //进入前和离开后整体的透明度
			transform: translate3d(24px, 0, 0);
			.inner
				transform: rotate(180deg);
	.cart-count
		display: inline-block
		vertical-align: top
		width: 12px
		padding-top: 6px
		line-height: 24px
		text-align: center
		font-size: 10px
		color: rgb(147, 153, 159);
	.cart-add
		display: inline-block
		padding: 6px
		line-height: 24px
		font-size: 24px
		color: rgb(0, 160, 220)
</style>
