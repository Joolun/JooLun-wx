/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.dto;

import com.joolun.mall.entity.GoodsSpecValue;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
/**
 * SPU 绑定的可编辑规格定义。
 *
 * 管理端通过这个 DTO 维护商品规格组，
 * 例如“颜色”对应“红色、黑色”等规格值集合。
 *
 * @author www.joolun.com
 */
public class SpuSpecDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 编辑已有规格时的规格主键。 */
	private String id;

	/** 规格名称，例如颜色、尺码。 */
	private String name;

	/** 规格组排序值。 */
	private Integer sort;

	/** 管理端表单输入的规格值文本，多个值以逗号分隔。 */
	private String value;

	/** 结构化规格值列表，供详情回显和规格关系重建使用。 */
	private List<GoodsSpecValue> values;
}
