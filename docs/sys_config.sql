INSERT INTO `yqm-shop`.sys_config (id, config_name, config_value, config_desc, create_by, create_time, updated_by,
                                   updated_time)
VALUES ('1', 'upload', 'qiniu-upload', '上传配置', '1', '2022-04-24 10:47:43', '1', '2022-04-24 10:47:43');
INSERT INTO `yqm-shop`.sys_config (id, config_name, config_value, config_desc, create_by, create_time, updated_by,
                                   updated_time)
VALUES ('2', 'qiniu-upload', '
{

	"accessKey":"",
	"secretKey":"",
	"bucket":"",
	"host":""
}', '七牛云', '1', '2022-04-24 10:48:09', '1', '2022-04-24 10:48:09');
INSERT INTO `yqm-shop`.sys_config (id, config_name, config_value, config_desc, create_by, create_time, updated_by,
                                   updated_time)
VALUES ('3', 'sys_phone', '15871301059', '联系方式', '1', '2022-04-24 10:50:58', '1', '2022-04-24 10:50:58');
INSERT INTO `yqm-shop`.sys_config (id, config_name, config_value, config_desc, create_by, create_time, updated_by,
                                   updated_time)
VALUES ('4', 'order-overtime-close', '30', '正常订单超时，未付款，订单自动关闭', '1', '2022-04-24 14:41:47', '1', '2022-04-24 14:45:12');
INSERT INTO `yqm-shop`.sys_config (id, config_name, config_value, config_desc, create_by, create_time, updated_by,
                                   updated_time)
VALUES ('5', 'order-delivery-overtime-completed', '7', '发货超时，未收货，订单自动完成', '1', '2022-04-24 14:42:18', '1',
        '2022-04-24 14:45:12');
INSERT INTO `yqm-shop`.sys_config (id, config_name, config_value, config_desc, create_by, create_time, updated_by,
                                   updated_time)
VALUES ('6', 'order-completed-overtime-refund', '14', '订单完成超时，自动结束交易，不能申请售后', '1', '2022-04-24 14:42:46', '1',
        '2022-04-24 14:45:12');
INSERT INTO `yqm-shop`.sys_config (id, config_name, config_value, config_desc, create_by, create_time, updated_by,
                                   updated_time)
VALUES ('7', 'order-completed-overtime-evaluation', '7', '订单完成超时，自动五星好评', '1', '2022-04-24 14:43:15', '1',
        '2022-04-24 14:45:12');
