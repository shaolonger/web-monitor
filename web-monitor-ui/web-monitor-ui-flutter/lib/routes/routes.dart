import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_login/module_login.dart';
import 'package:web_monitor_app/modules/module_overview/module_overview.dart';

export 'package:web_monitor_app/modules/module_main/module_main.dart';
export 'package:web_monitor_app/modules/module_startup/module_startup.dart';

// 登录页
const moduleLogin = "/login";
// 总览页(首页)
const moduleOverview = "/overview";

// 路由List
final routeList = [
  {"routeName": moduleLogin, "routeClass": () => ModuleLogin()},
  {"routeName": moduleOverview, "routeClass": () => ModuleOverview()},
];

// 路由Map
final Map<String, WidgetBuilder> routes = Map.fromIterable(routeList,
    key: (e) => e["routeName"], value: (e) => (context) => e["routeClass"]());
