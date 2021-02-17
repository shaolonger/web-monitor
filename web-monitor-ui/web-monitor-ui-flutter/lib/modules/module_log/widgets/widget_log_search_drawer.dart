import 'package:flutter/material.dart';

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

  /// 调用父组件设置查询参数的方法
  void _setSearchParamsDrawer() {
    widget.setSearchParams(
        startTime: _startTime, endTime: _endTime, timeInterval: _timeInterval);
  }

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          children: [
            Text("时间范围："),
            Row(
              children: [
                Text(""),
                RaisedButton(
                  child: Text("选择起止时间"),
                  onPressed: () {
                    setState(() {
                      _startTime = "2020-02-20";
                      _endTime = "2020-02-20";
                    });
                  },
                ),
              ],
            ),
            Row(
              children: [
                RaisedButton(
                  child: Text("确定"),
                  onPressed: _setSearchParamsDrawer,
                ),
                RaisedButton(
                  child: Text("取消"),
                  onPressed: () {},
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
