import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_overview/services/service_overview.dart';

class ModuleOverview extends StatefulWidget {
  @override
  _ModuleOverviewState createState() => _ModuleOverviewState();
}

class _ModuleOverviewState extends State<ModuleOverview> {
  @override
  void initState() {
    super.initState();
    this.getAllRelatedProjectOverview();
  }

  /// 获取用户关联的所有项目的统计情况列表
  void getAllRelatedProjectOverview() async {
    var overviewList = await ServiceOverview.getAllRelatedProjectOverview();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Text("总览"),
    );
  }
}