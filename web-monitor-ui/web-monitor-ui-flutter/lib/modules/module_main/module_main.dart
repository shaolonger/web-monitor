import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_startup/module_startup.dart';
import 'package:web_monitor_app/routes/routes.dart';

class ModuleMain extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'web monitor',
      // 路由表定义
      routes: routes,
      home: ModuleStartup(),
    );
  }
}