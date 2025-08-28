INSERT IGNORE INTO tb_system_api(`id`, `sort_id`, `active`, `built_in`, `call_path`, `request_method`, `url`, `description`, `request_spec`, `response_spec`)
VALUES (1001, 1001, 1, 1, 'admax.system.api.getApis', 'get', '/api/admax/system/api/getApis', '获取系统接口', null, '[\n  {\n    callPath: \'前端开发调用路径\',\n    requestMethod: \'请求方法 get/post\',\n    url: \'请求URL\',\n    description: \'接口说明\',\n  }\n]');

INSERT IGNORE INTO tb_system_api(`id`, `sort_id`, `active`, `built_in`, `call_path`, `request_method`, `url`, `description`, `request_spec`, `response_spec`)
VALUES (2001, 2001, 1, 1, 'admax.system.option.getOptions', 'get', '/api/admax/system/option/getOptions', '获取（允许前端访问的）系统参数', null, '[\n  {\n    optionKey: \'参数Key\',\n    optionValue: \'参数值\',\n  }\n]');
