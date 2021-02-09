import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_mine/widgets/widget_option_list.dart';
import 'package:web_monitor_app/modules/module_mine/widgets/widget_user_block.dart';

class ModuleMine extends StatefulWidget {
  @override
  _ModuleMineState createState() => _ModuleMineState();
}

class _ModuleMineState extends State<ModuleMine> {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        WidgetUserBlock(),
        WidgetOptionList(),
      ],
    );
  }
}