package com.joolun.common.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 数据脱敏工具类
 *
 * @Author: JooLun
 * @Author: https://www.cnblogs.com/xiluonanfeng/p/10183926.html
 * @Date: 2021/7/19 16:21
 */
public class SensitiveUtils {

	/**
	 * 默认填充字符
	 */
	public static final String DEFAULT_PAD_STR = "*";

	/**
	 * 数据脱敏
	 *
	 */
	public static String process(String data) {
		return process(data, 2, 1, DEFAULT_PAD_STR);
	}

	/**
	 * 数据脱敏
	 *
	 */
	public static String process(String data, Integer leftLen, Integer rightLen) {
		return process(data, leftLen, rightLen, DEFAULT_PAD_STR);
	}

	/**
	 * 对字符串进行脱敏操作
	 * @param originStr 原始字符串
	 * @param prefixNoMaskLen 左侧需要保留几位明文字段
	 * @param suffixNoMaskLen 右侧需要保留几位明文字段
	 * @param maskStr 用于遮罩的字符串, 如'*'
	 * @return 脱敏后结果
	 */
	public static String process(String originStr, int prefixNoMaskLen, int suffixNoMaskLen, String maskStr) {
		if (originStr == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = originStr.length(); i < n; i++) {
			if (i < prefixNoMaskLen) {
				sb.append(originStr.charAt(i));
				continue;
			}
			if (i > (n - suffixNoMaskLen - 1)) {
				sb.append(originStr.charAt(i));
				continue;
			}
			sb.append(maskStr);
		}
		return sb.toString();
	}

	/**
	 * 中文姓名只显示最后一个汉字
	 * @param fullName 姓名
	 * @return
	 */
	public static String chineseName(String fullName) {
		if (fullName == null) {
			return null;
		}
		return process(fullName, 0, 1, DEFAULT_PAD_STR);
	}

	/**
	 * 手机号码前三位，后四位，如186****2356
	 * @param num 手机号码
	 * @return
	 */
	public static String mobilePhone(String num) {
		return process(num, 0, 4, DEFAULT_PAD_STR);
	}

	/**
	 * 地址只显示到地区
	 * @param address 地址
	 * @return
	 */
	public static String address(String address) {
		return process(address, 6, 0, DEFAULT_PAD_STR);
	}

	/**
	 * 电子邮箱 仅显示第一个字母，@后面的地址显示，比如：r**@qq.com
	 * @param email 电子邮箱
	 * @return
	 */
	public static String email(String email) {
		if (email == null) {
			return null;
		}
		int index = StrUtil.indexOf(email, '@');
		if (index <= 1) {
			return email;
		}
		String preEmail = process(email.substring(0, index), 1, 0, DEFAULT_PAD_STR);
		return preEmail + email.substring(index);

	}

	/**
	 * 密码的全部字符，如：******
	 * @param password 密码
	 * @return
	 */
	public static String password(String password) {
		if (password == null) {
			return null;
		}
		return "******";
	}

	/**
	 * 密钥除了最后三位，全部，比如：***klo
	 * @param key 密钥
	 * @return 结果
	 */
	public static String key(String key) {
		if (key == null) {
			return null;
		}
		int viewLength = 6;
		StringBuilder tmpKey = new StringBuilder(process(key, 0, 3, DEFAULT_PAD_STR));
		if (tmpKey.length() > viewLength) {
			return tmpKey.substring(tmpKey.length() - viewLength);
		}
		else if (tmpKey.length() < viewLength) {
			int buffLength = viewLength - tmpKey.length();
			for (int i = 0; i < buffLength; i++) {
				tmpKey.insert(0, DEFAULT_PAD_STR);
			}
			return tmpKey.toString();
		}
		else {
			return tmpKey.toString();
		}
	}

	public static void main(String[] args) {
		String s = mobilePhone("18653653621");
		System.out.println(s);
	}
}