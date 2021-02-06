import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:web_monitor_app/modules/module_overview/consts/const_overview.dart';
import 'package:web_monitor_app/modules/module_overview/services/service_overview.dart';
import 'package:web_monitor_app/modules/module_overview/widgets/widget_overview_item.dart';
import 'package:web_monitor_app/utils/util_date_time.dart';

import 'models/model_overview_item.dart';

class ModuleOverview extends StatefulWidget {
  @override
  _ModuleOverviewState createState() => _ModuleOverviewState();
}

class _ModuleOverviewState extends State<ModuleOverview> {
  Timer _timer;
  String _lastUpdateTime = "";
  List<ModelOverviewListItem> _overviewList = [];

  @override
  void initState() {
    super.initState();
    startDataRefreshTimer();
    getAllRelatedProjectOverview();
  }

  @override
  void dispose() {
    if (_timer != null) {
      _timer.cancel();
    }
    super.dispose();
  }

  /// 开启刷新数据的定时器
  void startDataRefreshTimer() {
    const duration = Duration(seconds: ConstOverview.REFRESH_INTERVAL);
    setState(() {
      _timer =
          Timer.periodic(duration, (timer) => getAllRelatedProjectOverview());
    });
  }

  /// 获取用户关联的所有项目的统计情况列表
  Future getAllRelatedProjectOverview({bool showToast = false}) async {
    // 查询参数
    var endDateTime = DateTime.now().toLocal();
    var startDateTime = endDateTime.subtract(Duration(minutes: 1));
    String startTime =
        UtilDateTime.getDateStrByFormatAndDateTime(dateTime: startDateTime);
    String endTime =
        UtilDateTime.getDateStrByFormatAndDateTime(dateTime: endDateTime);

    var overviewList = await ServiceOverview.getAllRelatedProjectOverview(
        startTime: startTime, endTime: endTime);
    setState(() {
      _overviewList = overviewList;
      _lastUpdateTime = startTime;
    });
    if (showToast) {
      EasyLoading.showToast(
        "数据已更新",
        toastPosition: EasyLoadingToastPosition.bottom,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return RefreshIndicator(
      child: ListView(padding: EdgeInsets.symmetric(vertical: 8.0), children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              "数据更新于：$_lastUpdateTime",
              style: TextStyle(
                fontSize: 12.0,
                color: Colors.grey,
              ),
            ),
          ],
        ),
        ..._overviewList.map((e) => WidgetOverviewItem(item: e)).toList(),
      ]),
      onRefresh: () => getAllRelatedProjectOverview(showToast: true),
    );
  }
}
