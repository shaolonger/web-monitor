import 'package:flutter/material.dart';
import 'package:web_monitor_app/config/global_config.dart';
import 'package:web_monitor_app/routes/routes.dart';

class WidgetOptionList extends StatefulWidget {
  @override
  _WidgetOptionListState createState() => _WidgetOptionListState();
}

class _WidgetOptionListState extends State<WidgetOptionList> {
  final List<Map<String, Object>> _optionList = [
    {
      "icon": Icon(Icons.style, size: 30.0),
      "name": "项目管理",
      "routeName": moduleProjectManage,
    },
    {
      "icon": Icon(Icons.info, size: 30.0),
      "name": "关于${GlobalConfig.APP_NAME_CODE}",
      "routeName": moduleAbout,
    },
  ];

  /// 点击跳转
  void _onItemTap(String routeName) {
    Navigator.pushNamed(context, routeName);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(top: 16.0),
      child: Column(
        children: _optionList.map((e) {
          return GestureDetector(
            child: Container(
              decoration: BoxDecoration(
                border: Border(
                  bottom: BorderSide(
                    color: Colors.grey[400],
                    width: 1.0,
                    style: BorderStyle.solid,
                  ),
                ),
              ),
              padding: EdgeInsets.only(
                  top: 16.0, right: 16.0, bottom: 16.0, left: 32.0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Row(
                    children: [
                      e["icon"],
                      Padding(
                        padding: EdgeInsets.only(left: 16.0),
                        child: Text(e["name"]),
                      ),
                    ],
                  ),
                  Padding(
                    padding: EdgeInsets.only(right: 24.0),
                    child: Icon(
                      Icons.arrow_forward_ios,
                      size: 20.0,
                      color: Colors.grey[400],
                    ),
                  ),
                ],
              ),
            ),
            onTap: () => _onItemTap(e["routeName"]),
          );
        }).toList(),
      ),
    );
  }
}
