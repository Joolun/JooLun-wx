/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.joolun.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户收货地址
 *
 * @author JL
 * @date 2019-09-11 14:28:59
 */
@Data
@TableName("user_address")
@EqualsAndHashCode(callSuper = true)
public class UserAddress extends Model<UserAddress> {
  private static final long serialVersionUID = 1L;

    /**
   * PK
   */
	@Excel(name = "PK")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
   * 逻辑删除标记（0：显示；1：隐藏）
   */
	@Excel(name = "逻辑删除标记")
    private String delFlag;
    /**
   * 创建时间
   */
	@Excel(name = "创建时间")
    private LocalDateTime createTime;
    /**
   * 最后更新时间
   */
	@Excel(name = "最后更新时间")
    private LocalDateTime updateTime;
    /**
   * 用户编号
   */
	@Excel(name = "用户编号")
    private String userId;
    /**
   * 收货人名字
   */
	@Excel(name = "收货人名字")
    private String userName;
    /**
   * 邮编
   */
	@Excel(name = "邮编")
    private String postalCode;
    /**
   * 省名
   */
	@Excel(name = "省名")
    private String provinceName;
    /**
   * 市名
   */
	@Excel(name = "市名")
    private String cityName;
    /**
   * 区名
   */
	@Excel(name = "区名")
    private String countyName;
    /**
   * 详情地址
   */
	@Excel(name = "详情地址")
    private String detailInfo;
    /**
   * 电话号码
   */
	@Excel(name = "电话号码")
    private String telNum;
    /**
   * 是否默认 1是0否
   */
	@Excel(name = "是否默认")
    private String isDefault;

}
