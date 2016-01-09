package com.yike.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author zhaoweiChuang
 * 
 * @2015年3月30日
 * 
 * @descripte
 * 
 *            验证邮箱 和 身份证
 */
public class CheckCode {
	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */

	public static boolean isEmail(String email) {
		String emailPattern = "[a-zA-Z0-9][a-zA-Z0-9._-]{2,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
		boolean result = Pattern.matches(emailPattern, email);
		return result;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (mobiles == null || mobiles.equals(""))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	// 判断身份证：要么是15位，要么是18位，最后一位可以为字母，并写程序提出其中的年月日。
	public static boolean isID(String idNum) {
		// 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
		Pattern idNumPattern = Pattern
				.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
		// 通过Pattern获得Matcher
		Matcher idNumMatcher = idNumPattern.matcher(idNum);
		// 判断用户输入是否为身份证号
		if (idNumMatcher.matches()) {
			// 如果是，定义正则表达式提取出身份证中的出生日期
			// Pattern birthDatePattern = Pattern
			// .compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");// 身份证上的前6位以及出生年月日
			// 通过Pattern获得Matcher
			// Matcher birthDateMather = birthDatePattern.matcher(idNum);
			// if(birthDateMather.find()){
			// String year = birthDateMather.group(1);
			// String month = birthDateMather.group(2);
			// String date = birthDateMather.group(3);
			// //输出用户的出生年月日
			// System.out.println(year+"年"+month+"月"+date+"日");
			// }
			return true;
		} else {
			// 如果不是，输出信息提示用户
			return false;
		}
	}

}
