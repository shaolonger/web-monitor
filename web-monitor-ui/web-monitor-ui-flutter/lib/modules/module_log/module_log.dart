import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_log/screens/screen_log_js.dart';

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
          "tabContent": ScreenLogJs(
            projectName: _projectName,
            projectIdentifier: _projectIdentifier,
          ),
        },
        {
          "tabName": "HTTP",
          "tabContent": ScreenLogJs(
            projectName: _projectName,
            projectIdentifier: _projectIdentifier,
          ),
        },
        {
          "tabName": "RES",
          "tabContent": ScreenLogJs(
            projectName: _projectName,
            projectIdentifier: _projectIdentifier,
          ),
        },
        {
          "tabName": "CUS",
          "tabContent": ScreenLogJs(
            projectName: _projectName,
            projectIdentifier: _projectIdentifier,
          ),
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
