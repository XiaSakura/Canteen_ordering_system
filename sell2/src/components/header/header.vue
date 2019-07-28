<template>
	<div class="header">
		<!--包裹内容-->
		<div class="content-wrapper">
			<!--商家头像 avatar-->
			<div class="avatar"><img width="64" height="64" :src="this.$store.state.imgUrl + '/' + seller.avatar" /></div>
			<!--右侧内容-->
			<div class="content">
				<!--标题-->
				<div class="title">
					<!--brand 品牌图标-->
					<span class="brand"></span>
					<span class="name">{{ seller.name }}</span>
				</div>
				<!--描述-->
				<div class="description">{{ seller.description }}</div>
				<!--商家支持的活动-->
				<div class="support" v-if="seller.supports">
					<!--左侧小图标 根据什么活动而来的 type-->
					<span class="icon" :class="classMap[seller.supports[0].type]"></span>
					<!--具体活动内容 只显示第一条-->
					<span class="text">{{ seller.supports[0].description }}</span>
				</div>
			</div>
			<!--统计活动个数 右侧的小框-->
			<div class="support-count" @click="showDetail" v-if="seller.supports">
				<!-- 需要用到背景和一个右箭头 -->
				<span class="count">{{ seller.supports.length }}</span>
				<i class="icon-keyboard_arrow_right"></i>
			</div>
		</div>
		<!-- 公告栏 -->
		<div class="bulletin-wrapper" @click="showDetail">
			<!-- 左侧公告图标 -->
			<span class="bulletin-title"></span>
			<!-- 右侧公告文字 -->
			<span class="bulletin-text">{{ seller.bulletin }}</span>
			<!-- 最右侧小箭头 -->
			<i class="icon-keyboard_arrow_right"></i>
		</div>
		<!-- 背景图片 -->
		<div class="background"><img :src="this.$store.state.imgUrl + '/' + seller.avatar" width="100%" height="100%" /></div>
		<!-- 渐变动画  -->
		<transition name="fade">
			<!-- 点击查看 商家详情页  全屏浮层-->
			<div v-show="detailShow" class="detail">
				<!-- stickFooter布局  外层必须要清除浮动 -->
				<div class="detail-wrapper clearfix">
					<!-- 主要内容 -->
					<div class="detail-main">
						<!-- 店铺名字 -->
						<h1 class="name">{{ seller.name }}</h1>
						<div class="star-wrapper"><star :size="48" :score="seller.score"></star></div>
						<!-- 优惠信息 -->
						<div class="title">
							<!-- 左边的线 -->
							<div class="line"></div>
							<div class="text">优惠信息</div>
							<!-- 右边的线 -->
							<div class="line"></div>
						</div>
						<!-- 支持的活动 -->
						<ul v-if="seller.supports" class="supports">
							<!-- 遍历出商家支持的活动 -->
							<li class="support-item" v-for="(item, index) in seller.supports" :key="index">
								<!-- 活动图标 -->
								<span class="icon" :class="classMap[seller.supports[index].type]"></span>
								<!-- 活动文字 -->
								<span class="text">{{ seller.supports[index].description }}</span>
							</li>
						</ul>
						<!-- 下面是同样的结构 商家的公告 -->
						<div class="title">
							<!-- 左边的线 -->
							<div class="line"></div>
							<div class="text">商家公告</div>
							<!-- 右边的线 -->
							<div class="line"></div>
						</div>
						<div class="bulletin">
							<p class="content">{{ seller.bulletin }}</p>
						</div>
					</div>
				</div>
				<!-- 关闭按钮 -->
				<div class="detail-close" @click="closeDetail">
					<!-- 按钮字体 -->
					<i class="icon-close"></i>
				</div>
			</div>
		</transition>
	</div>
</template>

<script>
/* eslint-disable */
import star from 'components/star/star.vue';

export default {
	data() {
		return {
			detailShow: false
		};
	},
	props: {
		seller: {
			type: Object
		}
	},
	created() {
		/* 定义一个classmap 就可以 因为对应接口数据里面的 type是数字 0,1,2,3 */
		this.classMap = ['decrease', 'discount', 'special', 'invoice', 'guarantee'];
	},
	methods: {
		showDetail() {
			this.detailShow = true;
		},
		closeDetail() {
			this.detailShow = false;
		}
	},
	components: {
		star
	}
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin";

.header
	color: #fff;
	position: relative
	.content-wrapper
		position: relative
		padding: 24px 12px 18px 24px;
		font-size: 0;
		.avatar
			display:inline-block;
			/* 这里必须要置顶 下面的brand和name才会置顶 */
			vertical-align:top;
		.content
			display:inline-block;
			margin-left:16px;
			.title
				margin:2px 0 8px 0;
				.brand
					display:inline-block;
					vertical-align:top;
					width:30px;
					height:18px;
					bg-image('brand');
					background-size:30px 18px;
					background-repeat:no-repeat;
				.name
					margin-left:6px;
					font-size:16px;
					line-height:18px;
					font-weight:bold;
			.description
				margin-bottom:10px;
				line-height: 12px;
				font-size: 12px;
				color:rgb(255,255,255);
			.support
				.icon
					display:inline-block;
					vertical-align:top;
					width:12px;
					height:12px;
					margin-right:4px;
					background-size:12px 12px;
					background-repeat:no-repeat;
					&.decrease
						bg-image('decrease_1');
					&.discount
						bg-image('discount_1');
					&.special
						bg-image('special_1');
					&.invoice
						bg-image('invoice_1');
					&.guarantee
						bg-image('guarantee_1');
				.text
					line-height: 12px;
					font-size: 10px;
		.support-count
			position:absolute;
			right: 12px;
			bottom: 14px;
			background: rgba(0, 0, 0, 0.2)
			border-radius:14px;
			padding:0 8px;
			text-align:center;
			line-height:24px;
			height:24px;
			.count
				font-size:10px
				vertical-align: top
			.icon-keyboard_arrow_right
				margin-left:2px;
				line-height:24px;
				font-size:10px;
	.bulletin-wrapper
		position:relative;
		height:28px;
		line-height:28px;
		padding:0 22px 0 12px;
		overflow:hidden;
		white-space:nowrap;
		text-overflow:ellipsis;
		font-size: 0;
		background: rgba(7, 17, 27, 0.2);
		.bulletin-title
			display:inline-block;
			vertical-align:top;
			font-size:10px;
			margin-top:8px;
			width:22px;
			height:12px;
			bg-image("bulletin");
			background-size: 22px 12px;
			background-repeat: no-repeat;
		.bulletin-text
			vertical-align:top;
			margin-left:4px;
			font-size: 10px;
		.icon-keyboard_arrow_right
			position:absolute;
			font-size:10px;
			right:12px;
			top:8px;
	.background
		position:absolute;
		top:0px;
		left:0px;
		width:100%;
		height:100%;
		z-index:-1;
		filter: blur(10px);
	.detail
		position:fixed;
		z-index:100;
		top:0;
		left:0;
		width:100%;
		height:100%;
		overflow:auto;
		background:rgba(7,17,27,0.8);
		opacity: 1;
		backdrop-filter:blur(10px);
		transition: all 0.5s;
		&.fade-enter, &.fade-leave-active
			opacity: 0;
			background: rgba(7, 17, 27, 0);
		.detail-wrapper
			/* 规定最小高度  撑开视窗*/
			min-height:100%;
			width:100%;
			.detail-main
				margin-top:64px;
				padding-bottom:64px;
				.name
					line-height:16px;
					text-align:center;
					font-size:16px;
					font-weight:700;
				.star-wrapper
					margin-top:18px;
					padding:2px 0;
					text-align:center;
				.title
					display:flex;
					width:80%;
					margin:30px auto 24px auto;
					.line
						flex:1
						position:relative;
						top:-6px;
						border-bottom:1px solid rgba(255,255,255,0.2);
					.text
						padding:0 12px;
						font-weight:700;
						font-size:14px;
				.supports
					width:80%;
					margin:0 auto;
					.support-item
						padding:0 12px;
						margin-bottom:12px;
						font-size:0;
						&:last-child
							margin-bottom: 0;
						.icon
							display: inline-block;
							width:16px;
							height:16px;
							vertical-align: top;
							margin-right: 6px;
							background-size: 16px 16px;
							background-repeat: no-repeat;
							&.decrease
								bg-image('decrease_2');
							&.discount
								bg-image('discount_2');
							&.guarantee
								bg-image('guarantee_2');
							&.invoice
								bg-image('invoice_2');
							&.special
								bg-image('special_2');
						.text
							font-size:12px;
							line-height:16px;
				.bulletin
					width:80%;
					margin:0 auto;
					.content
						padding:0 12px;
						line-height:24px;
						font-size:12px;
		.detail-close
			position:relative;
			width:32px;
			height:32px;
			margin:-64px auto 0 auto;
			clear:both;
			font-size:32px;
</style>
