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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpFreePublishService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信发布
 *
 * @author www.joolun.com
 * @date 2022-03-10 21:26:35
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/freepublish")
public class WxFreePublishController extends BaseController {
	private final WxMpService wxService;

	/**
	 * 删除发布
	 *
	 * @param
	 * @return R
	 */
	@DeleteMapping
	@PreAuthorize("@ss.hasPermi('wxmp:wxfreepublish:del')")
	public AjaxResult del(String id) throws Exception {
		WxMpFreePublishService wxMpFreePublishService = wxService.getFreePublishService();
		return AjaxResult.success(wxMpFreePublishService.deletePushAllArticle(id));
	}

	/**
	 * 获取成功发布列表
	 *
	 * @param page 获取成功发布列表
	 * @param
	 * @return
	 */
	@GetMapping("/page")
	@PreAuthorize("@ss.hasPermi('wxmp:wxfreepublish:index')")
	public AjaxResult getPage(Page page) throws Exception {
		WxMpFreePublishService wxMpFreePublishService = wxService.getFreePublishService();
		int count = (int) page.getSize();
		int offset = (int) page.getCurrent() * count - count;
		return AjaxResult.success(wxMpFreePublishService.getPublicationRecords(offset, count));
	}

}
