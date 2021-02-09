import 'package:flutter/material.dart';
import 'package:web_monitor_app/config/global_config.dart';

class ModuleAbout extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("关于${GlobalConfig.APP_NAME_CODE}"),
      ),
      body: Padding(
        padding: EdgeInsets.only(
          top: 60.0,
          left: 16.0,
          right: 16.0,
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              GlobalConfig.APP_INTRODUCTION_FULL,
              style: TextStyle(fontSize: 16.0),
            ),
            Padding(
              padding: EdgeInsets.symmetric(vertical: 36.0),
              child: Text(
                GlobalConfig.APP_GITHUB_ADDRESS,
                style: TextStyle(fontSize: 16.0),
              ),
            ),
            Text(
              GlobalConfig.APP_GITHUB_REMARK,
              style: TextStyle(fontSize: 16.0),
            ),
          ],
        ),
      ),
    );
  }
}
