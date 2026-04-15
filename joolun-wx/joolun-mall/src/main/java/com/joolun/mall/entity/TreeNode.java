/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author www.joolun.com
 */
@Data
public class TreeNode {
	protected String id;
	protected String parentId;
	private Integer sort;
	protected List<TreeNode> children = new ArrayList<>();

	public void addChildren(TreeNode treeNode) {
		children.add(treeNode);
	}

	public List<TreeNode> getChildren() {
		if(children.size()<=0){
			return null;
		}
		return children;
	}
}
