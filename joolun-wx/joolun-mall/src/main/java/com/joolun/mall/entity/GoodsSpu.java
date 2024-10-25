/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.joolun.common.annotation.Excel;
import com.joolun.framework.config.typehandler.ArrayStringTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * spu商品
 *
 * @author JL
 * @date 2019-08-12 16:25:10
 */
@Data
@TableName("goods_spu")
@EqualsAndHashCode(callSuper = true)
public class GoodsSpu extends Model<GoodsSpu> {
private static final long serialVersionUID = 1L;

    /**
   * PK
   */
	@Excel(name = "PK")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
   * spu编码
   */
	@Excel(name = "spu编码")
    private String spuCode;
    /**
   * spu名字
   */
	@Excel(name = "spu名字")
    private String name;
    /**
   * 卖点
   */
	@Excel(name = "卖点")
    private String sellPoint;
    /**
   * 描述
   */
	@Excel(name = "描述")
    private String description;
    /**
	 * 一级分类ID
	 */
	@Excel(name = "一级分类ID")
    private String categoryFirst;
	/**
	 * 二级分类ID
	 */
	@Excel(name = "二级分类ID")
	private String categorySecond;
    /**
   * 商品主图
   */
	@Excel(name = "商品主图")
	@TableField(typeHandler = ArrayStringTypeHandler.class, jdbcType= JdbcType.VARCHAR)
    private String[] picUrls;
    /**
   * 是否上架（0否 1是）
   */
	@Excel(name = "是否上架（0否 1是）")
    private String shelf;
    /**
   * 排序字段
   */
	@Excel(name = "排序字段")
    private Integer sort;
	/**
	 * 销售价格
	 */
	@Excel(name = "销售价格")
	private BigDecimal salesPrice;
	/**
	 * 市场价
	 */
	@Excel(name = "市场价")
	private BigDecimal marketPrice;
	/**
	 * 成本价
	 */
	@Excel(name = "成本价")
	private BigDecimal costPrice;
	/**
	 * 成本
	 */
	@Excel(name = "库存")
	private Integer stock;
	/**
	 * 销量
	 */
	@Excel(name = "销量")
	private Integer saleNum;
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
   * 逻辑删除标记（0：显示；1：隐藏）
   */
	@Excel(name = "逻辑删除标记（0：显示；1：隐藏）")
    private String delFlag;
	/**
	 * 版本号
	 */
	@Version
	private Integer version;
}
