<template>
	<!-- 商品评论上面的选择类型 全部,推荐,吐槽 或者是满意,不满意 还有只查看有内容的评价
	* 注意这里是没有写评论列表的 因为有些地方的评价页是不同的  -->
	<div class="ratingselect">
		<!-- 查看评论类型 -->
		<div class="rating-type border-1px">
			<span @click="select(2, $event)" class="block positive" :class="{ active: selectType === 2 }">
				{{ desc.all }}
				<span class="count">{{ ratings.length }}</span>
			</span>
			<span @click="select(0, $event)" class="block positive" :class="{ active: selectType === 0 }">
				{{ desc.positive }}
				<span class="count">{{ positives.length }}</span>
			</span>
			<span @click="select(1, $event)" class="block negative" :class="{ active: selectType === 1 }">
				{{ desc.negative }}
				<span class="count">{{ negatives.length }}</span>
			</span>
			<span style="float: right;" class="block positive active" @click="showComment">发表评论</span>
		</div>
		<!-- 是否只查看内容 -->
		<div class="switch" :class="{ active: onlyContent }" @click="toggleContent">
			<span class="icon-check_circle"></span>
			<span class="text">只查看有内容的评价</span>
		</div>
		<div v-transfer-dom>
			<popup v-model="show" height="300px">
				<div class="popup0">
					<group title="评论">
						<selector name="rateType" tiltel="评价类型" placeholder="请选择评价类型" v-model="rateType" :options="list"></selector>
						<x-textarea :max="200" name="description" v-model="description" placeholder="请输入评价"></x-textarea>
						<cell title="对此评分"><rater v-model="score" :max="5"></rater></cell>
						<x-button type="primary" @click.native="comment">发布评论</x-button>
					</group>
				</div>
			</popup>
		</div>
	</div>
</template>

<script>
import { TransferDom, Popup, Group, Cell, XButton, XSwitch, XTextarea, XInput, Rater, Selector } from 'vux';
import { getStore, setStore, removeStore } from 'common/js/mUtils.js';
/* eslint-disable */
/* 注意这里还需要传入描述 动态的修改 因为评价页的这些是不用的(全部,推荐和吐槽是不一样的) */
const POSITIVE = 0;
const NEGATIVE = 1;
const ALL = 2;
export default {
	directives: {
		TransferDom
	},
	props: {
		/* 从父组件传过来的ratings 用于获取评论 */
		ratings: {
			type: Array,
			default() {
				return [];
			}
		},
		foodid: null,
		/* 对应 上面的评价类型  全部,推荐和吐槽,或者全部,满意,不满意*/
		/* 注意这里是number类型 需要自己通知父组件 值改变了 */
		selectType: {
			type: Number,
			default: 2
		},
		/* 只查看内容,需要通知父组件 */
		onlyContent: {
			type: Boolean,
			default: false
		},
		/* 这个描述 就是上面那个设计稿蓝色区块的描述文字,可以从父组件里面修改  */
		desc: {
			type: Object,
			default() {
				return {
					all: '全部',
					positive: '满意',
					negative: '不满意'
				};
			}
		}
	},
	data() {
		return {
			/* 这里为了方便追踪变量 也需要定义这几个常量*/
			score: 5,
			description: '',
			show: false,
			rateType: 0,
			list: [{ key: '0', value: '好评' }, { key: '1', value: '差评' }]
		};
	},
	methods: {
		refresh() {
			this.$router.replace('/rating');
		},
		comment(event) {
			const url =this.$store.state.apiUrl+"/comment";
			this.userInfo=JSON.parse(getStore("userInfo"));
			let data = {
				userid: this.userInfo.id,
				ratetype: this.rateType,
				text: this.description,
				score: this.score,
				foodid: this.foodid
			};
			this.$http.post(url, data).then(response => {
				response = response.body;
				if (response.success) {
					this.description="";
					this.$vux.alert.show({
						title: '发表评论成功',
						content: '点击继续',
						onHide() {}
					});
					this.$router.push('/emptyPage');
				} else {
					this.$vux.alert.show({
						title: '发表评论失败',
						content: '请重新评价'
					});
				}
			});
			this.show = false;
		},
		showComment(event) {
			if (!event._constructed) {
				return;
			}
			this.show = true;
		},
		select(type, event) {
			if (!event._constructed) {
				return;
			}
			/* 注意这里再进行修改 否则会出错 因为父组件里面还会进行修改 所以这里就不用修改了 */
			//this.selectType=type;
			/* 让父组件监听这个事件 @select="selectRating" 在父组件selectRating这个方法*/
			this.$emit('select', type);
		},
		toggleContent(event) {
			if (!event._constructed) {
				return;
			}
			/* 注意这里再进行修改 否则会出错 因为父组件里面还会进行修改 所以这里就不用修改了 */
			//this.onlyContent=!this.onlyContent;
			/* 同上 @toggle="toggleContent" */
			this.$emit('toggle');
		}
	},
	computed: {
		positives() {
			/* 利用过滤器 返回ratings里面rateType为要求的*/
			return this.ratings.filter(rating => {
				return rating.rateType === POSITIVE;
			});
		},
		negatives() {
			/* 利用过滤器 返回ratings里面rateType为要求的*/
			return this.ratings.filter(rating => {
				return rating.rateType === NEGATIVE;
			});
		}
	},
	components: {
		Popup,
		Group,
		Cell,
		Selector,
		XSwitch,
		XButton,
		XTextarea,
		XInput,
		Rater
	}
};
</script>

<style lang="stylus" rel="stylesheet/stylus">
@import "../../common/stylus/mixin.styl";
.ratingselect
	.rating-type
		/* 因为这里有1像素的border 所以需要padding和margin */
		padding:18px 0;
		margin:0 18px;
		border-1px(rgba(7, 17, 27, 0.1));
		font-size: 0;
		.block
			display:inline-block;
			padding:8px 12px;
			margin-right:8px;
			line-height: 16px
			border-radius: 1px
			font-size: 12px
			color: rgb(77, 85, 93);
			&.active
				color: #fff
			.count
				margin-left: 2px
				font-size: 8px
			&.positive
				background: rgba(0, 160, 220, 0.2);
				&.active
					background: rgb(0, 160, 220);
			&.negative
				background: rgba(77, 85, 93, 0.2);
				&.active
					background: rgb(0, 160, 220);
	.switch
		padding: 12px 18px
		line-height: 24px
		border-bottom: 1px solid rgba(7, 17, 27, 0.1)
		color: rgb(147, 153, 159)
		font-size: 0;
		&.active
			.icon-check_circle
				color: #00c850;
		.icon-check_circle
			display: inline-block
			vertical-align: top
			margin-right: 4px
			font-size: 24px
		.text
			display: inline-block
			vertical-align: top
			font-size: 12px
</style>
