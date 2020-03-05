package com.ruoyi.project.weixin.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpDataCubeService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

/**
 * 微信账号配置
 *
 * @author JL
 * @date 2019-03-23 21:26:35
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wxsummary")
public class WxSummaryController extends BaseController {

	private final WxMpService wxService;
	/**
	 * 获取用户增减数据
	 * @param appId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping("/usersummary")
//	@PreAuthorize("@ss.hasPermi('wxmp:wxsummary:index')")
	public AjaxResult getUsersummary(String appId, String startDate, String endDate) {
		try {
			WxMpDataCubeService wxMpDataCubeService = wxService.getDataCubeService();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return AjaxResult.success(wxMpDataCubeService.getUserSummary(sdf.parse(startDate), sdf.parse(endDate)));
		} catch (WxErrorException e) {
			e.printStackTrace();
			log.error("获取用户增减数据失败",e);
			return AjaxResult.error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户增减数据失败",e);
			return AjaxResult.error("获取用户增减数据失败");
		}
	}

	/**
	 * 获取累计用户数据
	 * @param appId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping("/usercumulate")
//	@PreAuthorize("@ss.hasPermi('wxmp:wxsummary:index')")
	public AjaxResult getUserCumulate(String appId, String startDate, String endDate){
		try {
			WxMpDataCubeService wxMpDataCubeService = wxService.getDataCubeService();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return AjaxResult.success(wxMpDataCubeService.getUserCumulate(sdf.parse(startDate), sdf.parse(endDate)));
		} catch (WxErrorException e) {
			e.printStackTrace();
			log.error("获取累计用户数据失败",e);
			return AjaxResult.error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户增减数据失败",e);
			return AjaxResult.error("获取用户增减数据失败");
		}
	}

	/**
	 * 获取接口分析数据
	 * @param appId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping("/interfacesummary")
//	@PreAuthorize("@ss.hasPermi('wxmp:wxsummary:index')")
	public AjaxResult getInterfaceSummary(String appId, String startDate, String endDate){
		try {
			WxMpDataCubeService wxMpDataCubeService = wxService.getDataCubeService();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return AjaxResult.success(wxMpDataCubeService.getInterfaceSummary(sdf.parse(startDate), sdf.parse(endDate)));
		} catch (WxErrorException e) {
			e.printStackTrace();
			log.error("获取接口分析数据失败",e);
			return AjaxResult.error(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			log.error("获取接口分析数据失败",e);
			return AjaxResult.error("获取接口分析数据失败");
		}
	}
}
