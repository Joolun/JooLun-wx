/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.entity;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义菜单模型
 *
 * @author www.joolun.com
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
	private String article_id;
}
