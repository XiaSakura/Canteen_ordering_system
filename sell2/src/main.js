// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import VueResource from 'vue-resource';
import App from './App';
import FastClick from 'fastclick'
import router from './router/index.js';
import './common/stylus/index.styl';
import store from './store';
import { AlertPlugin, ToastPlugin } from 'vux';


Vue.use(AlertPlugin);
Vue.use(ToastPlugin);
Vue.use(VueResource);

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
	store,
  render: h => h(App)
});
