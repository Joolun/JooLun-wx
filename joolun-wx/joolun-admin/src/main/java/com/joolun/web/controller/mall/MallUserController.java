/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joolun.common.annotation.Log;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.common.enums.BusinessType;
import com.joolun.common.enums.DesensitizedType;
import com.joolun.common.utils.StringUtils;
import com.joolun.common.utils.poi.ExcelUtil;
import com.joolun.mall.dto.MallUserExportDTO;
import com.joolun.mall.entity.MallUser;
import com.joolun.mall.entity.MallUserOperateLog;
import com.joolun.mall.entity.MallUserTag;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderItem;
import com.joolun.mall.entity.ShoppingCart;
import com.joolun.mall.entity.UserAddress;
import com.joolun.mall.service.MallUserOperateLogService;
import com.joolun.mall.service.MallUserService;
import com.joolun.mall.service.MallUserTagService;
import com.joolun.mall.service.OrderInfoService;
import com.joolun.mall.service.OrderItemService;
import com.joolun.mall.service.ShoppingCartService;
import com.joolun.mall.service.UserAddressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 商城用户后台控制器。
 * 用于商城会员列表查询、详情查看，以及基础资料维护。
 *
 * @author www.joolun.com
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/malluser")
public class MallUserController extends BaseController {

	private final MallUserService mallUserService;
	private final MallUserOperateLogService mallUserOperateLogService;
	private final MallUserTagService mallUserTagService;
	private final OrderInfoService orderInfoService;
	private final OrderItemService orderItemService;
	private final ShoppingCartService shoppingCartService;
	private final UserAddressService userAddressService;

	/**
	 * 分页查询商城用户。
	 * 支持按会员编号、昵称、真实姓名、手机号、会员身份、状态、注册来源、
	 * 时间区间、消费区间和活跃场景筛选。
	 *
	 * @param page 分页对象
	 * @param mallUser 查询条件
	 * @return 分页数据
	 */
	@GetMapping("/page")
	@PreAuthorize("@ss.hasPermi('mall:malluser:index')")
	public AjaxResult getMallUserPage(Page<MallUser> page, MallUser mallUser) {
		IPage<MallUser> resultPage = mallUserService.page(page, buildMallUserQueryWrapper(mallUser));
		maskMallUserPage(resultPage);
		return AjaxResult.success(resultPage);
	}

	/**
	 * 按当前筛选条件导出商城用户。
	 * 导出口径与后台列表保持一致，方便运营同学直接把筛选结果下发给客服或做二次分析。
	 *
	 * @param response 响应对象
	 * @param mallUser 查询条件
	 */
	@Log(title = "商城用户", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@PreAuthorize("@ss.hasPermi('mall:malluser:index')")
	public void export(HttpServletResponse response, MallUser mallUser) {
		List<MallUser> mallUserList = mallUserService.list(buildMallUserQueryWrapper(mallUser));
		maskMallUserList(mallUserList);
		ExcelUtil<MallUserExportDTO> excelUtil = new ExcelUtil<>(MallUserExportDTO.class);
		excelUtil.exportExcel(response, buildMallUserExportList(mallUserList), "商城用户数据");
	}

	/**
	 * 统一构造商城用户后台查询条件。
	 * 列表分页和导出都复用这里，避免出现页面看见的结果和导出的结果不一致。
	 *
	 * @param mallUser 查询条件
	 * @return 查询构造器
	 */
	private LambdaQueryWrapper<MallUser> buildMallUserQueryWrapper(MallUser mallUser) {
		MallUser query = mallUser == null ? new MallUser() : mallUser;
		LambdaQueryWrapper<MallUser> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(StringUtils.isNotBlank(query.getUserNo()), MallUser::getUserNo, query.getUserNo())
				.like(StringUtils.isNotBlank(query.getNickName()), MallUser::getNickName, query.getNickName())
				.like(StringUtils.isNotBlank(query.getRealName()), MallUser::getRealName, query.getRealName())
				.like(StringUtils.isNotBlank(query.getMobile()), MallUser::getMobile, query.getMobile())
				.like(StringUtils.isNotBlank(query.getUserTag()), MallUser::getUserTag, query.getUserTag())
				.eq(StringUtils.isNotBlank(query.getMemberFlag()), MallUser::getMemberFlag, query.getMemberFlag())
				.eq(StringUtils.isNotBlank(query.getStatus()), MallUser::getStatus, query.getStatus())
				.eq(StringUtils.isNotBlank(query.getRegisterSource()), MallUser::getRegisterSource, query.getRegisterSource())
				.ge(query.getRegisterTimeBegin() != null, MallUser::getRegisterTime, query.getRegisterTimeBegin())
				.le(query.getRegisterTimeEnd() != null, MallUser::getRegisterTime, query.getRegisterTimeEnd())
				.ge(query.getLastLoginTimeBegin() != null, MallUser::getLastLoginTime, query.getLastLoginTimeBegin())
				.le(query.getLastLoginTimeEnd() != null, MallUser::getLastLoginTime, query.getLastLoginTimeEnd())
				.ge(query.getMemberTimeBegin() != null, MallUser::getMemberTime, query.getMemberTimeBegin())
				.le(query.getMemberTimeEnd() != null, MallUser::getMemberTime, query.getMemberTimeEnd())
				.ge(query.getMinConsumeAmount() != null, MallUser::getConsumeAmount, query.getMinConsumeAmount())
				.le(query.getMaxConsumeAmount() != null, MallUser::getConsumeAmount, query.getMaxConsumeAmount())
				.ge(query.getMinOrderCount() != null, MallUser::getOrderCount, query.getMinOrderCount())
				.le(query.getMaxOrderCount() != null, MallUser::getOrderCount, query.getMaxOrderCount())
				.orderByDesc(MallUser::getLastLoginTime)
				.orderByDesc(MallUser::getCreateTime);
		applyActivityScene(wrapper, query.getActivityScene());
		if (query.getHasOrder() != null) {
			if (query.getHasOrder() == 1) {
				wrapper.ge(MallUser::getOrderCount, 1);
			} else {
				wrapper.eq(MallUser::getOrderCount, 0);
			}
		}
		if (query.getHasConsume() != null) {
			if (query.getHasConsume() == 1) {
				wrapper.ge(MallUser::getConsumeCount, 1);
			} else {
				wrapper.eq(MallUser::getConsumeCount, 0);
			}
		}
		return wrapper;
	}

	/**
	 * 叠加活跃场景筛选。
	 * 这里直接落在 mall_user 自身的登录时间字段上，避免为了后台筛选再做重聚合。
	 *
	 * @param wrapper 查询构造器
	 * @param activityScene 活跃场景编码
	 */
	private void applyActivityScene(LambdaQueryWrapper<MallUser> wrapper, String activityScene) {
		if (StringUtils.isBlank(activityScene)) {
			return;
		}
		LocalDateTime now = LocalDateTime.now();
		switch (activityScene) {
			case "active7":
				wrapper.ge(MallUser::getLastLoginTime, now.minusDays(7));
				break;
			case "wake30":
				wrapper.ge(MallUser::getLastLoginTime, now.minusDays(30))
						.lt(MallUser::getLastLoginTime, now.minusDays(7));
				break;
			case "silent30":
				wrapper.and(queryWrapper -> queryWrapper
						.isNull(MallUser::getLastLoginTime)
						.or()
						.lt(MallUser::getLastLoginTime, now.minusDays(30)));
				break;
			case "neverLogin":
				wrapper.isNull(MallUser::getLastLoginTime);
				break;
			default:
				break;
		}
	}

	/**
	 * 查询商城用户概览统计。
	 * 统计口径和会员列表筛选条件保持一致，用于后台顶部概览卡片。
	 *
	 * @param mallUser 查询条件
	 * @return 统计结果
	 */
	@GetMapping("/summary")
	@PreAuthorize("@ss.hasPermi('mall:malluser:index')")
	public AjaxResult getSummary(MallUser mallUser) {
		return AjaxResult.success(mallUserService.getAdminSummary(mallUser));
	}

	/**
	 * 查询商城用户运营标签分布。
	 * 用于后台会员页展示当前筛选口径下最常见的运营标签，帮助运营快速筛选或批量打标。
	 *
	 * @param mallUser 查询条件
	 * @return 标签分布
	 */
	@GetMapping("/tag/summary")
	@PreAuthorize("@ss.hasPermi('mall:malluser:index')")
	public AjaxResult getTagSummary(MallUser mallUser) {
		return AjaxResult.success(mallUserService.getAdminTagSummary(mallUser));
	}

	/**
	 * 查询启用中的会员标签选项。
	 * 用于后台会员页快捷打标和标签筛选。
	 *
	 * @return 标签选项
	 */
	@GetMapping("/tag/options")
	@PreAuthorize("@ss.hasPermi('mall:malluser:index')")
	public AjaxResult getTagOptions() {
		return AjaxResult.success(mallUserTagService.listActiveTags());
	}

	/**
	 * 查询会员标签库。
	 * 后台可查看标签启停状态、排序和当前使用人数。
	 *
	 * @return 标签库列表
	 */
	@GetMapping("/tag/library")
	@PreAuthorize("@ss.hasPermi('mall:malluser:index')")
	public AjaxResult getTagLibrary() {
		return AjaxResult.success(mallUserTagService.listTagLibrary());
	}

	/**
	 * 新增会员标签。
	 *
	 * @param mallUserTag 标签信息
	 * @return 处理结果
	 */
	@PostMapping("/tag")
	@PreAuthorize("@ss.hasPermi('mall:malluser:edit')")
	public AjaxResult addTag(@RequestBody MallUserTag mallUserTag) {
		String tagName = normalizeTagName(mallUserTag == null ? null : mallUserTag.getTagName());
		if (StringUtils.isBlank(tagName)) {
			return AjaxResult.error("标签名称不能为空");
		}
		boolean exists = mallUserTagService.count(Wrappers.<MallUserTag>lambdaQuery()
				.eq(MallUserTag::getTagName, tagName)
				.eq(MallUserTag::getDelFlag, "0")) > 0;
		if (exists) {
			return AjaxResult.error("标签名称已存在");
		}
		LocalDateTime now = LocalDateTime.now();
		MallUserTag saveTag = new MallUserTag();
		saveTag.setTagName(tagName);
		saveTag.setStatus(resolveStatus(mallUserTag == null ? null : mallUserTag.getStatus()));
		saveTag.setSort(mallUserTag == null || mallUserTag.getSort() == null ? 0 : mallUserTag.getSort());
		saveTag.setRemark(StringUtils.isBlank(mallUserTag == null ? null : mallUserTag.getRemark()) ? null : mallUserTag.getRemark().trim());
		saveTag.setCreateTime(now);
		saveTag.setUpdateTime(now);
		saveTag.setDelFlag("0");
		return AjaxResult.success(mallUserTagService.save(saveTag));
	}

	/**
	 * 修改会员标签。
	 *
	 * @param mallUserTag 标签信息
	 * @return 处理结果
	 */
	@PutMapping("/tag")
	@PreAuthorize("@ss.hasPermi('mall:malluser:edit')")
	public AjaxResult updateTag(@RequestBody MallUserTag mallUserTag) {
		if (mallUserTag == null || StringUtils.isBlank(mallUserTag.getId())) {
			return AjaxResult.error("标签ID不能为空");
		}
		MallUserTag dbTag = mallUserTagService.getById(mallUserTag.getId());
		if (dbTag == null) {
			return AjaxResult.error("标签不存在");
		}
		String tagName = normalizeTagName(mallUserTag.getTagName());
		if (StringUtils.isBlank(tagName)) {
			return AjaxResult.error("标签名称不能为空");
		}
		boolean exists = mallUserTagService.count(Wrappers.<MallUserTag>lambdaQuery()
				.ne(MallUserTag::getId, dbTag.getId())
				.eq(MallUserTag::getTagName, tagName)
				.eq(MallUserTag::getDelFlag, "0")) > 0;
		if (exists) {
			return AjaxResult.error("标签名称已存在");
		}
		dbTag.setTagName(tagName);
		dbTag.setStatus(resolveStatus(mallUserTag.getStatus()));
		dbTag.setSort(mallUserTag.getSort() == null ? 0 : mallUserTag.getSort());
		dbTag.setRemark(StringUtils.isBlank(mallUserTag.getRemark()) ? null : mallUserTag.getRemark().trim());
		dbTag.setUpdateTime(LocalDateTime.now());
		return AjaxResult.success(mallUserTagService.updateById(dbTag));
	}

	/**
	 * 查询商城用户详情。
	 *
	 * @param id 商城用户主键
	 * @return 商城用户详情
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@ss.hasPermi('mall:malluser:get')")
	public AjaxResult getById(@PathVariable("id") String id) {
		MallUser mallUser = mallUserService.getById(id);
		if (mallUser == null) {
			return AjaxResult.success();
		}
		fillMallUserDetail(mallUser);
		maskMallUser(mallUser);
		return AjaxResult.success(mallUser);
	}

	/**
	 * 修改商城用户基础资料。
	 * 当前只开放后台维护资料，不提供手工新增和删除入口。
	 *
	 * @param mallUser 商城用户
	 * @return 处理结果
	 */
	@PutMapping
	@PreAuthorize("@ss.hasPermi('mall:malluser:edit')")
	public AjaxResult updateById(@RequestBody MallUser mallUser) {
		if (StringUtils.isBlank(mallUser.getId())) {
			return AjaxResult.error("商城用户ID不能为空");
		}
		MallUser dbMallUser = mallUserService.getById(mallUser.getId());
		if (dbMallUser == null) {
			return AjaxResult.error("商城用户不存在");
		}
		String oldRealName = dbMallUser.getRealName();
		String oldSex = dbMallUser.getSex();
		String oldBirthday = dbMallUser.getBirthday() == null ? null : String.valueOf(dbMallUser.getBirthday());
		String oldUserTag = dbMallUser.getUserTag();
		String oldStatus = dbMallUser.getStatus();
		String oldRemark = dbMallUser.getRemark();
		dbMallUser.setRealName(StringUtils.isBlank(mallUser.getRealName()) ? null : mallUser.getRealName().trim());
		dbMallUser.setSex(resolveSex(mallUser.getSex()));
		dbMallUser.setBirthday(mallUser.getBirthday());
		dbMallUser.setUserTag(resolveUserTag(mallUser.getUserTag()));
		dbMallUser.setStatus(resolveStatus(mallUser.getStatus()));
		dbMallUser.setRemark(StringUtils.isBlank(mallUser.getRemark()) ? null : mallUser.getRemark().trim());
		mallUserTagService.saveIfAbsent(splitTagList(dbMallUser.getUserTag()));
		boolean success = mallUserService.updateById(dbMallUser);
		if (success) {
			String newBirthday = dbMallUser.getBirthday() == null ? null : String.valueOf(dbMallUser.getBirthday());
			String operateContent = buildProfileOperateContent(oldRealName, dbMallUser.getRealName(), oldSex, dbMallUser.getSex(),
					oldBirthday, newBirthday, oldUserTag, dbMallUser.getUserTag(), oldStatus, dbMallUser.getStatus(),
					oldRemark, dbMallUser.getRemark());
			recordOperateLog(dbMallUser.getId(), "PROFILE", "修改会员资料", operateContent);
		}
		return AjaxResult.success(success);
	}

	/**
	 * 批量更新商城用户状态。
	 * 主要用于后台批量启用、禁用会员账号。
	 *
	 * @param mallUser 批量请求参数
	 * @return 处理结果
	 */
	@PutMapping("/batch/status")
	@PreAuthorize("@ss.hasPermi('mall:malluser:edit')")
	public AjaxResult batchUpdateStatus(@RequestBody MallUser mallUser) {
		List<MallUser> mallUsers = listBatchMallUsers(mallUser.getIds());
		if (mallUsers.isEmpty()) {
			return AjaxResult.error("请选择要操作的商城用户");
		}
		String status = resolveStatus(mallUser.getStatus());
		mallUsers.forEach(item -> item.setStatus(status));
		boolean success = mallUserService.updateBatchById(mallUsers);
		if (success) {
			recordBatchOperateLog(mallUsers, "STATUS", "批量更新会员状态", "批量将会员状态设置为：" + resolveStatusText(status));
		}
		return AjaxResult.success(success);
	}

	/**
	 * 批量更新商城用户备注。
	 * 便于运营人员给一批目标会员打运营标签或跟进说明。
	 *
	 * @param mallUser 批量请求参数
	 * @return 处理结果
	 */
	@PutMapping("/batch/remark")
	@PreAuthorize("@ss.hasPermi('mall:malluser:edit')")
	public AjaxResult batchUpdateRemark(@RequestBody MallUser mallUser) {
		List<MallUser> mallUsers = listBatchMallUsers(mallUser.getIds());
		if (mallUsers.isEmpty()) {
			return AjaxResult.error("请选择要操作的商城用户");
		}
		String remark = StringUtils.isBlank(mallUser.getRemark()) ? null : mallUser.getRemark().trim();
		mallUsers.forEach(item -> item.setRemark(remark));
		boolean success = mallUserService.updateBatchById(mallUsers);
		if (success) {
			recordBatchOperateLog(mallUsers, "REMARK", "批量更新会员备注", "批量备注内容：" + (StringUtils.isBlank(remark) ? "已清空备注" : remark));
		}
		return AjaxResult.success(success);
	}

	/**
	 * 批量更新商城用户运营标签。
	 * 适合后台按当前筛选结果给一批会员统一打上运营分组，便于后续筛选和导出。
	 *
	 * @param mallUser 批量请求参数
	 * @return 处理结果
	 */
	@PutMapping("/batch/tag")
	@PreAuthorize("@ss.hasPermi('mall:malluser:edit')")
	public AjaxResult batchUpdateTag(@RequestBody MallUser mallUser) {
		List<MallUser> mallUsers = listBatchMallUsers(mallUser.getIds());
		if (mallUsers.isEmpty()) {
			return AjaxResult.error("请选择要操作的商城用户");
		}
		String userTag = resolveUserTag(mallUser.getUserTag());
		if (StringUtils.isBlank(userTag)) {
			return AjaxResult.error("运营标签不能为空");
		}
		mallUsers.forEach(item -> item.setUserTag(userTag));
		mallUserTagService.saveIfAbsent(splitTagList(userTag));
		boolean success = mallUserService.updateBatchById(mallUsers);
		if (success) {
			recordBatchOperateLog(mallUsers, "TAG", "批量更新运营标签", "批量将运营标签设置为：" + userTag);
		}
		return AjaxResult.success(success);
	}

	/**
	 * 新增会员跟进记录。
	 *
	 * @param id 商城用户ID
	 * @param operateLog 跟进记录
	 * @return 处理结果
	 */
	@PostMapping("/{id}/follow")
	@PreAuthorize("@ss.hasPermi('mall:malluser:edit')")
	public AjaxResult addFollowRecord(@PathVariable("id") String id, @RequestBody MallUserOperateLog operateLog) {
		if (StringUtils.isBlank(id)) {
			return AjaxResult.error("商城用户ID不能为空");
		}
		if (mallUserService.getById(id) == null) {
			return AjaxResult.error("商城用户不存在");
		}
		String operateContent = operateLog == null ? null : StringUtils.trim(operateLog.getOperateContent());
		if (StringUtils.isBlank(operateContent)) {
			return AjaxResult.error("跟进内容不能为空");
		}
		mallUserOperateLogService.saveOperateLog(id, "FOLLOW_UP", "新增跟进记录", operateContent,
				String.valueOf(getUserId()), getUsername(), null);
		return AjaxResult.success();
	}

	/**
	 * 规范化性别值。
	 *
	 * @param sex 性别值
	 * @return 规范化后的性别
	 */
	private String resolveSex(String sex) {
		if ("1".equals(sex) || "2".equals(sex)) {
			return sex;
		}
		return "0";
	}

	/**
	 * 规范化会员状态。
	 *
	 * @param status 状态值
	 * @return 规范化后的状态
	 */
	private String resolveStatus(String status) {
		return "1".equals(status) ? "1" : "0";
	}

	/**
	 * 规范化运营标签。
	 *
	 * @param userTag 运营标签
	 * @return 规范化后的运营标签
	 */
	private String resolveUserTag(String userTag) {
		List<String> tagList = splitTagList(userTag);
		return tagList.isEmpty() ? null : String.join("，", tagList);
	}

	/**
	 * 拆分标签文本。
	 *
	 * @param userTag 标签原文
	 * @return 标签列表
	 */
	private List<String> splitTagList(String userTag) {
		if (StringUtils.isBlank(userTag)) {
			return new ArrayList<>();
		}
		return Arrays.stream(userTag.split("[，,、]"))
				.map(this::normalizeTagName)
				.filter(StringUtils::isNotBlank)
				.distinct()
				.collect(Collectors.toList());
	}

	/**
	 * 规范化单个标签名称。
	 *
	 * @param tagName 标签名称
	 * @return 规范化后的标签名称
	 */
	private String normalizeTagName(String tagName) {
		return StringUtils.trim(tagName).replaceAll("[，,、]+", "");
	}

	/**
	 * 组装导出数据。
	 * 导出字段统一在 DTO 中声明，避免把页面展示专用字段混到实体类里。
	 *
	 * @param mallUserList 商城用户列表
	 * @return 导出数据列表
	 */
	private List<MallUserExportDTO> buildMallUserExportList(List<MallUser> mallUserList) {
		return mallUserList.stream()
				.map(this::buildMallUserExportDTO)
				.collect(Collectors.toList());
	}

	/**
	 * 将商城用户实体转换为导出对象。
	 *
	 * @param mallUser 商城用户
	 * @return 导出对象
	 */
	private MallUserExportDTO buildMallUserExportDTO(MallUser mallUser) {
		MallUserExportDTO exportDTO = new MallUserExportDTO();
		exportDTO.setId(mallUser.getId());
		exportDTO.setUserNo(mallUser.getUserNo());
		exportDTO.setNickName(mallUser.getNickName());
		exportDTO.setRealName(mallUser.getRealName());
		exportDTO.setMobile(mallUser.getMobile());
		exportDTO.setUserTag(mallUser.getUserTag());
		exportDTO.setMemberIdentity(resolveMemberIdentityText(mallUser.getMemberFlag()));
		exportDTO.setMemberTime(mallUser.getMemberTime());
		exportDTO.setRegisterSourceText(resolveRegisterSourceText(mallUser.getRegisterSource()));
		exportDTO.setStatusText(resolveStatusText(mallUser.getStatus()));
		exportDTO.setOrderCount(mallUser.getOrderCount());
		exportDTO.setConsumeCount(mallUser.getConsumeCount());
		exportDTO.setRefundCount(mallUser.getRefundCount());
		exportDTO.setConsumeAmount(mallUser.getConsumeAmount());
		exportDTO.setRegisterTime(mallUser.getRegisterTime());
		exportDTO.setLastLoginTime(mallUser.getLastLoginTime());
		exportDTO.setRemark(mallUser.getRemark());
		return exportDTO;
	}

	/**
	 * 解析会员身份文案。
	 *
	 * @param memberFlag 会员标记
	 * @return 会员身份文案
	 */
	private String resolveMemberIdentityText(String memberFlag) {
		return "1".equals(memberFlag) ? "已入会" : "未入会";
	}

	/**
	 * 解析注册来源文案。
	 *
	 * @param registerSource 注册来源编码
	 * @return 注册来源文案
	 */
	private String resolveRegisterSourceText(String registerSource) {
		switch (registerSource) {
			case "1":
				return "小程序";
			case "2":
				return "公众号";
			case "3":
				return "H5";
			case "4":
				return "APP";
			default:
				return "其他";
		}
	}

	/**
	 * 解析状态文案。
	 *
	 * @param status 状态编码
	 * @return 状态文案
	 */
	private String resolveStatusText(String status) {
		return "1".equals(status) ? "禁用" : "正常";
	}

	/**
	 * 解析性别文案。
	 *
	 * @param sex 性别编码
	 * @return 性别文案
	 */
	private String resolveSexText(String sex) {
		if ("1".equals(sex)) {
			return "男";
		}
		if ("2".equals(sex)) {
			return "女";
		}
		return "未知";
	}

	/**
	 * 记录单个会员运营日志。
	 *
	 * @param userId 商城用户ID
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 */
	private void recordOperateLog(String userId, String operateType, String operateTitle, String operateContent) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(operateContent)) {
			return;
		}
		mallUserOperateLogService.saveOperateLog(userId, operateType, operateTitle, operateContent,
				String.valueOf(getUserId()), getUsername(), null);
	}

	/**
	 * 批量记录会员运营日志。
	 *
	 * @param mallUsers 商城用户列表
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 */
	private void recordBatchOperateLog(List<MallUser> mallUsers, String operateType, String operateTitle, String operateContent) {
		if (mallUsers == null || mallUsers.isEmpty() || StringUtils.isBlank(operateContent)) {
			return;
		}
		List<String> userIds = mallUsers.stream()
				.map(MallUser::getId)
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());
		mallUserOperateLogService.saveBatchOperateLog(userIds, operateType, operateTitle, operateContent,
				String.valueOf(getUserId()), getUsername(), null);
	}

	/**
	 * 组装会员资料修改日志内容。
	 *
	 * @return 日志内容
	 */
	private String buildProfileOperateContent(String oldRealName, String newRealName, String oldSex, String newSex,
			String oldBirthday, String newBirthday, String oldUserTag, String newUserTag, String oldStatus, String newStatus,
			String oldRemark, String newRemark) {
		List<String> changeList = new ArrayList<>();
		appendChangeLog(changeList, "真实姓名", oldRealName, newRealName);
		appendChangeLog(changeList, "性别", resolveSexText(oldSex), resolveSexText(newSex));
		appendChangeLog(changeList, "生日", StringUtils.isBlank(oldBirthday) ? "未填写" : oldBirthday, StringUtils.isBlank(newBirthday) ? "未填写" : newBirthday);
		appendChangeLog(changeList, "运营标签", StringUtils.isBlank(oldUserTag) ? "未打标签" : oldUserTag, StringUtils.isBlank(newUserTag) ? "未打标签" : newUserTag);
		appendChangeLog(changeList, "账号状态", resolveStatusText(oldStatus), resolveStatusText(newStatus));
		appendChangeLog(changeList, "备注", StringUtils.isBlank(oldRemark) ? "无" : oldRemark, StringUtils.isBlank(newRemark) ? "无" : newRemark);
		return changeList.isEmpty() ? null : String.join("；", changeList);
	}

	/**
	 * 追加字段变更说明。
	 *
	 * @param changeList 变更列表
	 * @param fieldName 字段名
	 * @param oldValue 旧值
	 * @param newValue 新值
	 */
	private void appendChangeLog(List<String> changeList, String fieldName, String oldValue, String newValue) {
		if (!Objects.equals(oldValue, newValue)) {
			changeList.add(fieldName + "：" + oldValue + " -> " + newValue);
		}
	}

	/**
	 * 解析批量操作涉及的商城用户。
	 * 这里统一去空、去重后再查库，避免前端重复提交同一主键。
	 *
	 * @param ids 主键集合
	 * @return 已存在的商城用户列表
	 */
	private List<MallUser> listBatchMallUsers(List<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<>();
		}
		List<String> validIds = ids.stream()
				.filter(StringUtils::isNotBlank)
				.map(String::trim)
				.collect(Collectors.collectingAndThen(Collectors.toCollection(LinkedHashSet::new), ArrayList::new));
		if (validIds.isEmpty()) {
			return new ArrayList<>();
		}
		return mallUserService.listByIds(validIds);
	}

	/**
	 * 补齐后台会员详情需要的聚合信息。
	 * 当前先聚合最近订单、收货地址和几个待处理订单统计，方便后台客服查看。
	 *
	 * @param mallUser 商城用户
	 */
	private void fillMallUserDetail(MallUser mallUser) {
		LocalDateTime now = LocalDateTime.now();
		List<OrderInfo> recentOrderList = orderInfoService.list(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getUserId, mallUser.getId())
				.orderByDesc(OrderInfo::getCreateTime)
				.last("limit 5"))
			.stream()
			.map(orderInfo -> orderInfoService.getById2(orderInfo.getId()))
			.filter(orderInfo -> orderInfo != null)
			.collect(Collectors.toList());
		mallUser.setRecentOrderList(recentOrderList);
		mallUser.setRecentOperateLogList(mallUserOperateLogService.listRecentByUserId(mallUser.getId(), 20));
		mallUser.setLastOrderTime(recentOrderList.stream()
				.map(OrderInfo::getCreateTime)
				.filter(createTime -> createTime != null)
				.max(Comparator.naturalOrder())
				.orElse(null));

		OrderInfo recentPaidOrder = orderInfoService.list(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getUserId, mallUser.getId())
				.eq(OrderInfo::getIsPay, "1")
				.orderByDesc(OrderInfo::getPaymentTime)
				.last("limit 1"))
			.stream()
			.findFirst()
			.orElse(null);
		mallUser.setRecentPaymentTime(recentPaidOrder == null ? null : recentPaidOrder.getPaymentTime());
		mallUser.setRecentPaymentOrderNo(recentPaidOrder == null ? null : recentPaidOrder.getOrderNo());

		mallUser.setRecentActiveTime(maxTime(mallUser.getLastLoginTime(), mallUser.getLastOrderTime(), mallUser.getUpdateTime(), mallUser.getCreateTime()));
		mallUser.setMemberAgeDays(resolveMemberAgeDays(mallUser, now));
		mallUser.setThirtyDayOrderCount((int) orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getUserId, mallUser.getId())
				.ge(OrderInfo::getCreateTime, now.minusDays(30))));
		mallUser.setThirtyDayConsumeAmount(resolveThirtyDayConsumeAmount(mallUser.getId(), now));
		mallUser.setWaitPayOrderCount((int) orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getUserId, mallUser.getId())
				.eq(OrderInfo::getIsPay, "0")
				.isNull(OrderInfo::getStatus)));
		mallUser.setWaitDeliveryOrderCount((int) orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getUserId, mallUser.getId())
				.eq(OrderInfo::getStatus, "1")));
		mallUser.setWaitReceiveOrderCount((int) orderInfoService.count(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getUserId, mallUser.getId())
				.eq(OrderInfo::getStatus, "2")));
		fillAfterSaleAndCartStats(mallUser);

		List<UserAddress> recentAddressList = userAddressService.list(Wrappers.<UserAddress>lambdaQuery()
				.eq(UserAddress::getUserId, mallUser.getId())
				.orderByDesc(UserAddress::getIsDefault)
				.orderByDesc(UserAddress::getUpdateTime)
				.orderByDesc(UserAddress::getCreateTime)
				.last("limit 5"));
		mallUser.setRecentAddressList(recentAddressList);
		mallUser.setAddressCount((int) userAddressService.count(Wrappers.<UserAddress>lambdaQuery()
				.eq(UserAddress::getUserId, mallUser.getId())));
		mallUser.setDefaultAddressText(recentAddressList.stream()
				.filter(address -> "1".equals(address.getIsDefault()))
				.findFirst()
				.map(this::buildAddressText)
				.orElseGet(() -> recentAddressList.stream()
						.findFirst()
						.map(this::buildAddressText)
						.orElse("暂无收货地址")));
	}

	/**
	 * 组装售后和购物车相关指标。
	 * 售后当前仍依赖订单项状态，因此这里按会员全部订单项做轻量聚合。
	 *
	 * @param mallUser 商城用户
	 */
	private void fillAfterSaleAndCartStats(MallUser mallUser) {
		List<String> orderIdList = orderInfoService.list(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getUserId, mallUser.getId()))
			.stream()
			.map(OrderInfo::getId)
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toList());
		List<OrderItem> orderItemList = orderIdList.isEmpty() ? new ArrayList<>() : orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
				.in(OrderItem::getOrderId, orderIdList));
		mallUser.setAfterSaleCount((int) orderItemList.stream()
				.filter(this::isAfterSaleItem)
				.count());
		mallUser.setRefundedAmount(orderItemList.stream()
				.filter(orderItem -> "1".equals(orderItem.getIsRefund()))
				.map(OrderItem::getPaymentPrice)
				.filter(Objects::nonNull)
				.reduce(BigDecimal.ZERO, BigDecimal::add));

		List<ShoppingCart> shoppingCartList = shoppingCartService.list(Wrappers.<ShoppingCart>lambdaQuery()
				.eq(ShoppingCart::getUserId, mallUser.getId()));
		mallUser.setCartSkuCount(shoppingCartList.size());
		mallUser.setCartQuantity(shoppingCartList.stream()
				.map(ShoppingCart::getQuantity)
				.filter(Objects::nonNull)
				.reduce(0, Integer::sum));
	}

	/**
	 * 判断订单项是否已进入售后链路。
	 *
	 * @param orderItem 订单项
	 * @return true 表示已进入售后
	 */
	private boolean isAfterSaleItem(OrderItem orderItem) {
		return orderItem != null && ("1".equals(orderItem.getIsRefund())
				|| "1".equals(orderItem.getStatus())
				|| "2".equals(orderItem.getStatus())
				|| "3".equals(orderItem.getStatus()));
	}

	/**
	 * 统计近 30 天消费金额。
	 *
	 * @param mallUserId 商城用户主键
	 * @param now 当前时间
	 * @return 近 30 天消费金额
	 */
	private BigDecimal resolveThirtyDayConsumeAmount(String mallUserId, LocalDateTime now) {
		return orderInfoService.list(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getUserId, mallUserId)
				.eq(OrderInfo::getIsPay, "1")
				.ge(OrderInfo::getPaymentTime, now.minusDays(30)))
			.stream()
			.map(OrderInfo::getPaymentPrice)
			.filter(Objects::nonNull)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 计算入会天数。
	 *
	 * @param mallUser 商城用户
	 * @param now 当前时间
	 * @return 入会天数
	 */
	private Long resolveMemberAgeDays(MallUser mallUser, LocalDateTime now) {
		if (!"1".equals(mallUser.getMemberFlag()) || mallUser.getMemberTime() == null) {
			return null;
		}
		LocalDateTime beginTime = mallUser.getMemberTime();
		if (beginTime == null) {
			return null;
		}
		long dayCount = ChronoUnit.DAYS.between(beginTime.toLocalDate(), now.toLocalDate());
		return Math.max(dayCount, 0L);
	}

	/**
	 * 取多个时间中的最近一个。
	 *
	 * @param timeValues 时间列表
	 * @return 最近时间
	 */
	private LocalDateTime maxTime(LocalDateTime... timeValues) {
		LocalDateTime maxTime = null;
		for (LocalDateTime timeValue : timeValues) {
			if (timeValue == null) {
				continue;
			}
			if (maxTime == null || timeValue.isAfter(maxTime)) {
				maxTime = timeValue;
			}
		}
		return maxTime;
	}

	/**
	 * 拼装地址摘要文案。
	 *
	 * @param userAddress 收货地址
	 * @return 地址摘要
	 */
	/**
	 * 统一脱敏商城用户分页结果中的手机号。
	 *
	 * @param resultPage 分页结果
	 */
	private void maskMallUserPage(IPage<MallUser> resultPage) {
		if (resultPage == null || resultPage.getRecords() == null) {
			return;
		}
		maskMallUserList(resultPage.getRecords());
	}

	/**
	 * 批量脱敏商城用户手机号。
	 *
	 * @param mallUserList 商城用户列表
	 */
	private void maskMallUserList(List<MallUser> mallUserList) {
		if (mallUserList == null || mallUserList.isEmpty()) {
			return;
		}
		mallUserList.forEach(this::maskMallUser);
	}

	/**
	 * 脱敏单个商城用户手机号。
	 *
	 * @param mallUser 商城用户
	 */
	private void maskMallUser(MallUser mallUser) {
		if (mallUser == null) {
			return;
		}
		mallUser.setMobile(maskPhone(mallUser.getMobile()));
	}

	/**
	 * 统一按手机号规则脱敏。
	 *
	 * @param phone 原始手机号
	 * @return 脱敏后的手机号
	 */
	private String maskPhone(String phone) {
		if (StringUtils.isBlank(phone)) {
			return phone;
		}
		return DesensitizedType.PHONE.desensitizer().apply(phone);
	}

	private String buildAddressText(UserAddress userAddress) {
		if (userAddress == null) {
			return "暂无收货地址";
		}
		return StringUtils.nvl(userAddress.getProvinceName(), "")
				+ StringUtils.nvl(userAddress.getCityName(), "")
				+ StringUtils.nvl(userAddress.getCountyName(), "")
				+ StringUtils.nvl(userAddress.getDetailInfo(), "");
	}
}
