import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_log/consts/const_log.dart';
import 'package:web_monitor_app/modules/module_log/models/model_log_count_between_diff_date.dart';
import 'package:web_monitor_app/modules/module_log/models/model_log_overview.dart';
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
  ModelLogOverview _overview = ModelLogOverview();

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
    List<dynamic> result = model.jsErrorLog;
    _setOverviewData(result);
  }

  /// 设置总览数据
  void _setOverviewData(List<dynamic> result) {
    var overview = ModelLogOverview();
    if (result.length > 1) {
      var yesterday = result[0];
      var today = result[1];
      var affectUVPercentYesterday = yesterday["count"] == 0
          ? "-"
          : "${((yesterday["uv"] / yesterday["count"]) * 100).toFixed(2)}%";
      var affectUVPercentToday = today["count"] == 0
          ? "-"
          : "${((today["uv"] / today["count"]) * 100).toFixed(2)}%";
      var affectUVPercentChange = 0;
      var affectUVPercentRate = "-";
      if (affectUVPercentYesterday != "-" && affectUVPercentToday != "-") {
        var affectUVPercentYesterdayRate = yesterday["uv"] / yesterday["count"];
        var affectUVPercentTodayRate = today["uv"] / today["count"];
        affectUVPercentChange = affectUVPercentTodayRate !=
                affectUVPercentYesterdayRate
            ? (affectUVPercentTodayRate > affectUVPercentYesterdayRate ? 1 : -1)
            : 0;
        affectUVPercentRate = affectUVPercentYesterdayRate == 0
            ? '-'
            : "${(((affectUVPercentTodayRate - affectUVPercentYesterdayRate) / affectUVPercentYesterdayRate) * 100).toFixed(2)}%";
      }
      var countChange = today["count"] != yesterday["count"]
          ? (today["count"] > yesterday["count"] ? 1 : -1)
          : 0;
      var countRate = yesterday["count"] == 0
          ? "-"
          : "${(((today["count"] - yesterday["count"]) / yesterday["count"]) * 100).toFixed(2)}%";
      var affectUVChange = today["uv"] != yesterday["uv"]
          ? (today["uv"] > yesterday["uv"] ? 1 : -1)
          : 0;
      var affectUVRate = yesterday["uv"] == 0
          ? "-"
          : "${(((today["uv"] - yesterday["uv"]) / yesterday["uv"]) * 100).toFixed(2)}%";
      overview.count = ModelLogOverviewItem(
        yesterday: yesterday["count"],
        today: today["count"],
        change: countChange,
        rate: countRate,
      );
      overview.affectUV = ModelLogOverviewItem(
        yesterday: yesterday["uv"],
        today: today["uv"],
        change: affectUVChange,
        rate: affectUVRate,
      );
      overview.affectUVPercent = ModelLogOverviewItem(
        yesterday: affectUVPercentYesterday,
        today: affectUVPercentToday,
        change: affectUVPercentChange,
        rate: affectUVPercentRate,
      );
    }
    setState(() {
      _overview = overview;
    });
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
        WidgetLogOverview(overview: _overview),
      ],
    );
  }
}