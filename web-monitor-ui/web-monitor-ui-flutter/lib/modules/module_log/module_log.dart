import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_cus_log.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_http_log.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_js_log.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_res_log.dart';

class ModuleLog extends StatefulWidget {
  @override
  _ModuleLogState createState() => _ModuleLogState();
}

class _ModuleLogState extends State<ModuleLog> with TickerProviderStateMixin {
  String _projectName;
  String _projectIdentifier;
  TabController _tabController;
  List _tabList = [];

  /// 初始化tab
  void _initTab() {
    setState(() {
      _tabList = [
        {
          "tabName": "JS",
          "tabContent": WidgetJsLog(
            projectName: _projectName,
            projectIdentifier: _projectIdentifier,
          ),
        },
        {
          "tabName": "HTTP",
          "tabContent": WidgetHttpLog(),
        },
        {
          "tabName": "RES",
          "tabContent": WidgetResLog(),
        },
        {
          "tabName": "CUS",
          "tabContent": WidgetCusLog(),
        },
      ];
    });
    _tabController = TabController(length: _tabList.length, vsync: this);
  }

  /// 获取路由传参
  void _getRouteParams(BuildContext context) {
    final Map<String, String> params =
        ModalRoute.of(context).settings.arguments;
    setState(() {
      _projectName = params["projectName"] ?? "";
      _projectIdentifier = params["_projectIdentifier"] ?? "";
    });
    _initTab();
  }

  @override
  Widget build(BuildContext context) {
    _getRouteParams(context);

    return Scaffold(
      appBar: AppBar(
        title: Text(_projectName),
        bottom: TabBar(
          isScrollable: true,
          controller: _tabController,
          tabs: _tabList.map((e) => Tab(text: e["tabName"])).toList(),
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: _tabList.map<Widget>((e) => e["tabContent"]).toList(),
      ),
    );
  }
}
