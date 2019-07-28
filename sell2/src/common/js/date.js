	/* eslint-disable */
/* 格式化日期方法 需要传入Date类型的date 还有格式 例如'yyyy-MM-dd hh:mm' */
export function formatDate(date,fmt) {
	/* 利用正则先匹配 年份 yyyy */
	if (/(y+)/.test(fmt)) {
		/* 替换掉fmt里面 yyyy成date里面的年份 */
		/* RegExp.$1对应就是上面的正则匹配的结果 */
		/* 因为这里传入的年份可能是 yy  例如2016年 截取的是16年 substr */
		fmt=fmt.replace(RegExp.$1,(date.getFullYear()+'').substr(4-RegExp.$1.length));
	}
	/* 注意月份是从0开始的 比较特殊 */
	let o={
		'M+':date.getMonth()+1,
		'd+':date.getDate(),
		'h+':date.getHours(),
		'm+':date.getMinutes(),
		's+':date.getSeconds()
	};
	/* 遍历上面的对象  */
	for (let k in o) {
		/* k对应的是M+,d+等等 */
		if (new RegExp(`(${k})`).test(fmt)) {
			/* value */
			let str=o[k]+'';
			/* 进行替换 判断  传入的只有一个月份例如 m 而不是mm的话 就不需要补0  传入mm的话就需要在左边添上一个0 */
			fmt=fmt.replace(RegExp.$1,(RegExp.$1.length===1)?str:padLeftZero(str));
		}
	}
	return fmt;
};

/* 添加一个方法 左边加上0 */
function padLeftZero(str) {
	/* 比如说str的长度是1 比如说9 那就是009 substr 那就是09 刚好 */
	return ('00'+str).substr(str.length);
}