/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.entity;

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
@EqualsAndHashCode(callSuper = true)
public class GoodsCategoryTree extends TreeNode {

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
