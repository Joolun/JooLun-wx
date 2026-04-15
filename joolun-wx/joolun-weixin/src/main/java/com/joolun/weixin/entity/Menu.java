/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.entity;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import me.chanjar.weixin.common.bean.menu.WxMenuRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义菜单模型
 *
 * @author www.joolun.com
 */
@Data
public class Menu implements Serializable {
  private static final long serialVersionUID = -7083914585539687746L;

  private List<MenuButton> button = new ArrayList<>();

  private WxMenuRule matchrule;

  /**
   * 反序列化
   */
  public static Menu fromJson(String json) {
	  return JSONUtil.parseObj(json).toBean(Menu.class);
  }

  public String toJson() {
    return JSONUtil.toJsonStr(this);
  }

  @Override
  public String toString() {
    return this.toJson();
  }

}
