/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.entity;

import lombok.Data;

/**
 * imageManager
 * @author www.joolun.com
 * @date 2019-03-23 21:26:35
 */
@Data
public class ImageManager{
private static final long serialVersionUID = 1L;

    private String url;
    private String thumb;
    private String tag;
    private String name;
	private Integer id;
}
