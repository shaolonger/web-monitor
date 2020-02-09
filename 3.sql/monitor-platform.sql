/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/2/9 18:28:45                            */
/*==============================================================*/


drop table if exists lms_custom_error_log;

drop table if exists lms_http_error_log;

drop table if exists lms_js_error_log;

drop table if exists lms_resource_load_error_log;

drop table if exists pms_project;

drop table if exists ums_user;

drop table if exists ums_user_project_relation;

/*==============================================================*/
/* Table: lms_custom_error_log                                  */
/*==============================================================*/
create table lms_custom_error_log
(
   id                   bigint not null,
   log_type             varchar(50) not null,
   project_id           bigint not null,
   create_time          datetime not null,
   user_id              bigint not null,
   user_name            varchar(64),
   page_url             text,
   page_key             varchar(50),
   device_name          varchar(100),
   os                   varchar(20),
   browser_name         varchar(20),
   browser_version      text,
   ip_address           varchar(50),
   error_type           varchar(30) not null,
   error_message        text not null,
   primary key (id)
);

alter table lms_custom_error_log comment '自定义异常日志表';

/*==============================================================*/
/* Table: lms_http_error_log                                    */
/*==============================================================*/
create table lms_http_error_log
(
   id                   bigint not null comment '主键',
   log_type             varchar(50) not null comment '日志类型',
   project_id           bigint not null,
   create_time          datetime not null,
   user_id              bigint not null,
   user_name            varchar(64),
   page_url             text,
   page_key             varchar(50),
   device_name          varchar(100),
   os                   varchar(20),
   browser_name         varchar(20),
   browser_version      text,
   ip_address           varchar(50),
   http_type            int(1) comment 'http请求类型，如"request"、"response"',
   http_url_complete    text not null comment '完整的http请求地址',
   http_url_short       text not null,
   status               varchar(20) not null,
   status_text          varchar(50),
   res_time             varchar(13),
   primary key (id)
);

alter table lms_http_error_log comment 'HTTP异常日志表';

/*==============================================================*/
/* Table: lms_js_error_log                                      */
/*==============================================================*/
create table lms_js_error_log
(
   id                   bigint not null,
   log_type             varchar(50) not null,
   project_id           bigint not null,
   create_time          datetime not null,
   user_id              bigint not null,
   user_name            varchar(64),
   page_url             text,
   page_key             varchar(50),
   device_name          varchar(100),
   os                   varchar(20),
   browser_name         varchar(20),
   browser_version      text,
   ip_address           varchar(50),
   error_type           varchar(30) not null,
   error_message        text not null,
   error_stack          text,
   primary key (id)
);

alter table lms_js_error_log comment 'JS异常日志表';

/*==============================================================*/
/* Table: lms_resource_load_error_log                           */
/*==============================================================*/
create table lms_resource_load_error_log
(
   id                   bigint not null comment '主键',
   log_type             varchar(50) not null comment '日志类型',
   project_id           bigint not null,
   create_time          datetime not null,
   user_id              bigint not null,
   user_name            varchar(64),
   page_url             text,
   page_key             varchar(50),
   device_name          varchar(100),
   os                   varchar(20),
   browser_name         varchar(20),
   browser_version      text,
   ip_address           varchar(50),
   resource_url         text not null,
   resource_type        varchar(20) not null,
   status               int(1) not null,
   primary key (id)
);

alter table lms_resource_load_error_log comment '资源加载异常日志表';

/*==============================================================*/
/* Table: pms_project                                           */
/*==============================================================*/
create table pms_project
(
   id                   bigint not null,
   project_name         varchar(100) not null,
   description          varchar(200),
   create_time          datetime,
   update_time          datetime,
   primary key (id)
);

alter table pms_project comment '项目表';

/*==============================================================*/
/* Table: ums_user                                              */
/*==============================================================*/
create table ums_user
(
   id                   bigint not null,
   username             varchar(64) not null,
   password             varchar(64) not null,
   phone                varchar(64),
   icon                 varchar(500),
   gender               int(1),
   email                varchar(100),
   create_time          datetime,
   update_time          datetime,
   primary key (id)
);

alter table ums_user comment '用户表';

/*==============================================================*/
/* Table: ums_user_project_relation                             */
/*==============================================================*/
create table ums_user_project_relation
(
   id                   bigint not null,
   user_id              bigint not null,
   project_id           bigint not null,
   primary key (id)
);

alter table ums_user_project_relation comment '用户与项目的关系表';

alter table lms_custom_error_log add constraint FK_Reference_4 foreign key (project_id)
      references pms_project (id) on delete restrict on update restrict;

alter table lms_http_error_log add constraint FK_Reference_2 foreign key (project_id)
      references pms_project (id) on delete restrict on update restrict;

alter table lms_js_error_log add constraint FK_Reference_1 foreign key (project_id)
      references pms_project (id) on delete restrict on update restrict;

alter table lms_resource_load_error_log add constraint FK_Reference_3 foreign key (project_id)
      references pms_project (id) on delete restrict on update restrict;

alter table ums_user_project_relation add constraint FK_Reference_5 foreign key (user_id)
      references ums_user (id) on delete restrict on update restrict;

alter table ums_user_project_relation add constraint FK_Reference_6 foreign key (project_id)
      references pms_project (id) on delete restrict on update restrict;

