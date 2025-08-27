INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `sort_id`, `option_key`, `option_value`, `name`, `description`)
VALUES (1, 1, 1, 1, 0, 'console.openLog', 'false', '控制台日志打印开关', 'true/false，默认：false');

INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `sort_id`, `option_key`, `option_value`, `name`, `description`)
VALUES (1, 1, 1, 1, 0, 'request.timeout', '10000', '请求超时时长', '单位：毫秒，默认：1000');

INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `sort_id`, `option_key`, `option_value`, `name`, `description`)
VALUES (1, 1, 1, 1, 0, 'captcha.length', '5', '验证码长度', '推荐4～6位，默认：5');

INSERT IGNORE INTO tb_system_option(`active`, `built_in`, `editable`, `front_load`, `sort_id`, `option_key`, `option_value`, `name`, `description`)
VALUES (1, 1, 1, 0, 0, 'captcha.expireSeconds', '60', '验证码有效期', '单位：秒，默认：60');
