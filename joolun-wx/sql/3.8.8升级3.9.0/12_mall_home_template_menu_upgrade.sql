/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2068, '首页装修', 2033, 6, 'hometemplate', 'mall/hometemplate/index', NULL, '', 1, 0, 'C', '0', '0', 'mall:hometemplate:index', 'picture', 'admin', NOW(), '', NULL, '商城首页装修菜单'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2068);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2069, '首页装修查询', 2068, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:hometemplate:index', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2069);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2070, '首页装修保存', 2068, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:hometemplate:edit', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2070);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2068
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2068);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2069
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2069);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2070
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2070);
