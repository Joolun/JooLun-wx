package com.ruoyi.project.weixin.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.weixin.service.WxMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 自定义菜单
 *
 * @author JL
 * @date 2019-03-27 16:52:10
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wxmenu")
public class WxMenuController extends BaseController {

	private final WxMenuService wxMenuService;

	/**
	 * 通过appId查询自定义菜单
	 *
	 * @return R
	 */
	@GetMapping("/list")
	@PreAuthorize("@ss.hasPermi('wxmp:wxmenu:get')")
	public AjaxResult getWxMenuButton() {
		return AjaxResult.success(wxMenuService.getWxMenuButton());
	}

	/**
	 * 保存并发布菜单
	 *
	 * @param
	 * @return R
	 */
	@PostMapping("/release")
	@PreAuthorize("@ss.hasPermi('wxmp:wxmenu:add')")
	public AjaxResult saveAndRelease(@RequestBody String data) {
		JSONObject jSONObject = JSONUtil.parseObj(data);
		String strWxMenu = jSONObject.getStr("strWxMenu");
		String appId = jSONObject.getStr("appId");
		try {
			wxMenuService.saveAndRelease(strWxMenu);
			return AjaxResult.success();
		} catch (WxErrorException e) {
			e.printStackTrace();
			log.error("发布自定义菜单失败appID:" + appId + ":" + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}

}
