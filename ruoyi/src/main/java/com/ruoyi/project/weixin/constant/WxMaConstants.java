package com.ruoyi.project.weixin.constant;

/**
 * @author
 */
public interface WxMaConstants {

	/**
	 * redis中3rd_session过期时间(单位：小时)
	 */
	long TIME_OUT_SESSION = 6;
	/**
	 * redis中3rd_session拼接前缀
	 */
	String THIRD_SESSION_BEGIN = "wx:ma:3rd_session";
}
