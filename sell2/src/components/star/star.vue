<template>
	<!-- 星级组件 -->
	<div class="star" :class="starType">
		<!-- itemClasses使用计算属性 根据分数获得多少个半星和全星 -->
		<span v-for="(itemClass,index) in itemClasses" :class="itemClass" class="star-item" :key="index"></span>
	</div>
</template>

<script>
	/* eslint-disable */
	/* 最多有5颗星 */
	const LENGTH = 5;
	/* 这个图片的尺寸 需要从外面传入一些变量 
	尺寸和分数 size和score 都是数字类型  score代表分数 就是有几颗星*/
	export default {
		props:{
			size:{
				type:Number
			},
			score:{
				type:Number
			}
		},
		computed:{
			/*  这个startType就是利用size拼接出来的类型 */
			starType(){
				return 'star-' + this.size;
			},
			itemClasses(){
				let result=[];
				/* 让分数 取整 例如分数 4.2*2=8.4/2=4  */
				let score=Math.floor(this.score*2)/2;
				let hasDecimal=score%1!==0;
				let integer=Math.floor(score);
				/* 插入integer个整星 */
				for (var i = 0; i < integer; i++) {
					result.push("on");
				}
				/* 半星最多一个 */
				if (hasDecimal) {
					result.push("half");
				}
				while (result.length<LENGTH){
					result.push("off");
				}
				return result;
			}
		}
	};
</script>

<style lang="stylus" rel="stylesheet/stylus">
	@import "../../common/stylus/mixin.styl"
	
	.star
		font-size:0px;
		.star-item
			display:inline-block;
			background-repeat: no-repeat;
		&.star-48
			.star-item
				width: 20px;
				height: 20px;
				margin-right: 22px;
				background-size: 20px 20px;
				&:last-child
					margin-right: 0;
				&.on
					bg-image('star48_on');
				&.half
					bg-image('star48_half');
				&.off
					bg-image('star48_off');
		&.star-36
			.star-item
				width: 15px;
				height: 15px;
				margin-right: 6px;
				background-size: 15px 15px;
				&:last-child
					margin-right: 0;
				&.on
					bg-image('star36_on');
				&.half
					bg-image('star36_half');
				&.off
					bg-image('star36_off');
		&.star-24
			.star-item
				width: 10px;
				height: 10px;
				margin-right: 3px;
				background-size: 10px 10px;
				&:last-child
					margin-right: 0;
				&.on
					bg-image('star24_on');
				&.half
					bg-image('star24_half');
				&.off
					bg-image('star24_off');
</style>
