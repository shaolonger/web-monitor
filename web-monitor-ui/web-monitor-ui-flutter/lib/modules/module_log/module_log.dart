import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_log/screens/screen_log_js.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_search_drawer.dart';
import 'package:web_monitor_app/utils/util_date_time.dart';

class ModuleLog extends StatefulWidget {
  @override
  _ModuleLogState createState() => _ModuleLogState();
}

class _ModuleLogState extends State<ModuleLog> with TickerProviderStateMixin {
  String _projectName;
  String _projectIdentifier;
  TabController _tabController;
  List _tabList = [];

  // 查询条件
  String _startTime = "";
  String _endTime = "";
  int _timeInterval = 60;

  /// 初始化查询参数
  void _initSearchParams() {
    var nowDate = DateTime.now().toLocal();
    var endDate = nowDate.add(Duration(days: 1));
    String startTime = UtilDateTime.getDateStrByFormatAndDateTime(
        dateTime: nowDate, pattern: "yyyy-MM-dd") + " 00:00:00";
    String endTime = UtilDateTime.getDateStrByFormatAndDateTime(
        dateTime: endDate, pattern: "yyyy-MM-dd") + " 00:00:00";
    setState(() {
      _startTime = startTime;
      _endTime = endTime;
    });
  }

  /// 初始化tab
  void _initTab() {
    setState(() {
      _tabList = [
        {
          "tabName": "JS",
          "tabContent": ScreenLogJs(
            projectIdentifier: _projectIdentifier,
            startTime: _startTime,
            endTime: _endTime,
            timeInterval: _timeInterval,
          ),
        },
        {
          "tabName": "HTTP",
          "tabContent": ScreenLogJs(
            projectIdentifier: _projectIdentifier,
            startTime: _startTime,
            endTime: _endTime,
            timeInterval: _timeInterval,
          ),
        },
        {
          "tabName": "RES",
          "tabContent": ScreenLogJs(
            projectIdentifier: _projectIdentifier,
            startTime: _startTime,
            endTime: _endTime,
            timeInterval: _timeInterval,
          ),
        },
        {
          "tabName": "CUS",
          "tabContent": ScreenLogJs(
            projectIdentifier: _projectIdentifier,
            startTime: _startTime,
            endTime: _endTime,
            timeInterval: _timeInterval,
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
      _projectIdentifier = params["projectIdentifier"] ?? "";
    });
    _initSearchParams();
    _initTab();
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
        this._startTime = startTime;
        this._endTime = endTime;
      }
      if (timeInterval > 0) {
        this._timeInterval = timeInterval;
      }
    });
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
        children: _tabList.map<Widget>((e) => e["tabContent"]).toList(),
      ),
    );
  }
}
