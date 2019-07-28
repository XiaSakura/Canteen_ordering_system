<template>
	<div class="my">
		<div class="user-info">
			<div class="user-img"><img :src="userAvatar" /></div>
			<div class="name-mobile">
				<p>{{ name }}</p>
			</div>
		</div>
		<group style="margin-bottom: 10px;"><cell title="我的订单" value="查看全部订单" is-link :link="{ path: '/order' }"></cell></group>
		<group>
			<cell title="重新登录" is-link @click.native="loginBack"></cell>
			<!-- 	<cell title="我的二维码" is-link :link="{ path: '/MyQR' }"></cell> -->
		</group>
	</div>
</template>

<script>
import { Group, Cell, CellBox, Flexbox, FlexboxItem } from 'vux';
import { getStore, setStore, removeStore } from 'common/js/mUtils.js';
export default {
	name: 'my',
	data() {
		return {
			name: '',
			userAvatar:"",
			userInfo:Object
		};
	},
	created() {
		this.$store.state.headerShow = true;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo == null) {
			this.$router.replace('/login');
		}
		this.name = this.userInfo.username;
		this.userAvatar=this.$store.state.imgUrl+"/"+this.userInfo.avatar
	},
	activated() {
		this.$store.state.headerShow = true;
		this.userInfo = JSON.parse(getStore('userInfo'));
		if (this.userInfo == null) {
			this.$router.replace('/login');
		}
		this.name = this.userInfo.username;
		this.userAvatar=this.$store.state.imgUrl+"/"+this.userInfo.avatar
	},
	methods: {
		loginBack() {
			removeStore('userInfo');
			this.$router.replace('/login');
		}
	},
	components: {
		Group,
		Cell,
		CellBox,
		Flexbox,
		FlexboxItem
	}
};
</script>

<style scoped lang="less">
.user-info {
	width: 100%;
	height: 140px;
	display: flex;
	align-items: center;
	@width77: 77px;
	> div {
		height: @width77;
	}
	.user-img {
		width: @width77;
		border-radius: 50%;
		overflow: hidden;
		margin-left: 30px;
		background-color: #b0b6c4;
		img {
			width: 100%;
			height: 100%;
		}
	}
	.name-mobile {
		padding: 5px 10px;
		-webkit-box-sizing: border-box;
		-moz-box-sizing: border-box;
		box-sizing: border-box;
		display: flex;
		flex-direction: column;
		justify-content: space-around;
	}
}
.order-type-box {
	width: 100%;
	display: flex;
	> div {
		flex: 1;
		height: 55px;
		font-size: 12px;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		img {
			width: 24px;
			height: 24px;
			margin-bottom: 5px;
		}
	}
}
</style>
