import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_overview/models/model_overview_item.dart';
import 'package:web_monitor_app/routes/routes.dart';

class WidgetOverviewItem extends StatelessWidget {
  final ModelOverviewListItem item;

  WidgetOverviewItem({
    @required this.item,
  });

  /// 跳转日志详情页
  void _navigate(BuildContext context) {
    String projectName = item.projectName;
    String projectIdentifier = item.projectIdentifier;
    var params = {
      "projectName": projectName,
      "projectIdentifier": projectIdentifier,
    };
    Navigator.pushNamed(context, moduleLog, arguments: params);
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => _navigate(context),
      child: Container(
        margin: EdgeInsets.only(top: 8.0, right: 16.0, bottom: 8.0, left: 16.0),
        padding: EdgeInsets.all(16.0),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.all(Radius.circular(10.0)),
          color: Colors.grey[100],
          boxShadow: [
            BoxShadow(
              color: Colors.grey[400],
              blurRadius: 3.0,
            )
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: EdgeInsets.only(bottom: 16.0),
              child: Text(
                item.projectName,
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 16.0,
                ),
              ),
            ),
            Flex(
              direction: Axis.horizontal,
              children: [
                Expanded(
                  flex: 1,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text("JS"),
                      Padding(
                        padding: EdgeInsets.only(right: 20.0),
                        child: Text(
                          item.data.jsErrorLogCount.toString(),
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 28.0,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  flex: 1,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text("HTTP"),
                      Padding(
                        padding: EdgeInsets.only(right: 20.0),
                        child: Text(
                          item.data.httpErrorLogCount.toString(),
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 28.0,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
            Flex(
              direction: Axis.horizontal,
              children: [
                Expanded(
                  flex: 1,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text("RES"),
                      Padding(
                        padding: EdgeInsets.only(right: 20.0),
                        child: Text(
                          item.data.resourceLoadErrorLogCount.toString(),
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 28.0,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  flex: 1,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text("CUS"),
                      Padding(
                        padding: EdgeInsets.only(right: 20.0),
                        child: Text(
                          item.data.customErrorLogCount.toString(),
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 28.0,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
