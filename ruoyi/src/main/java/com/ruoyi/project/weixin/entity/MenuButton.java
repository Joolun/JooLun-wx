/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.ruoyi.project.weixin.entity;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义菜单模型
 *
 * @author JL
 */
@Data
public class MenuButton implements Serializable {

	private String type;

	private String name;

	private String key;

	private String url;

	private String media_id;

	private String appid;

	private String pagepath;

	private List<MenuButton> sub_button = new ArrayList();
	/**
	 * content内容
	 */
	private JSONObject content;

	private String repContent;
	/**
	 * 消息类型
	 */
	private String repType;
	/**
	 * 消息名
	 */
	private String repName;
	/**
	 * 视频和音乐的描述
	 */
	private String repDesc;
	/**
	 * 视频和音乐的描述
	 */
	private String repUrl;
	/**
	 * 高质量链接
	 */
	private String repHqUrl;
	/**
	 * 缩略图的媒体id
	 */
	private String repThumbMediaId;
	/**
	 * 缩略图url
	 */
	private String repThumbUrl;
}
