import 'package:flutter/material.dart';

class WidgetJsLog extends StatefulWidget {
  final String projectName;
  final String projectIdentifier;

  const WidgetJsLog({
    Key key,
    @required this.projectName,
    @required this.projectIdentifier,
  }) : super(key: key);

  @override
  _WidgetJsLogState createState() => _WidgetJsLogState();
}

class _WidgetJsLogState extends State<WidgetJsLog> {
  @override
  Widget build(BuildContext context) {
    return ListView(
      children: [
        Text("${widget.projectName}"),
        Text("${widget.projectIdentifier}"),
      ],
    );
  }
}
