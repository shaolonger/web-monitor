import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_overview/models/model_overview_item.dart';

class WidgetOverviewItem extends StatelessWidget {
  final ModelOverviewListItem item;

  WidgetOverviewItem({
    @required this.item,
  });
  
  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Text(item.projectName),
          Flex(
            direction: Axis.horizontal,
            children: [
              Expanded(
                flex: 1,
                child: Row(
                  children: [
                    Text("JS"),
                    Text(item.data.jsErrorLogCount.toString()),
                  ],
                ),
              ),
              Expanded(
                flex: 1,
                child: Row(
                  children: [
                    Text("HTTP"),
                    Text(item.data.httpErrorLogCount.toString()),
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
                  children: [
                    Text("RES"),
                    Text(item.data.resourceLoadErrorLogCount.toString()),
                  ],
                ),
              ),
              Expanded(
                flex: 1,
                child: Row(
                  children: [
                    Text("CUS"),
                    Text(item.data.customErrorLogCount.toString()),
                  ],
                ),
              ),
            ],
          ),
        ],
      ),
      decoration: BoxDecoration(
        boxShadow: [
          BoxShadow(
            color: Colors.grey,
            blurRadius: 1.0,
          )
        ],
      ),
    );
  }
}
