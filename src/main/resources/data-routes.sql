INSERT IGNORE INTO tb_system_route(`id`, `parent_id`, `name`, `path`, `redirect`, `component`, `sort_id`, `login`, `permission`, `menu`, `label`, `icon_name`, `built_in`, `active`) VALUE
(1, 0, 'portal', '/portal', '/portal/login', '/src/views/portal/index.vue', 1, 0, null, 0, null, null, 1, 1),
(2, 1, 'login', '/portal/login', null, '/src/views/portal/login/index.vue', 2, 0, null, 0, null, null, 1, 1),
(3, 1, 'register', '/portal/register', null, '/src/views/portal/register/index.vue', 3, 0, null, 0, null, null, 1, 1),
(4, 1, 'reset_password', '/portal/reset-password', null, '/src/views/portal/reset_password/index.vue', 4, 0, null, 0, null, null, 1, 1);
