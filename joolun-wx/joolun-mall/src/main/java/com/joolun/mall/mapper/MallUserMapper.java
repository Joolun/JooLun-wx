/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joolun.mall.entity.MallUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商城用户 Mapper。
 *
 * @author www.joolun.com
 */
public interface MallUserMapper extends BaseMapper<MallUser> {

	/**
	 * 查询后台商城用户概览统计。
	 *
	 * @param mallUser 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> selectSummary(@Param("query") MallUser mallUser);

	/**
	 * 查询后台会员运营标签原始值列表。
	 * 只返回命中过滤条件的 user_tag 字段，具体拆分和聚合放在服务层处理。
	 *
	 * @param mallUser 查询条件
	 * @return 标签原始值列表
	 */
	List<String> selectTagValues(@Param("query") MallUser mallUser);
}
