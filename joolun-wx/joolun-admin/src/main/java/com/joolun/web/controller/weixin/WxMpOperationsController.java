/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.weixin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.weixin.entity.WxUser;
import com.joolun.weixin.service.WxUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpCommentService;
import me.chanjar.weixin.mp.api.WxMpDataCubeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.WxMpMassPreviewMessage;
import me.chanjar.weixin.mp.bean.WxMpMassTagMessage;
import me.chanjar.weixin.mp.bean.kefu.request.WxMpKfAccountRequest;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 单公众号运营能力：模板消息、渠道二维码、数据、客服、群发和评论。
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wxmp/operations")
public class WxMpOperationsController extends BaseController {
	private static final Pattern CUSTOMER_ACCOUNT_PATTERN = Pattern.compile("^[A-Za-z0-9_]{1,10}@[A-Za-z0-9_-]{1,30}$");
	private static final String CUSTOMER_ACCOUNT_FORMAT_MESSAGE =
			"客服账号格式不正确，请填写“账号前缀@公众号微信号”：前缀限1-10位英文、数字或下划线，@后必须是公众平台设置的公众号微信号（不是AppID、原始ID或公众号名称）";

	private final WxMpService wxMpService;
	private final WxUserService wxUserService;

	@GetMapping("/users")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult userOptions(@RequestParam(defaultValue = "") String keyword) {
		String search = keyword.trim();
		List<WxUserOption> users = wxUserService.list(Wrappers.<WxUser>lambdaQuery()
				.select(WxUser::getOpenId, WxUser::getNickName, WxUser::getHeadimgUrl)
				.eq(WxUser::getAppType, "2")
				.eq(WxUser::getSubscribe, "1")
				.isNotNull(WxUser::getOpenId)
				.ne(WxUser::getOpenId, "")
				.and(!search.isEmpty(), query -> query.like(WxUser::getNickName, search)
						.or().like(WxUser::getOpenId, search))
				.orderByDesc(WxUser::getUpdateTime)
				.last("LIMIT 50"))
				.stream()
				.map(user -> new WxUserOption(user.getOpenId(), user.getNickName(), user.getHeadimgUrl()))
				.toList();
		return success(users);
	}

	@GetMapping("/templates")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult templates() {
		return invoke(() -> wxMpService.getTemplateMsgService().getAllPrivateTemplate());
	}

	@PostMapping("/templates")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult addTemplate(@RequestBody TemplateAddRequest request) {
		return invoke(() -> wxMpService.getTemplateMsgService()
				.addTemplate(request.shortTemplateId(), request.keywordNameList()));
	}

	@DeleteMapping("/templates/{templateId}")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult deleteTemplate(@PathVariable String templateId) {
		return invoke(() -> wxMpService.getTemplateMsgService().delPrivateTemplate(templateId));
	}

	@PostMapping("/templates/send")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:send')")
	public AjaxResult sendTemplate(@RequestBody WxMpTemplateMessage message) {
		return invoke(() -> wxMpService.getTemplateMsgService().sendTemplateMsg(message));
	}

	@PostMapping("/qrcode")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult createQrcode(@RequestBody QrcodeRequest request) {
		return invoke(() -> {
			WxMpQrCodeTicket ticket;
			if (Boolean.TRUE.equals(request.permanent())) {
				ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(request.scene());
			} else {
				int expires = request.expireSeconds() == null ? 2592000 : request.expireSeconds();
				ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(request.scene(), expires);
			}
			Map<String, Object> result = new LinkedHashMap<>();
			result.put("ticket", ticket.getTicket());
			result.put("expireSeconds", ticket.getExpireSeconds());
			result.put("url", ticket.getUrl());
			result.put("imageUrl", wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket()));
			return result;
		});
	}

	@GetMapping("/analytics")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult analytics(@RequestParam String metric,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return invoke(() -> queryAnalytics(metric, startDate, endDate));
	}

	@GetMapping("/customer/accounts")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult customerAccounts() {
		return invoke(() -> wxMpService.getKefuService().kfList());
	}

	@GetMapping("/customer/online")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult onlineAccounts() {
		return invoke(() -> wxMpService.getKefuService().kfOnlineList());
	}

	@PostMapping("/customer/accounts")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult addCustomerAccount(@RequestBody CustomerAccountRequest request) {
		return invoke(() -> wxMpService.getKefuService().kfAccountAdd(request.toWxRequest()));
	}

	@PutMapping("/customer/accounts")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult updateCustomerAccount(@RequestBody CustomerAccountRequest request) {
		return invoke(() -> wxMpService.getKefuService().kfAccountUpdate(request.toWxRequest()));
	}

	@DeleteMapping("/customer/accounts/{account}")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult deleteCustomerAccount(@PathVariable String account) {
		return invoke(() -> wxMpService.getKefuService().kfAccountDel(account));
	}

	@GetMapping("/customer/sessions")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult sessions(@RequestParam String account) {
		return invoke(() -> wxMpService.getKefuService().kfSessionList(account));
	}

	@GetMapping("/customer/waiting")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult waitingSessions() {
		return invoke(() -> wxMpService.getKefuService().kfSessionGetWaitCase());
	}

	@PostMapping("/customer/sessions")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult createSession(@RequestBody SessionRequest request) {
		return invoke(() -> wxMpService.getKefuService().kfSessionCreate(request.openId(), request.account()));
	}

	@DeleteMapping("/customer/sessions")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult closeSession(@RequestParam String openId, @RequestParam String account) {
		return invoke(() -> wxMpService.getKefuService().kfSessionClose(openId, account));
	}

	@GetMapping("/customer/records")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult customerRecords(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
			@RequestParam(required = false) Long msgId,
			@RequestParam(defaultValue = "100") Integer number) {
		return invoke(() -> wxMpService.getKefuService().kfMsgList(startTime, endTime, msgId, number));
	}

	@PostMapping("/customer/messages")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:send')")
	public AjaxResult sendCustomerMessage(@RequestBody WxMpKefuMessage message) {
		return invoke(() -> wxMpService.getKefuService().sendKefuMessage(message));
	}

	@PostMapping("/mass/tag")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:send')")
	public AjaxResult massByTag(@RequestBody WxMpMassTagMessage message) {
		return invoke(() -> wxMpService.getMassMessageService().massGroupMessageSend(message));
	}

	@PostMapping("/mass/openids")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:send')")
	public AjaxResult massByOpenIds(@RequestBody WxMpMassOpenIdsMessage message) {
		return invoke(() -> wxMpService.getMassMessageService().massOpenIdsMessageSend(message));
	}

	@PostMapping("/mass/preview")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:send')")
	public AjaxResult massPreview(@RequestBody WxMpMassPreviewMessage message) {
		return invoke(() -> wxMpService.getMassMessageService().massMessagePreview(message));
	}

	@GetMapping("/mass/{msgId}")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult massStatus(@PathVariable Long msgId) {
		return invoke(() -> wxMpService.getMassMessageService().messageMassGet(msgId));
	}

	@DeleteMapping("/mass/{msgId}")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult deleteMass(@PathVariable Long msgId,
			@RequestParam(required = false) Integer articleIndex) {
		return invokeVoid(() -> wxMpService.getMassMessageService().delete(msgId, articleIndex));
	}

	@GetMapping("/mass-speed")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult massSpeed() {
		return invoke(() -> wxMpService.getMassMessageService().messageMassSpeedGet());
	}

	@PutMapping("/mass-speed/{speed}")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult updateMassSpeed(@PathVariable Integer speed) {
		return invokeVoid(() -> wxMpService.getMassMessageService().messageMassSpeedSet(speed));
	}

	@GetMapping("/comments")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:query')")
	public AjaxResult comments(@RequestParam String msgDataId,
			@RequestParam(defaultValue = "0") Integer index,
			@RequestParam(defaultValue = "0") Integer begin,
			@RequestParam(defaultValue = "20") Integer count,
			@RequestParam(defaultValue = "0") Integer type) {
		return invoke(() -> wxMpService.getCommentService().list(msgDataId, index, begin, count, type));
	}

	@PostMapping("/comments/{action}")
	@PreAuthorize("@ss.hasPermi('wxmp:operations:edit')")
	public AjaxResult operateComment(@PathVariable String action, @RequestBody CommentRequest request) {
		return invokeVoid(() -> {
			WxMpCommentService service = wxMpService.getCommentService();
			switch (action) {
				case "open" -> service.open(request.msgDataId(), request.index());
				case "close" -> service.close(request.msgDataId(), request.index());
				case "mark" -> service.markElect(request.msgDataId(), request.index(), request.userCommentId());
				case "unmark" -> service.unmarkElect(request.msgDataId(), request.index(), request.userCommentId());
				case "delete" -> service.delete(request.msgDataId(), request.index(), request.userCommentId());
				case "reply" -> service.replyAdd(request.msgDataId(), request.index(), request.userCommentId(), request.content());
				case "delete-reply" -> service.replyDelete(request.msgDataId(), request.index(), request.userCommentId());
				default -> throw new IllegalArgumentException("不支持的评论操作：" + action);
			}
		});
	}

	private Object queryAnalytics(String metric, Date startDate, Date endDate) throws WxErrorException {
		validateAnalyticsRange(metric, startDate, endDate);
		WxMpDataCubeService service = wxMpService.getDataCubeService();
		return switch (metric) {
			case "userSummary" -> service.getUserSummary(startDate, endDate);
			case "userCumulate" -> service.getUserCumulate(startDate, endDate);
			case "articleSummary" -> service.getArticleSummary(startDate, endDate);
			case "articleTotal" -> service.getArticleTotal(startDate, endDate);
			case "userRead" -> service.getUserRead(startDate, endDate);
			case "userReadHour" -> service.getUserReadHour(startDate, endDate);
			case "userShare" -> service.getUserShare(startDate, endDate);
			case "userShareHour" -> service.getUserShareHour(startDate, endDate);
			case "upstreamMsg" -> service.getUpstreamMsg(startDate, endDate);
			case "upstreamMsgHour" -> service.getUpstreamMsgHour(startDate, endDate);
			case "upstreamMsgWeek" -> service.getUpstreamMsgWeek(startDate, endDate);
			case "upstreamMsgMonth" -> service.getUpstreamMsgMonth(startDate, endDate);
			case "upstreamMsgDist" -> service.getUpstreamMsgDist(startDate, endDate);
			case "interfaceSummary" -> service.getInterfaceSummary(startDate, endDate);
			case "interfaceSummaryHour" -> service.getInterfaceSummaryHour(startDate, endDate);
			default -> throw new IllegalArgumentException("不支持的数据指标：" + metric);
		};
	}

	private void validateAnalyticsRange(String metric, Date startDate, Date endDate) {
		LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate latest = LocalDate.now().minusDays(1);
		if (start.isAfter(end)) {
			throw new IllegalArgumentException("开始日期不能晚于结束日期");
		}
		if (end.isAfter(latest)) {
			throw new IllegalArgumentException("微信数据最晚只能查询到昨天");
		}
		int maxDays = switch (metric) {
			case "articleSummary", "articleTotal", "userReadHour", "userShareHour",
					"upstreamMsgHour", "interfaceSummaryHour" -> 1;
			case "userRead" -> 3;
			case "userSummary", "userCumulate", "userShare", "upstreamMsg" -> 7;
			case "upstreamMsgDist" -> 15;
			case "upstreamMsgWeek", "upstreamMsgMonth", "interfaceSummary" -> 30;
			default -> throw new IllegalArgumentException("不支持的数据指标：" + metric);
		};
		long days = ChronoUnit.DAYS.between(start, end) + 1;
		if (days > maxDays) {
			throw new IllegalArgumentException("该指标最多查询连续 " + maxDays + " 天");
		}
	}

	private AjaxResult invoke(WxSupplier supplier) {
		try {
			return success(supplier.get());
		} catch (Exception e) {
			log.error("调用微信公众号接口失败", e);
			return error(resolveMessage(e));
		}
	}

	private AjaxResult invokeVoid(WxRunnable runnable) {
		return invoke(() -> {
			runnable.run();
			return Boolean.TRUE;
		});
	}

	private String resolveMessage(Exception e) {
		if (e instanceof WxErrorException wxError && wxError.getError() != null) {
			String message = wxError.getError().getErrorMsg();
			if (message != null && (message.contains("illegal custom service account")
					|| message.contains("invalid kf_account"))) {
				return CUSTOMER_ACCOUNT_FORMAT_MESSAGE;
			}
			return message;
		}
		return e.getMessage();
	}

	@FunctionalInterface
	private interface WxSupplier {
		Object get() throws Exception;
	}

	@FunctionalInterface
	private interface WxRunnable {
		void run() throws Exception;
	}

	public record TemplateAddRequest(String shortTemplateId, List<String> keywordNameList) {
	}

	public record WxUserOption(String openId, String nickName, String headimgUrl) {
	}

	public record QrcodeRequest(String scene, Boolean permanent, Integer expireSeconds) {
	}

	public record SessionRequest(String openId, String account) {
	}

	public record CustomerAccountRequest(String kfAccount, String nickName, String inviteWx) {
		WxMpKfAccountRequest toWxRequest() {
			String account = kfAccount == null ? "" : kfAccount.trim();
			String nickname = nickName == null ? "" : nickName.trim();
			if (!CUSTOMER_ACCOUNT_PATTERN.matcher(account).matches()) {
				throw new IllegalArgumentException(CUSTOMER_ACCOUNT_FORMAT_MESSAGE);
			}
			if (nickname.isEmpty()) {
				throw new IllegalArgumentException("客服昵称不能为空");
			}
			return WxMpKfAccountRequest.builder()
				.kfAccount(account)
				.nickName(nickname)
				.inviteWx(inviteWx == null || inviteWx.isBlank() ? null : inviteWx.trim())
				.build();
		}
	}

	public record CommentRequest(String msgDataId, Integer index, Long userCommentId, String content) {
	}
}
