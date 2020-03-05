/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.ruoyi.project.weixin.entity;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import me.chanjar.weixin.common.bean.menu.WxMenuRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义菜单模型
 *
 * @author JL
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
