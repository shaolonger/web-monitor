import 'package:flutter/material.dart';

class WidgetLogOverview extends StatelessWidget {
  final String count;
  final String uv;
  final String rate;
  final String countPercent;
  final String uvPercent;
  final String ratePercent;

  const WidgetLogOverview({
    Key key,
    this.count = "0",
    this.uv = "0",
    this.rate = "-",
    this.countPercent = "-",
    this.uvPercent = "-",
    this.ratePercent = "-",
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
                    count,
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 22.0),
                  ),
                ),
                Text(
                  "对比昨日：$countPercent",
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
                    uv,
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 22.0),
                  ),
                ),
                Text(
                  "对比昨日：$uvPercent",
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
                    rate,
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 22.0),
                  ),
                ),
                Text(
                  "对比昨日：$ratePercent",
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
