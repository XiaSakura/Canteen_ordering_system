import Vue from 'vue';
import Router from 'vue-router';
import goods from 'components/goods/goods';
import ratings from 'components/ratings/ratings';
import seller from 'components/seller/seller';
import login from 'components/login/login';
import order from 'components/order/order';
import my from 'components/my/my';
 import emptyPage from 'components/emptyPage/emptyPage'

Vue.use(Router);

const routes = [{
		path: '/',
		redirect: '/login'
	},
	{
		path:'/login',
		component:login
	},
	{
		path:'/emptyPage',
		component:emptyPage
	},
	{
		path:'/my',
		component:my
	},
	{
		path: '/goods',
		component: goods
	}, {
		path: '/order',
		component: order
	}, {
		path: '/ratings',
		component: ratings
	}, {
		path: '/seller',
		component: seller
	}
];

export default new Router({
	linkActiveClass: 'active',
	routes
});
