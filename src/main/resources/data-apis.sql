INSERT IGNORE INTO tb_system_api(`id`, `sort_id`, `active`, `built_in`, `call_path`, `request_method`, `url`, `description`, `request_spec`, `response_spec`)
VALUES (1001, 1001, 1, 1, 'admax.system.api.getApis', 'get', '/api/admax/system/api/getApis', '获取系统接口', null, '[\n  {\n    callPath: \'前端开发调用路径\',\n    requestMethod: \'请求方法 get/post\',\n    url: \'请求URL\',\n    description: \'接口说明\',\n  }\n]');

INSERT IGNORE INTO tb_system_api(`id`, `sort_id`, `active`, `built_in`, `call_path`, `request_method`, `url`, `description`, `request_spec`, `response_spec`)
VALUES (1002, 1002, 1, 1, 'admax.system.api.updateApisCache', 'get', '/api/admax/system/api/updateApisCache', '更新系统接口缓存', null, null);

INSERT IGNORE INTO tb_system_api(`id`, `sort_id`, `active`, `built_in`, `call_path`, `request_method`, `url`, `description`, `request_spec`, `response_spec`)
VALUES (2001, 2001, 1, 1, 'admax.system.option.getOptions', 'get', '/api/admax/system/option/getOptions', '获取（允许前端访问的）系统参数', null, '[\n  {\n    optionKey: \'参数Key\',\n    optionValue: \'参数值\',\n  }\n]');

INSERT IGNORE INTO tb_system_api(`id`, `sort_id`, `active`, `built_in`, `call_path`, `request_method`, `url`, `description`, `request_spec`, `response_spec`)
VALUES (2002, 2002, 1, 1, 'admax.system.option.updateFrontLoadOptionsCache', 'get', '/api/admax/system/option/updateFrontLoadOptionsCache', '更新（允许前端加载的）系统参数缓存', null, null);

INSERT IGNORE INTO tb_system_api(`id`, `sort_id`, `active`, `built_in`, `call_path`, `request_method`, `url`, `description`, `request_spec`, `response_spec`)
VALUES (2003, 2003, 1, 1, 'admax.system.option.updateCertainOptionCache', 'get', '/api/admax/system/option/updateCertainOptionCache', '更新某个系统参数缓存', null, null);

INSERT IGNORE INTO tb_system_api(`id`, `sort_id`, `active`, `built_in`, `call_path`, `request_method`, `url`, `description`, `request_spec`, `response_spec`)
VALUES (2004, 2004, 1, 1, 'admax.system.option.removeCertainOptionCache', 'get', '/api/admax/system/option/removeCertainOptionCache', '删除某个系统参数缓存', null, null);
