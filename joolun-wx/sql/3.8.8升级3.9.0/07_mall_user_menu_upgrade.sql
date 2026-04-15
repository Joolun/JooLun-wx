/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2060, '商城用户', 2033, 40, 'malluser', 'mall/malluser/index', NULL, '', 1, 0, 'C', '0', '0', 'mall:malluser:index', 'peoples', 'admin', NOW(), '', NULL, '商城用户菜单'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2060);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2061, '商城用户查询', 2060, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:malluser:get', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2061);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2062, '商城用户修改', 2060, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:malluser:edit', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2062);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2060
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2060);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2061
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2061);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2062
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2062);
