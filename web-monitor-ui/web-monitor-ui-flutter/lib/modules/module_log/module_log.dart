import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_log/screens/screen_log_cus.dart';
import 'package:web_monitor_app/modules/module_log/screens/screen_log_http.dart';
import 'package:web_monitor_app/modules/module_log/screens/screen_log_js.dart';
import 'package:web_monitor_app/modules/module_log/screens/screen_log_res.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_search_drawer.dart';
import 'package:web_monitor_app/utils/util_date_time.dart';

class ModuleLog extends StatefulWidget {
  @override
  _ModuleLogState createState() => _ModuleLogState();
}

class _ModuleLogState extends State<ModuleLog> with TickerProviderStateMixin {
  String _projectName = "";
  String _projectIdentifier = "";
  TabController _tabController;
  final List _tabList = [
    {
      "tabName": "JS",
    },
    {
      "tabName": "HTTP",
    },
    {
      "tabName": "RES",
    },
    {
      "tabName": "CUS",
    },
  ];

  // 查询条件
  String _startTime = "";
  String _endTime = "";
  int _timeInterval = 3600;

  /// 初始化查询参数
  void _initSearchParams() {
    var nowDate = DateTime.now().toLocal();
    var endDate = nowDate.add(Duration(days: 1));
    String startTime = UtilDateTime.getDateStrByFormatAndDateTime(
            dateTime: nowDate, pattern: "yyyy-MM-dd") +
        " 00:00:00";
    String endTime = UtilDateTime.getDateStrByFormatAndDateTime(
            dateTime: endDate, pattern: "yyyy-MM-dd") +
        " 00:00:00";
    setState(() {
      _startTime = startTime;
      _endTime = endTime;
    });
  }

  /// 初始化tab
  void _initTab() {
    setState(() {
      _tabController = TabController(length: _tabList.length, vsync: this);
    });
  }

  /// 获取路由传参
  void _getRouteParams(BuildContext context) {
    final Map<String, String> params =
        ModalRoute.of(context).settings.arguments;
    setState(() {
      _projectName = params["projectName"] ?? "";
      _projectIdentifier = params["projectIdentifier"] ?? "";
    });
    _initSearchParams();
  }

  /// 打开抽屉
  void _openDrawer(BuildContext context) {
    Scaffold.of(context).openDrawer();
  }

  /// 修改查询参数
  /// 注：在查询页子组件调用父组件的该方法
  void _setSearchParams({String startTime, String endTime, int timeInterval}) {
    setState(() {
      if (startTime.isNotEmpty && endTime.isNotEmpty) {
        _startTime = startTime;
        _endTime = endTime;
      }
      if (timeInterval > 0) {
        _timeInterval = timeInterval;
      }
    });
  }

  @override
  void initState() {
    super.initState();
    _initTab();

    // 原本路由参数的获取应该放在build中，但考虑到放build内，则当setState引起的rebuild时，
    // 会再次触发该函数，导致参数被覆盖，因此将其放在initState中，并通过异步的方式执行
    Future.delayed(Duration.zero, () {
      _getRouteParams(context);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_projectName),
        bottom: TabBar(
          isScrollable: true,
          controller: _tabController,
          tabs: _tabList.map((e) => Tab(text: e["tabName"])).toList(),
        ),
        leading: Builder(
          builder: (BuildContext context) {
            return IconButton(
              icon: Icon(
                Icons.arrow_back_outlined,
                color: Colors.white,
              ),
              onPressed: () => Navigator.of(context).pop(),
            );
          },
        ),
        actions: [
          Builder(builder: (BuildContext context) {
            return IconButton(
              icon: Icon(Icons.search),
              onPressed: () => _openDrawer(context),
            );
          }),
        ],
      ),
      drawer: WidgetLogSearchDrawer(
        startTime: _startTime,
        endTime: _endTime,
        timeInterval: _timeInterval,
        setSearchParams: _setSearchParams,
      ),
      body: TabBarView(
        controller: _tabController,
        physics: NeverScrollableScrollPhysics(),
        children: [
          ScreenLogJs(
            projectIdentifier: _projectIdentifier,
            startTime: _startTime,
            endTime: _endTime,
            timeInterval: _timeInterval,
          ),
          ScreenLogHttp(
            projectIdentifier: _projectIdentifier,
            startTime: _startTime,
            endTime: _endTime,
            timeInterval: _timeInterval,
          ),
          ScreenLogRes(
            projectIdentifier: _projectIdentifier,
            startTime: _startTime,
            endTime: _endTime,
            timeInterval: _timeInterval,
          ),
          ScreenLogCus(
            projectIdentifier: _projectIdentifier,
            startTime: _startTime,
            endTime: _endTime,
            timeInterval: _timeInterval,
          ),
        ],
      ),
    );
  }
}
