/*
MIT License

Copyright (c) 2020 www.joolun.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.joolun.web.controller.weixin;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpDraftService;
import me.chanjar.weixin.mp.api.WxMpFreePublishService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.draft.WxMpAddDraft;
import me.chanjar.weixin.mp.bean.draft.WxMpDraftArticles;
import me.chanjar.weixin.mp.bean.draft.WxMpUpdateDraft;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信草稿箱
 *
 * @author www.joolun.com
 * @date 2022-03-10 21:26:35
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wxdraft")
public class WxDraftController extends BaseController {
	private final WxMpService wxService;

	/**
	 * 新增图文消息
	 *
	 * @param data
	 * @return
	 */
	@PostMapping
	@PreAuthorize("@ss.hasPermi('wxmp:wxdraft:add')")
	public AjaxResult add(@RequestBody JSONObject data) throws Exception {
		JSONArray jSONArray = data.getJSONArray("articles");
		List<WxMpDraftArticles> articles = jSONArray.toList(WxMpDraftArticles.class);
		WxMpAddDraft wxMpAddDraft = new WxMpAddDraft();
		wxMpAddDraft.setArticles(articles);
		WxMpDraftService wxMpDraftService = wxService.getDraftService();
		String rs = wxMpDraftService.addDraft(wxMpAddDraft);
		return AjaxResult.success(rs);
	}

	/**
	 * 修改微信草稿箱
	 *
	 * @param data
	 * @return
	 */
	@PutMapping
	@PreAuthorize("@ss.hasPermi('wxmp:wxdraft:edit')")
	public AjaxResult edit(@RequestBody JSONObject data) throws Exception {
		String mediaId = data.getStr("mediaId");
		JSONArray jSONArray = data.getJSONArray("articles");
		List<WxMpDraftArticles> articles = jSONArray.toList(WxMpDraftArticles.class);
		WxMpDraftService wxMpDraftService = wxService.getDraftService();
		WxMpUpdateDraft wxMpUpdateDraft = new WxMpUpdateDraft();
		wxMpUpdateDraft.setMediaId(mediaId);
		int index = 0;
		for (WxMpDraftArticles article : articles) {
			wxMpUpdateDraft.setIndex(index);
			wxMpUpdateDraft.setArticles(article);
			wxMpDraftService.updateDraft(wxMpUpdateDraft);
			index++;
		}
		return AjaxResult.success();
	}

	/**
	 * 通过id删除微信草稿箱
	 *
	 * @param
	 * @return R
	 */
	@DeleteMapping
	@PreAuthorize("@ss.hasPermi('wxmp:wxdraft:del')")
	public AjaxResult del(String id) throws Exception {
		WxMpDraftService wxMpDraftService = wxService.getDraftService();
		return AjaxResult.success(wxMpDraftService.delDraft(id));
	}

	/**
	 * 分页查询
	 *
	 * @param page 分页对象
	 * @param
	 * @return
	 */
	@GetMapping("/page")
	@PreAuthorize("@ss.hasPermi('wxmp:wxdraft:index')")
	public AjaxResult getPage(Page page) throws Exception {
		WxMpDraftService wxMpDraftService = wxService.getDraftService();
		int count = (int) page.getSize();
		int offset = (int) page.getCurrent() * count - count;
		return AjaxResult.success(wxMpDraftService.listDraft(offset, count));
	}

	/**
	 * 发布草稿箱
	 * @param id
	 * @return
	 */
	@PostMapping("/publish/{id}")
	@PreAuthorize("@ss.hasPermi('wxmp:wxdraft:publish')")
	public AjaxResult publish(@PathVariable String id) throws Exception {
		WxMpFreePublishService wxMpFreePublishService = wxService.getFreePublishService();
		wxMpFreePublishService.submit(id);
		return AjaxResult.success();
	}
}
