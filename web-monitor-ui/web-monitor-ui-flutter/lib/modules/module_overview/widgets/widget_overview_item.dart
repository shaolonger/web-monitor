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
      margin: EdgeInsets.only(top: 8.0, right: 16.0, bottom: 8.0, left: 16.0),
      padding: EdgeInsets.all(16.0),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.all(Radius.circular(10.0)),
        color: Color(0xF2F2F2FF),
        boxShadow: [
          BoxShadow(
            color: Colors.grey,
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
    );
  }
}
