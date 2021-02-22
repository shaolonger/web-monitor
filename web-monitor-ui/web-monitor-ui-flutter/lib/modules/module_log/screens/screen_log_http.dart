import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:web_monitor_app/modules/module_log/consts/const_log.dart';
import 'package:web_monitor_app/modules/module_log/models/model_log_count_between_diff_date.dart';
import 'package:web_monitor_app/modules/module_log/models/model_log_overview.dart';
import 'package:web_monitor_app/modules/module_log/services/service_log.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_chart_browser_name.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_chart_device_name.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_chart_net_type.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_chart_os.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_chart_overview.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_chart_status.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_overview.dart';
import 'package:web_monitor_app/utils/util_date_time.dart';

class ScreenLogHttp extends StatefulWidget {
  final String projectIdentifier;
  final String startTime;
  final String endTime;
  final int timeInterval;

  ScreenLogHttp({
    Key key,
    @required this.projectIdentifier,
    @required this.startTime,
    @required this.endTime,
    @required this.timeInterval,
  }) : super(key: key);

  @override
  _ScreenLogHttpState createState() => _ScreenLogHttpState();
}

class _ScreenLogHttpState extends State<ScreenLogHttp> {
  final String _logType = ConstLog.LOG_TYPE_LIST_MAP["HTTP"];
  ModelLogOverview _overview = ModelLogOverview();
  List<dynamic> _chartOverviewData = [];
  List<dynamic> _chartDeviceNameData = [];
  List<dynamic> _chartOsData = [];
  List<dynamic> _chartBrowserNameData = [];
  List<dynamic> _chartNetTypeData = [];
  List<dynamic> _chartStatusData = [];

  /// 获取总览数据
  void _getOverviewData() async {
    var nowDate = DateTime.now().toLocal();
    var startDate = nowDate.subtract(Duration(days: 1));
    var endDate = nowDate.add(Duration(days: 1));
    String startTime = UtilDateTime.getDateStrByFormatAndDateTime(
            dateTime: startDate, pattern: "yyyy-MM-dd") +
        " 00:00:00";
    String endTime = UtilDateTime.getDateStrByFormatAndDateTime(
            dateTime: endDate, pattern: "yyyy-MM-dd") +
        " 00:00:00";
    ModelLogCountBetweenDiffDate model =
        await ServiceLog.getLogCountBetweenDiffDate(
      projectIdentifier: widget.projectIdentifier,
      logTypeList: _logType,
      indicatorList: "count,uv",
      startTime: startTime,
      endTime: endTime,
      timeInterval: 86400,
    );
    List<dynamic> result = model.httpErrorLog;
    EasyLoading.show(status: "数据处理中，请稍候", maskType: EasyLoadingMaskType.black);
    _setOverviewData(result);
    EasyLoading.dismiss();
  }

  /// 设置总览数据
  void _setOverviewData(List<dynamic> result) {
    var overview = ModelLogOverview();
    if (result.length > 1) {
      var yesterday = result[0];
      var today = result[1];
      var affectUVPercentYesterday = yesterday["count"] == 0
          ? "-"
          : "${((yesterday["uv"] / yesterday["count"]) * 100).toStringAsFixed(2)}%";
      var affectUVPercentToday = today["count"] == 0
          ? "-"
          : "${((today["uv"] / today["count"]) * 100).toStringAsFixed(2)}%";
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
            : "${(((affectUVPercentTodayRate - affectUVPercentYesterdayRate) / affectUVPercentYesterdayRate) * 100).toStringAsFixed(2)}%";
      }
      var countChange = today["count"] != yesterday["count"]
          ? (today["count"] > yesterday["count"] ? 1 : -1)
          : 0;
      var countRate = yesterday["count"] == 0
          ? "-"
          : "${(((today["count"] - yesterday["count"]) / yesterday["count"]) * 100).toStringAsFixed(2)}%";
      var affectUVChange = today["uv"] != yesterday["uv"]
          ? (today["uv"] > yesterday["uv"] ? 1 : -1)
          : 0;
      var affectUVRate = yesterday["uv"] == 0
          ? "-"
          : "${(((today["uv"] - yesterday["uv"]) / yesterday["uv"]) * 100).toStringAsFixed(2)}%";
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

  /// 获取折线图，日志数-时间表数据
  void _getChartOverviewData() async {
    ModelLogCountBetweenDiffDate model =
        await ServiceLog.getLogCountBetweenDiffDate(
      projectIdentifier: widget.projectIdentifier,
      logTypeList: _logType,
      indicatorList: "count",
      startTime: widget.startTime,
      endTime: widget.endTime,
      timeInterval: widget.timeInterval,
    );
    List<dynamic> chartData = model.httpErrorLog;
    setState(() {
      _chartOverviewData = chartData;
    });
  }

  /// 获取环形图，设备类型数据
  void _geChartDeviceNameData() async {
    List<dynamic> resultList =
        await ServiceLog.getLogDistributionBetweenDiffDate(
      projectIdentifier: widget.projectIdentifier,
      logType: _logType,
      indicator: ConstLog.INDICATOR_LIST_MAP["DEVICE_NAME"],
      startTime: widget.startTime,
      endTime: widget.endTime,
    );
    setState(() {
      _chartDeviceNameData = resultList;
    });
  }

  /// 获取环形图，操作系统数据
  void _geChartOsData() async {
    List<dynamic> resultList =
        await ServiceLog.getLogDistributionBetweenDiffDate(
      projectIdentifier: widget.projectIdentifier,
      logType: _logType,
      indicator: ConstLog.INDICATOR_LIST_MAP["OS"],
      startTime: widget.startTime,
      endTime: widget.endTime,
    );
    setState(() {
      _chartOsData = resultList;
    });
  }

  /// 获取环形图，浏览器数据
  void _geChartBrowserNameData() async {
    List<dynamic> resultList =
        await ServiceLog.getLogDistributionBetweenDiffDate(
      projectIdentifier: widget.projectIdentifier,
      logType: _logType,
      indicator: ConstLog.INDICATOR_LIST_MAP["BROWSER_NAME"],
      startTime: widget.startTime,
      endTime: widget.endTime,
    );
    setState(() {
      _chartBrowserNameData = resultList;
    });
  }

  /// 获取环形图，网络类型数据
  void _geChartNetTypeData() async {
    List<dynamic> resultList =
        await ServiceLog.getLogDistributionBetweenDiffDate(
      projectIdentifier: widget.projectIdentifier,
      logType: _logType,
      indicator: ConstLog.INDICATOR_LIST_MAP["NET_TYPE"],
      startTime: widget.startTime,
      endTime: widget.endTime,
    );
    setState(() {
      _chartNetTypeData = resultList;
    });
  }

  /// 获取环形图，状态码数据
  void _geChartStatusData() async {
    List<dynamic> resultList =
        await ServiceLog.getLogDistributionBetweenDiffDate(
      projectIdentifier: widget.projectIdentifier,
      logType: _logType,
      indicator: ConstLog.INDICATOR_LIST_MAP["STATUS"],
      startTime: widget.startTime,
      endTime: widget.endTime,
    );
    setState(() {
      _chartStatusData = resultList;
    });
  }

  /// 获取页面所需的数据
  void _getPageData() {
    if (widget.projectIdentifier.isNotEmpty) {
      _getOverviewData();
      _getChartOverviewData();
      _geChartDeviceNameData();
      _geChartOsData();
      _geChartBrowserNameData();
      _geChartNetTypeData();
      _geChartStatusData();
    }
  }

  @override
  void initState() {
    super.initState();
    _getPageData();
  }

  @override
  void didUpdateWidget(covariant ScreenLogHttp oldWidget) {
    super.didUpdateWidget(oldWidget);
    _getPageData();
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: EdgeInsets.all(20.0),
      children: [
        Column(
          children: [
            // 统计总览
            WidgetLogOverview(overview: _overview),
            // 折线图，日志数-时间表
            WidgetLogChartOverview(_chartOverviewData),
            // 环形图，设备类型
            WidgetLogChartDeviceName(_chartDeviceNameData),
            // 环形图，操作系统
            WidgetLogChartOs(_chartOsData),
            // 环形图，浏览器
            WidgetLogChartBrowserName(_chartBrowserNameData),
            // 环形图，网络类型
            WidgetLogChartNetType(_chartNetTypeData),
            // 环形图，状态码
            WidgetLogChartStatus(_chartStatusData),
          ],
        ),
      ],
    );
  }
}
