package com.xia.test;

import java.util.Date;

import org.junit.Test;

public class tst {
	@Test
	public void test01() {
		Date date = new Date();
		Date date2 = new Date();
		if (date.before(date2)) {
			System.out.println("date" + date);
		} else {
			System.out.println("date2" + date2);
		}
	}
	@Test
	public void test02() {
		String name = "皮蛋瘦肉粥";
		String[] split = name.split(",");
		for (String string : split) {
			System.out.println(string);
		}
	}
}
