/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
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
 * 商品类目
 *
 * @author www.joolun.com
 * @date 2019-08-12 11:46:28
 */
@Data
@TableName("goods_category")
@EqualsAndHashCode(callSuper = true)
public class GoodsCategory extends Model<GoodsCategory> {
private static final long serialVersionUID = 1L;

    /**
   * PK
   */
	@Excel(name = "PK")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
	/**
	 * （1：开启；0：关闭）
	 */
	@Excel(name = "1：开启；0：关闭")
	private String enable;
    /**
   * 父分类编号
   */
	@Excel(name = "父分类编号")
    private String parentId;
    /**
   * 名称
   */
	@Excel(name = "名称")
    private String name;
    /**
   * 描述
   */
	@Excel(name = "描述")
    private String description;
    /**
   * 图片
   */
	@Excel(name = "图片")
    private String picUrl;
    /**
   * 排序
   */
	@Excel(name = "排序")
    private Integer sort;
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
	@Excel(name = "逻辑删除标记")
    private String delFlag;
  
}
