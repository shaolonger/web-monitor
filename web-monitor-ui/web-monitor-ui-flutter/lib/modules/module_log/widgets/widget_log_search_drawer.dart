import 'package:flutter/material.dart';
import 'package:flutter_datetime_picker/flutter_datetime_picker.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:web_monitor_app/utils/util_date_time.dart';

class WidgetLogSearchDrawer extends StatefulWidget {
  final Function setSearchParams;
  final String startTime;
  final String endTime;
  final int timeInterval;

  const WidgetLogSearchDrawer({
    Key key,
    @required this.setSearchParams,
    @required this.startTime,
    @required this.endTime,
    @required this.timeInterval,
  }) : super(key: key);

  @override
  _WidgetLogSearchDrawerState createState() => _WidgetLogSearchDrawerState();
}

class _WidgetLogSearchDrawerState extends State<WidgetLogSearchDrawer> {
  final _timeIntervalOptionList = [
    // {
    //   "label": "分钟",
    //   "value": 60,
    // },
    {
      "label": "小时",
      "value": 3600,
    },
    {
      "label": "天",
      "value": 86400,
    },
  ];
  String _startTime;
  String _endTime;
  int _timeInterval;

  @override
  void initState() {
    super.initState();
    _receiveParentParams();
  }

  /// 将父组件的数值初始化到当前组件内
  void _receiveParentParams() {
    setState(() {
      this._startTime = widget.startTime;
      this._endTime = widget.endTime;
      this._timeInterval = widget.timeInterval;
    });
  }

  /// 校验查询参数合法性
  bool _checkSearchParams() {
    bool result = true;
    // 判断开始、结束时间
    var startDate = DateTime.parse(_startTime);
    var endDate = DateTime.parse(_endTime);
    if (endDate.isBefore(startDate)) {
      result = false;
      EasyLoading.showError("结束时间不得早于开始时间");
    }
    return result;
  }

  /// 调用父组件设置查询参数的方法
  void _setSearchParamsDrawer() {
    if (_checkSearchParams()) {
      widget.setSearchParams(
        startTime: _startTime,
        endTime: _endTime,
        timeInterval: _timeInterval,
      );
      _closeDrawer();
    }
  }

  /// 设置开始时间
  void _setStartTime(DateTime date) {
    String time = UtilDateTime.getDateStrByFormatAndDateTime(
            dateTime: date, pattern: "yyyy-MM-dd") +
        " 00:00:00";
    setState(() {
      _startTime = time;
    });
  }

  /// 设置结束时间
  void _setEndTime(DateTime date) {
    String time = UtilDateTime.getDateStrByFormatAndDateTime(
            dateTime: date, pattern: "yyyy-MM-dd") +
        " 00:00:00";
    setState(() {
      _endTime = time;
    });
  }

  /// 设置时间间隔
  void _setTimeInterval(int value) {
    setState(() {
      _timeInterval = value;
    });
  }

  /// 关闭抽屉
  void _closeDrawer() {
    Navigator.pop(context);
  }

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: MediaQuery.removePadding(
        context: context,
        removeTop: true,
        child: Padding(
          padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 30.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: EdgeInsets.symmetric(vertical: 10.0),
                child: Text(
                  "1.时间范围",
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              Padding(
                padding: EdgeInsets.only(left: 14.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text("开始时间"),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(_startTime),
                        RaisedButton(
                          child: Text("修改"),
                          onPressed: () {
                            DatePicker.showDatePicker(
                              context,
                              showTitleActions: true,
                              locale: LocaleType.zh,
                              onConfirm: (date) => _setStartTime(date),
                            );
                          },
                        ),
                      ],
                    ),
                    Text("结束时间"),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(_endTime),
                        RaisedButton(
                          child: Text("修改"),
                          onPressed: () {
                            DatePicker.showDatePicker(
                              context,
                              showTitleActions: true,
                              locale: LocaleType.zh,
                              onConfirm: (date) => _setEndTime(date),
                            );
                          },
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              Padding(
                padding: EdgeInsets.symmetric(vertical: 10.0),
                child: Text(
                  "2.时间粒度",
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              Padding(
                padding: EdgeInsets.only(left: 14.0),
                child: Row(
                  children: _timeIntervalOptionList.map((e) {
                    return Row(
                      children: [
                        Radio<int>(
                          value: e["value"],
                          groupValue: _timeInterval,
                          onChanged: _setTimeInterval,
                        ),
                        Text(e["label"]),
                      ],
                    );
                  }).toList(),
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  RaisedButton(
                    color: Colors.blue,
                    child: Text(
                      "确定",
                      style: TextStyle(
                        color: Colors.white,
                      ),
                    ),
                    onPressed: _setSearchParamsDrawer,
                  ),
                  RaisedButton(
                    child: Text("取消"),
                    onPressed: _closeDrawer,
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
