import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_log/models/model_log_overview.dart';

class WidgetLogOverview extends StatelessWidget {
  final ModelLogOverview overview;

  const WidgetLogOverview({
    Key key,
    @required this.overview,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
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
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          Container(
            child: Column(
              children: [
                Text(
                  "异常次数",
                  style: TextStyle(color: Colors.grey[500]),
                ),
                Padding(
                  padding: EdgeInsets.symmetric(vertical: 10.0),
                  child: Text(
                    overview.count.today.toString(),
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 22.0),
                  ),
                ),
                Text(
                  "对比昨日：${overview.count.rate}",
                  style: TextStyle(color: Colors.grey[500]),
                ),
              ],
            ),
          ),
          Container(
            height: 60.0,
            child: VerticalDivider(
              color: Colors.grey[400],
            ),
          ),
          Container(
            child: Column(
              children: [
                Text(
                  "影响用户",
                  style: TextStyle(color: Colors.grey[500]),
                ),
                Padding(
                  padding: EdgeInsets.symmetric(vertical: 10.0),
                  child: Text(
                    overview.affectUV.today.toString(),
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 22.0),
                  ),
                ),
                Text(
                  "对比昨日：${overview.affectUV.rate}",
                  style: TextStyle(color: Colors.grey[500]),
                ),
              ],
            ),
          ),
          Container(
            height: 60.0,
            child: VerticalDivider(
              color: Colors.grey[400],
            ),
          ),
          Container(
            child: Column(
              children: [
                Text(
                  "影响用户占比",
                  style: TextStyle(color: Colors.grey[500]),
                ),
                Padding(
                  padding: EdgeInsets.symmetric(vertical: 10.0),
                  child: Text(
                    overview.affectUVPercent.today.toString(),
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 22.0),
                  ),
                ),
                Text(
                  "对比昨日：${overview.affectUVPercent.rate}",
                  style: TextStyle(color: Colors.grey[500]),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
