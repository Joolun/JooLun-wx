package com.joolun.common.sensitive;

/**
 * 敏感信息枚举类
 *
 * @author JooLun
 * @Author: https://www.cnblogs.com/xiluonanfeng/p/10183926.html
 **/
public enum SensitiveTypeEnum {

	/**
	 * 用户名, 李*天, 张*
	 */
	CHINESE_NAME,
	/**
	 * 手机号, 185****1653
	 */
	MOBILE_PHONE,
	/**
	 * 电子邮件, r*****o@qq.com
	 */
	EMAIL,
	/**
	 * 密码, ******
	 */
	PASSWORD,
	/**
	 * 密钥, 最后三位其他都是***
	 */
	KEY

}
