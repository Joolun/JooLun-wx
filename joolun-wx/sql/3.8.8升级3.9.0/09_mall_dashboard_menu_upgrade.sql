/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2066, '数据看板', 2033, 5, 'dashboard', 'mall/dashboard/index', NULL, '', 1, 0, 'C', '0', '0', 'mall:dashboard:index', 'monitor', 'admin', NOW(), '', NULL, '商城数据看板菜单'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2066);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2067, '数据看板查询', 2066, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:dashboard:index', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2067);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2066
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2066);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2067
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2067);
