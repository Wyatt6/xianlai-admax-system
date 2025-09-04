INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `back_load`, `sort_id`, `option_key`, `option_value`, `js_type`, `name`, `description`) VALUE
(1, 1, 1, 1, 0, 0, 'console.openLog', 'false', 'Boolean',  '控制台日志打印开关', 'true/false，默认：false');

INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `back_load`, `sort_id`, `option_key`, `option_value`, `js_type`, `name`, `description`) VALUE
(1, 1, 1, 1, 0, 0, 'request.timeout', '10000', 'Number', '请求超时时长', '单位：毫秒，默认：10000');

INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `back_load`, `sort_id`, `option_key`, `option_value`, `js_type`, `name`, `description`) VALUE
(1, 1, 1, 1, 1, 0, 'system.name', '闲来 Admax', 'String', '系统名称', '默认：闲来 Admax');

INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `back_load`, `sort_id`, `option_key`, `option_value`, `js_type`, `name`, `description`) VALUE
(1, 1, 1, 1, 0, 0, 'portal.coverImageType', 'local', 'String', '门户封面图片类型', '默认：local'),
(1, 1, 1, 1, 0, 0, 'portal.coverImagePath', '/src/assets/images/portal/default-cover.jpg', 'String', '门户封面图片路径', '默认：/src/assets/images/portal/default-cover.jpg'),
(1, 1, 1, 1, 0, 0, 'portal.coverTitle', '封面标题', 'String', '门户封面标题', null),
(1, 1, 1, 1, 0, 0, 'portal.coverTitleSize', '5', 'Number', '门户封面标题字体大小', '默认：5'),
(1, 1, 1, 1, 0, 0, 'portal.coverTitleColor', '#ffffff', 'String', '门户封面标题字体颜色', '默认：#ffffff'),
(1, 1, 1, 1, 0, 0, 'portal.coverSubTitle', '这里是封面副标题，可以写点简单的介绍', 'String', '门户封面副标题', null),
(1, 1, 1, 1, 0, 0, 'portal.coverSubTitleSize', '2', 'Number', '门户封面副标题字体大小', '默认：2'),
(1, 1, 1, 1, 0, 0, 'portal.coverSubTitleColor', '#ffffff', 'String', '门户封面副标题字体颜色', '默认：#ffffff'),
(1, 1, 1, 1, 0, 0, 'portal.footerCopyright', 'Copyright © 2025-2025 XianLai.fun 闲来. All rights reserved.', 'String', '门户页脚版权提示', null),
(1, 1, 1, 1, 0, 0, 'portal.footerBeianIcp', '粤ICP备XXXXXXXX号', 'String', '门户页脚工信部ICP备案号', '如：粤ICP备XXXXXXXX号'),
(1, 1, 1, 1, 0, 0, 'portal.footerBeianGongan', '粤公网安备XXXXXXXX号', 'String', '门户页脚公安部互联网安全平台备案号', '如：粤公网安备XXXXXXXX号');

INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `back_load`, `sort_id`, `option_key`, `option_value`, `js_type`, `name`, `description`) VALUE
(1, 1, 1, 1, 1, 0, 'captcha.length', '5', 'Number', '验证码长度', '推荐4～6位，默认：5'),
(1, 1, 1, 0, 1, 0, 'captcha.expireSeconds', '60', null, '验证码有效期', '单位：秒，默认：60');

INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `back_load`, `sort_id`, `option_key`, `option_value`, `js_type`, `name`, `description`) VALUE
(1, 1, 1, 1, 1, 0, "user.username.minLen", '5', 'Number', '用户名最小长度', '默认：5'),
(1, 1, 1, 1, 1, 0, "user.username.maxLen", '20', 'Number', '用户名最大长度', '默认：20'),
(1, 1, 1, 1, 1, 0, 'user.username.regexp', '^[a-zA-Z][a-zA-Z_0-9]{4,19}$', 'String', '用户名正则表达式', '默认：^[a-zA-Z][a-zA-Z_0-9]{4,19}$'),
(1, 1, 1, 1, 1, 0, 'user.username.tips', '仅限大写、小写字母，数字，下划线(_)，必须以字母开头', 'String', '用户名格式提示', '默认：仅限大写、小写字母，数字，下划线(_)，必须以字母开头'),
(1, 1, 1, 1, 1, 0, 'user.password.minLen', '6', 'Number', '密码最小长度', '默认：6'),
(1, 1, 1, 1, 1, 0, 'user.password.maxLen', '30', 'Number', '密码最大长度', '默认：30'),
(1, 1, 1, 1, 1, 0, 'user.password.regexp', '^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$', 'String', '密码正则表达式', '默认：^[a-zA-Z_0-9.~!@#$%^&*?]{6,30}$'),
(1, 1, 1, 1, 1, 0, 'user.password.tips', '仅限大写、小写字母，数字，下划线(_)，特殊字符(.~!@#$%^&*?)', 'String', '密码格式提示', '默认：仅限大写、小写字母，数字，下划线(_)，特殊字符(.~!@#$%^&*?)');
