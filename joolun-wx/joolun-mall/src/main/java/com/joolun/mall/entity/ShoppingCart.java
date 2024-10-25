/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.joolun.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 *
 * @author JL
 * @date 2019-08-29 21:27:33
 */
@Data
@TableName("shopping_cart")
@EqualsAndHashCode(callSuper = true)
public class ShoppingCart extends Model<ShoppingCart> {
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
   * 商品 SPU 编号
   */
	@Excel(name = "spuId")
    private String spuId;
	/**
	 * 加入时价格
	 */
	@Excel(name = "加入时价格")
	private BigDecimal addPrice;
    /**
   * 商品购买数量
   */
	@Excel(name = "商品购买数量")
    private Integer quantity;
	/**
	 * 加入时的spu名字
	 */
	@Excel(name = "加入时的spu名字")
	private String spuName;
	/**
	 * 图片
	 */
	@Excel(name = "图片")
	private String picUrl;

	@TableField(exist = false)
	private GoodsSpu goodsSpu;
}
