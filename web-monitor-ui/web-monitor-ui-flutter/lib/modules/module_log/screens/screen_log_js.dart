import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_overview.dart';

class ScreenLogJs extends StatefulWidget {
  final String projectName;
  final String projectIdentifier;

  const ScreenLogJs({
    Key key,
    @required this.projectName,
    @required this.projectIdentifier,
  }) : super(key: key);

  @override
  _ScreenLogJsState createState() => _ScreenLogJsState();
}

class _ScreenLogJsState extends State<ScreenLogJs> {
  @override
  Widget build(BuildContext context) {
    return ListView(
      padding: EdgeInsets.all(20.0),
      children: [
        WidgetLogOverview(),
      ],
    );
  }
}
