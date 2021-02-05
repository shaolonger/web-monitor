import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_overview/services/service_overview.dart';
import 'package:web_monitor_app/modules/module_overview/widgets/widget_overview_item.dart';

import 'models/model_overview_item.dart';

class ModuleOverview extends StatefulWidget {
  @override
  _ModuleOverviewState createState() => _ModuleOverviewState();
}

class _ModuleOverviewState extends State<ModuleOverview> {
  List<ModelOverviewListItem> _overviewList = [];

  @override
  void initState() {
    super.initState();
    this.getAllRelatedProjectOverview();
  }

  /// 获取用户关联的所有项目的统计情况列表
  void getAllRelatedProjectOverview() async {
    var overviewList = await ServiceOverview.getAllRelatedProjectOverview();
    setState(() {
      _overviewList = overviewList;
    });
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
      children: _overviewList.map((e) => WidgetOverviewItem(item: e)).toList(),
    );
  }
}
