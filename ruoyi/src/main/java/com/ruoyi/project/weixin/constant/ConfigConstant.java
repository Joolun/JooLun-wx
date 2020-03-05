package com.ruoyi.project.weixin.constant;

/**
 * 全局常量
 * @author JL
 * 2019年1月21日
 */
public interface ConfigConstant {
	
	//订阅状态（0：已订阅；1：未订阅；2：网页授权用户）
	/**
	 * 0：未订阅，取消订阅
	 */
	String SUBSCRIBE_TYPE_NO = "0";
	/**
	 * 1：已订阅
	 */
	String SUBSCRIBE_TYPE_YES = "1";
  	/**
  	 * 2：网页授权用户
  	 */
  	String SUBSCRIBE_TYPE_WEBLICENS = "2";

	/**
	 * 应用类型 1:小程序
	 */
	String WX_APP_TYPE_1 = "1";
	/**
	 * 应用类型 2:公众号
	 */
	String WX_APP_TYPE_2 = "2";

	/**
	 * 消息自动回复类型（1、关注时回复；2、消息回复；3、关键词回复）
	 */
	String WX_AUTO_REPLY_TYPE_1 = "1";
	String WX_AUTO_REPLY_TYPE_2 = "2";
	String WX_AUTO_REPLY_TYPE_3 = "3";

	/**
	 * 回复类型文本匹配类型（1、全匹配，2、半匹配）
	 */
	String WX_REP_MATE_1 = "1";
	String WX_REP_MATE_2 = "2";

	/**
	 * 消息分类（1、用户发给公众号；2、公众号发给用户；）
	 */
	String WX_MSG_TYPE_1 = "1";
	String WX_MSG_TYPE_2 = "2";

	/**
	 * 群发消息发送类型（1、分组发；2、选择用户发）
	 */
	String WX_MASS_SEND_TYPE_1 = "1";
	String WX_MASS_SEND_TYPE_2 = "2";

	/**
	 *  群发消息发送后的状态(SUB_SUCCESS：提交成功，SUB_FAIL：提交失败，SEND_SUCCESS：发送成功，SENDING：发送中，SEND_FAIL：发送失败，DELETE：已删除)
	 */
	String WX_MASS_STATUS_SUB_SUCCESS = "SUB_SUCCESS";
	String WX_MASS_STATUS_SUB_FAIL = "SUB_FAIL";
	String WX_MASS_STATUS_SEND_SUCCESS = "SEND_SUCCESS";
	String WX_MASS_STATUS_SENDING = "SENDING";
	String WX_MASS_STATUS_SEND_FAIL = "SEND_FAIL";
	String WX_MASS_STATUS_DELETE = "DELETE";

	/**
	 * 菜单类型(1:普通菜单，2:个性化菜单)
	 */
	String WX_MENU_TYPE_1 = "1";
	String WX_MENU_TYPE_2 = "2";
}
