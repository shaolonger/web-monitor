import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_about/module_about.dart';
import 'package:web_monitor_app/modules/module_login/module_login.dart';
import 'package:web_monitor_app/modules/module_home/module_home.dart';
import 'package:web_monitor_app/modules/module_overview/module_overview.dart';
import 'package:web_monitor_app/modules/module_project_manage/module_project_manage.dart';
import 'package:web_monitor_app/modules/module_search/module_search.dart';
import 'package:web_monitor_app/modules/module_mine/module_mine.dart';

export 'package:web_monitor_app/modules/module_main/module_main.dart';
export 'package:web_monitor_app/modules/module_startup/module_startup.dart';
export 'package:web_monitor_app/modules/module_home/module_home.dart';

// 登录页
const moduleLogin = "/login";
// 首页
const moduleHome = "/home";
// 总览页
const moduleOverview = "/overview";
// 查询页
const moduleSearch = "/search";
// 我的页
const moduleMine = "/mine";
// 项目管理页
const moduleProjectManage = "/projectManage";
// 关于页
const moduleAbout = "/about";

// 路由List
final routeList = [
  {"routeName": moduleLogin, "routeClass": () => ModuleLogin()},
  {"routeName": moduleHome, "routeClass": () => ModuleHome()},
  {"routeName": moduleOverview, "routeClass": () => ModuleOverview()},
  {"routeName": moduleSearch, "routeClass": () => ModuleSearch()},
  {"routeName": moduleMine, "routeClass": () => ModuleMine()},
  {"routeName": moduleProjectManage, "routeClass": () => ModuleProjectManage()},
  {"routeName": moduleAbout, "routeClass": () => ModuleAbout()},
];

// 路由Map
final Map<String, WidgetBuilder> routes = Map.fromIterable(routeList,
    key: (e) => e["routeName"], value: (e) => (context) => e["routeClass"]());
