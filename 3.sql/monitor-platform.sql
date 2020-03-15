/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/3/15 12:18:19                           */
/*==============================================================*/


drop table if exists lms_custom_error_log;

drop table if exists lms_http_error_log;

drop table if exists lms_js_error_log;

drop table if exists lms_resource_load_error_log;

drop table if exists pms_project;

drop table if exists ums_user;

drop table if exists ums_user_project_relation;

drop table if exists ums_user_register_record;

/*==============================================================*/
/* Table: lms_custom_error_log                                  */
/*==============================================================*/
create table lms_custom_error_log
(
   id                   bigint not null comment 'ID',
   log_type             varchar(50) not null comment '日志类型',
   project_identifier   varchar(200) not null comment '关联的项目标识',
   create_time          datetime not null comment '创建时间',
   user_id              bigint not null comment '用户ID',
   user_name            varchar(64) default "" comment '用户名',
   page_url             text comment '页面URL',
   page_key             varchar(50) default "" comment '页面关键字',
   device_name          varchar(100) default "" comment '设备名',
   os                   varchar(20) default "" comment '操作系统',
   browser_name         varchar(20) default "" comment '浏览器名',
   browser_version      text comment '浏览器版本',
   ip_address           varchar(50) default "" comment 'IP地址',
   error_type           varchar(30) default "" comment '错误类型',
   error_message        text not null comment '错误信息',
   primary key (id)
);

alter table lms_custom_error_log comment '自定义异常日志表';

/*==============================================================*/
/* Table: lms_http_error_log                                    */
/*==============================================================*/
create table lms_http_error_log
(
   id                   bigint not null comment 'ID',
   log_type             varchar(50) not null comment '日志类型',
   project_identifier   varchar(200) not null comment '关联的项目标识',
   create_time          datetime not null comment '创建时间',
   user_id              bigint not null comment '用户ID',
   user_name            varchar(64) default "" comment '用户名',
   page_url             text comment '页面URL',
   page_key             varchar(50) default "" comment '页面关键字',
   device_name          varchar(100) default "" comment '设备名',
   os                   varchar(20) default "" comment '操作系统',
   browser_name         varchar(20) default "" comment '浏览器名',
   browser_version      text comment '浏览器版本',
   ip_address           varchar(50) default "" comment 'IP地址',
   http_type            varchar(20) default "" comment 'http请求类型，如"request"、"response"',
   http_url_complete    text not null comment '完整的http请求地址',
   http_url_short       text comment '缩写的http请求地址',
   status               varchar(20) not null comment '请求状态',
   status_text          varchar(50) default "" comment '请求状态文字描述',
   res_time             varchar(13) default "" comment '响应时间',
   primary key (id)
);

alter table lms_http_error_log comment 'HTTP异常日志表';

/*==============================================================*/
/* Table: lms_js_error_log                                      */
/*==============================================================*/
create table lms_js_error_log
(
   id                   bigint not null comment 'ID',
   log_type             varchar(50) not null comment '日志类型',
   project_identifier   varchar(200) not null comment '关联的项目标识',
   create_time          datetime not null comment '创建时间',
   user_id              bigint not null comment '关联的用户ID',
   user_name            varchar(64) default "" comment '用户名',
   page_url             text comment '页面URL',
   page_key             varchar(50) default "" comment '页面关键字',
   device_name          varchar(100) default "" comment '设备名',
   os                   varchar(20) default "" comment '操作系统',
   browser_name         varchar(20) default "" comment '浏览器名',
   browser_version      text comment '浏览器版本',
   ip_address           varchar(50) default "" comment 'IP地址',
   error_type           varchar(30) default "" comment '错误类型',
   error_message        text not null comment '错误信息',
   error_stack          text comment '错误堆栈信息',
   primary key (id)
);

alter table lms_js_error_log comment 'JS异常日志表';

/*==============================================================*/
/* Table: lms_resource_load_error_log                           */
/*==============================================================*/
create table lms_resource_load_error_log
(
   id                   bigint not null comment 'ID',
   log_type             varchar(50) not null comment '日志类型',
   project_identifier   varchar(200) not null comment '关联的项目标识',
   create_time          datetime not null comment '创建时间',
   user_id              bigint not null comment '用户ID',
   user_name            varchar(64) default "" comment '用户名',
   page_url             text comment '页面URL',
   page_key             varchar(50) default "" comment '页面关键字',
   device_name          varchar(100) default "" comment '设备名',
   os                   varchar(20) default "" comment '操作系统',
   browser_name         varchar(20) default "" comment '浏览器名',
   browser_version      text comment '浏览器版本',
   ip_address           varchar(50) default "" comment 'IP地址',
   resource_url         text not null comment '资源URL',
   resource_type        varchar(20) not null comment '资源类型',
   status               varchar(1) not null comment '加载状态',
   primary key (id)
);

alter table lms_resource_load_error_log comment '资源加载异常日志表';

/*==============================================================*/
/* Table: pms_project                                           */
/*==============================================================*/
create table pms_project
(
   id                   bigint not null comment 'ID',
   project_name         varchar(100) not null comment '项目名',
   project_identifier   varchar(200) not null comment '项目标识符',
   description          varchar(200) default "" comment '项目描述',
   create_time          datetime not null comment '创建时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   unique key AK_Key_2 (project_identifier),
   unique key AK_Key_3 (project_name)
);

alter table pms_project comment '项目表';

/*==============================================================*/
/* Table: ums_user                                              */
/*==============================================================*/
create table ums_user
(
   id                   bigint not null comment 'ID',
   username             varchar(64) not null comment '用户名',
   password             varchar(64) not null comment '密码',
   phone                varchar(64) default "" comment '电话',
   icon                 varchar(500) default "" comment '头像',
   gender               int(1) default 0 comment '性别，0-未知，1-男，2-女',
   email                varchar(100) not null comment '邮箱',
   create_time          datetime not null comment '创建时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   unique key AK_Key_2 (username)
);

alter table ums_user comment '用户表';

/*==============================================================*/
/* Table: ums_user_project_relation                             */
/*==============================================================*/
create table ums_user_project_relation
(
   id                   bigint not null comment 'ID',
   user_id              bigint not null comment '关联的用户ID',
   project_id           bigint not null comment '关联的项目ID',
   primary key (id)
);

alter table ums_user_project_relation comment '用户与项目的关系表';

/*==============================================================*/
/* Table: ums_user_register_record                              */
/*==============================================================*/
create table ums_user_register_record
(
   id                   bigint not null comment 'ID',
   username             varchar(64) not null comment '用户名',
   password             varchar(64) not null comment '密码',
   email                varchar(100) not null comment '邮箱',
   phone                varchar(64) default "" comment '电话',
   icon                 varchar(500) default "" comment '头像',
   gender               int(1) default 0 comment '性别，0-未知，1-男，2-女',
   create_time          datetime not null comment '创建时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   audit_user           bigint default 0 comment '审批人',
   audit_result         int(1) default -1 comment '审批结果，-1-未审核，0-不通过，1-通过',
   primary key (id),
   key AK_Key_2 (username)
);

alter table ums_user_register_record comment '用户表注册记录表';

alter table lms_custom_error_log add constraint FK_Reference_4 foreign key (project_identifier)
      references pms_project (project_identifier) on delete restrict on update restrict;

alter table lms_http_error_log add constraint FK_Reference_2 foreign key (project_identifier)
      references pms_project (project_identifier) on delete restrict on update restrict;

alter table lms_js_error_log add constraint FK_Reference_1 foreign key (project_identifier)
      references pms_project (project_identifier) on delete restrict on update restrict;

alter table lms_resource_load_error_log add constraint FK_Reference_3 foreign key (project_identifier)
      references pms_project (project_identifier) on delete restrict on update restrict;

alter table ums_user_project_relation add constraint FK_Reference_5 foreign key (user_id)
      references ums_user (id) on delete restrict on update restrict;

alter table ums_user_project_relation add constraint FK_Reference_6 foreign key (project_id)
      references pms_project (id) on delete restrict on update restrict;

