import Vue from 'vue';
import Vuex from 'vuex';
import getters from './getters';
import actions from './actions';
import mutations from './mutations';

Vue.use(Vuex);

const state={
	headerShow:false,
	userInfo:Object,
	nowcity:{"name":Object,"id":Object},
	//imgUrl:"http://localhost:8082/sell-manager-web",
	imgUrl:"http://192.168.137.1:8082/sell-manager-web",
	//apiUrl:"http://localhost:8081/sell-restapi",
	apiUrl:"http://192.168.137.1:8081/sell-restapi",
	/* http请求 */
	// goodsUrl:"http://localhost:8081/sell-restapi/goods",  /* 在goods.vue里面*/
	// orderUrl:"http://localhost:8081/sell-restapi/order", /* 在order.vue里面 */
	// ratingsUrl:"http://localhost:8081/sell-restapi/ratings", /* ratings.vue*/
	// sellerUrl:"http://localhost:8081/sell-restapi/seller", /* App.vue*/
	// loginUrl:"http://localhost:8081/sell-restapi/login", /* login.vue*/
	// submitOrderUrl:"http://localhost:8081/sell-restapi/submitOrder" ,/* shopcart.vue*/
	
	goodsUrl:"http://192.168.137.1:8081/sell-restapi/goods",  /* 在goods.vue里面*/
	orderUrl:"http://192.168.137.1:8081/sell-restapi/order", /* 在order.vue里面 */
	ratingsUrl:"http://192.168.137.1:8081/sell-restapi/ratings", /* ratings.vue*/
	sellerUrl:"http://192.168.137.1:8081/sell-restapi/seller", /* App.vue*/
	loginUrl:"http://192.168.137.1:8081/sell-restapi/login", /* login.vue*/
	submitOrderUrl:"http://192.168.137.1:8081/sell-restapi/submitOrder" /* shopcart.vue*/
	
};

export default new Vuex.Store({
	state,
	getters,
	actions,
	mutations
});