/* 通用的数据存取,存放到localStorage里面 */
/* eslint-disable */
/* 例子 id代表商家id,key是favorite value是true */
export function saveToLocal(id,key,value){
	/* _seller_ 代表一个名称空间 代表存放到这个seller的下面 下划线代表私有的 专门用来存取seller用的 */
	let seller=window.localStorage._seller_;
	if (!seller) {
		/* 初始化seller对象 */
		seller={};
		seller[id]={};
	}else{
		/* 将seller对象 转换成json对象 */
		seller=JSON.parse(seller);
		if (!seller[id]) {
			seller[id]={};
		}
	}
	seller[id][key]=value;
	/* localstorage只能存放字符串 */
	window.localStorage._seller_=JSON.stringify(seller);
};

/* 取出数据 */
/* def是默认值 当取不出来的时候用的 */
export function loadFromLocal(id,key,def){
	let seller=window.localStorage._seller_;
	if (!seller) {
		return def;
	}
	seller=JSON.parse(seller)[id];
	if (!seller) {
		return def;
	}
	let ref=seller[key];
	return ref||def;
}
