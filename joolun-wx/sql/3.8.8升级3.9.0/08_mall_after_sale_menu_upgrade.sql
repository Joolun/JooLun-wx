/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2063, '售后工作台', 2033, 50, 'aftersale', 'mall/aftersale/index', NULL, '', 1, 0, 'C', '0', '0', 'mall:aftersale:index', 'ticket', 'admin', NOW(), '', NULL, '商城售后工作台菜单'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2063);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2064, '售后查询', 2063, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:aftersale:index', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2064);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2065, '售后审核', 2063, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:aftersale:edit', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2065);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2063
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2063);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2064
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2064);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2065
WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2065);
