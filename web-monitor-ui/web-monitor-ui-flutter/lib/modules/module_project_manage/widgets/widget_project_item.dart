import 'package:flutter/material.dart';
import 'package:web_monitor_app/models/model_project_info.dart';

class WidgetProjectItem extends StatelessWidget {
  final ModelProjectInfo projectInfo;

  WidgetProjectItem({
    @required this.projectInfo,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
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
        children: [
          Padding(
            padding: EdgeInsets.only(bottom: 10.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("项目名"),
                Text(projectInfo.projectName),
              ],
            ),
          ),
          Padding(
            padding: EdgeInsets.only(bottom: 10.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("项目标识"),
                Text(projectInfo.projectIdentifier),
              ],
            ),
          ),
          Padding(
            padding: EdgeInsets.only(bottom: 10.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text("项目描述"),
                Text(projectInfo.description),
              ],
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text("创建时间"),
              Text(projectInfo.createTime),
            ],
          ),
        ],
      ),
    );
  }
}
