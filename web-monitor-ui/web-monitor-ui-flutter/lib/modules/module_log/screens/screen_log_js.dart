import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_log/consts/const_log.dart';
import 'package:web_monitor_app/modules/module_log/models/model_log_count_between_diff_date.dart';
import 'package:web_monitor_app/modules/module_log/services/service_log.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_overview.dart';
import 'package:web_monitor_app/utils/util_date_time.dart';

class ScreenLogJs extends StatefulWidget {
  final String projectIdentifier;
  final String startTime;
  final String endTime;
  final int timeInterval;

  const ScreenLogJs({
    Key key,
    @required this.projectIdentifier,
    @required this.startTime,
    @required this.endTime,
    @required this.timeInterval,
  }) : super(key: key);

  @override
  _ScreenLogJsState createState() => _ScreenLogJsState();
}

class _ScreenLogJsState extends State<ScreenLogJs> {
  /// 获取总览数据
  void _getOverviewData() async {
    var nowDate = DateTime.now().toLocal();
    var startDate = nowDate.subtract(Duration(days: 1));
    String startTime = UtilDateTime.getDateStrByFormatAndDateTime(
            dateTime: startDate, pattern: "yyyy-MM-dd") +
        " 00:00:00";
    ModelLogCountBetweenDiffDate model =
        await ServiceLog.getLogCountBetweenDiffDate(
      projectIdentifier: widget.projectIdentifier,
      logTypeList: ConstLog.LOG_TYPE_LIST_MAP["JS"],
      indicatorList: "count,uv",
      startTime: startTime,
      endTime: widget.endTime,
      timeInterval: 86400,
    );
    print(model.toString());
  }

  /// 获取详细数据
  void _getDetailData() async {
    ModelLogCountBetweenDiffDate model =
        await ServiceLog.getLogCountBetweenDiffDate(
      projectIdentifier: widget.projectIdentifier,
      logTypeList: ConstLog.LOG_TYPE_LIST_MAP["JS"],
      indicatorList: "count",
      startTime: widget.startTime,
      endTime: widget.endTime,
      timeInterval: widget.timeInterval,
    );
  }

  @override
  void initState() {
    super.initState();
    _getOverviewData();
    _getDetailData();
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: EdgeInsets.all(20.0),
      children: [
        Text(widget.projectIdentifier),
        Text(widget.startTime),
        Text(widget.endTime),
        Text(widget.timeInterval.toString()),
        WidgetLogOverview(),
      ],
    );
  }
}
