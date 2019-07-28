/**
 * 解析url里面的参数 并用对象的形式返回
 * @example ?id=12345&a=b
 * @return Object {id:12345,a:b}
 */
/* eslint-disable */
export function urlParse() {
	let url = window.location.search;
	let obj = {};
	/* 第一个 [?&] 代表包含?或者&的字符 这里是?
	第二个 [^?&] 代表在上面这个字符之后 不包含?&的  这里代表id
	第三个 在=之后的[^?&] 代表 12345 */
	let reg = /[?&][^?&]+=[^?&]/g;
	/* 把url分割成匹配的字符串   ['?id=12345', '&a=b']*/
	let arr = url.match(reg);
	if (arr) {
		arr.forEach((item) => {
			/* 分割出第一个问号 然后再使用= 分隔开 */
			let tempArr = item.substring(1).split("=");
			let key = decodeURIComponent(tempArr[0]);
			let val = decodeURIComponent(tempArr[1]);
			obj[key] = val;
		});
	}
	return obj;
};
