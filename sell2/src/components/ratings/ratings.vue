<template>
	<!-- 评价页 -->
	<div class="ratings" ref="ratings">
		<!-- 评论内容 -->
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
						<span class="title">送达时间</span>
						<span class="delivery">{{ seller.deliveryTime }}</span>
					</div>
				</div>
			</div>
			<split></split>
			<!-- 复用ratingselect组件 组件包含评论类型和只查看内容-->
			<ratingselect :ratings="ratings" :selectType="selectType" :onlyContent="onlyContent" @select="selectRating" @toggle="toggleContent"></ratingselect>
			<!-- 用户评价列表 -->
			<div class="rating-wrapper">
				<ul>
					<li v-for="(rating, index) in ratings" :key="index" class="rating-item" v-show="needShow(rating.rateType, rating.text)">
						<!-- 头像是固定的 在左边 所以可以用到flex布局 -->
						<div class="avatar"><img :src="$store.state.imgUrl+'/'+rating.avatar" width="28px" height="28px" /></div>
						<!-- flex布局 评论内容在右边-->
						<div class="content">
							<h1 class="name">{{ rating.username }}</h1>
							<!-- 星星组件 -->
							<div class="star-wrapper">
								<star :size="24" :score="rating.score"></star>
								<span class="delivery" v-show="rating.deliveryTime">{{ rating.deliveryTime }}</span>
							</div>
							<!-- 评论内容 -->
							<p class="text">{{ rating.text }}</p>
							<!-- 用户推荐 -->
							<div class="recommend" v-show="rating.recommend && rating.recommend.length">
								<span class="icon-thumb_up"></span>
								<span class="item" v-for="(item, index) in rating.recommend" :key="index">{{ item }}</span>
							</div>
							<!-- 发表时间 使用绝对定位-->
							<div class="time">{{ rating.rateTime | formatDate }}</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
</template>

<script>
/* eslint-disable */
import star from 'components/star/star.vue';
import ratingselect from 'components/ratingselect/ratingselect.vue';
import split from 'components/split/split.vue';
import { formatDate } from 'common/js/date';
import BScroll from 'better-scroll';
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
	activated() {
		this.$store.state.headerShow = true;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo.username == null) {
			this.$router.replace('/login');
		}
		const url = this.$store.state.apiUrl+"/ratings";
		this.$http.get(url).then(response => {
			response = response.body;
			if (response.errno === ERROR_OK) {
				this.ratings = response.data;
				this.$nextTick(() => {
					this.scroll = new BScroll(this.$refs.ratings, {
						click: true
					});
				});
			}
		});
	},
	data() {
		return {
			ratings: [],
			/* 这些参数用于ratingselect组件 */
			selectType: ALL,
			onlyContent: true
		};
	},
	created() {
		this.$store.state.headerShow = true;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo.username == null) {
			this.$router.replace('/login');
		}
		const url = this.$store.state.ratingsUrl;
		this.$http.get(url).then(response => {
			response = response.body;
			if (response.errno === ERROR_OK) {
				this.ratings = response.data;
				this.$nextTick(() => {
					this.scroll = new BScroll(this.$refs.ratings, {
						click: true
					});
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
	components: {
		star,
		ratingselect,
		split
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
						margin-bottom: 6px
						font-size: 0
						.star
							display: inline-block
							margin-right: 6px
							vertical-align: top
						.delivery
							display: inline-block
							vertical-align: top
							line-height: 12px
							font-size: 10px
							color: rgb(147, 153, 159)
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
