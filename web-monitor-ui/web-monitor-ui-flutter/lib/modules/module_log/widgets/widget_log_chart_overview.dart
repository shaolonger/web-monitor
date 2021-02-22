import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';

class WidgetLogChartOverview extends StatefulWidget {
  final List<dynamic> chartData;

  WidgetLogChartOverview(this.chartData);

  @override
  _WidgetLogChartOverviewState createState() => _WidgetLogChartOverviewState();
}

class _WidgetLogChartOverviewState extends State<WidgetLogChartOverview> {
  /// 获取X轴显示文本
  String _getBottomTiles(double value) {
    return "";
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 16.0),
      child: Card(
        color: Colors.white,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: EdgeInsets.all(12.0),
              child: Text(
                "日志数",
                style: TextStyle(fontSize: 18.0),
              ),
            ),
            AspectRatio(
              aspectRatio: 1.0,
              child: Column(
                children: [
                  widget.chartData.length == 0
                      ? Expanded(
                          child: Center(
                            child: Text("暂无数据"),
                          ),
                        )
                      : Row(
                          children: [
                            Expanded(
                              child: LineChart(
                                LineChartData(
                                  titlesData: FlTitlesData(
                                    leftTitles: SideTitles(
                                      showTitles: true,
                                      reservedSize: 10.0,
                                    ),
                                    bottomTitles: SideTitles(
                                      showTitles: true,
                                      getTitles: (value) =>
                                          _getBottomTiles(value),
                                    ),
                                  ),
                                  borderData: FlBorderData(
                                    show: false,
                                    border: Border.all(
                                        color: const Color(0xff37434d),
                                        width: 1),
                                  ),
                                  lineBarsData: [
                                    LineChartBarData(
                                      isCurved: true,
                                      colors: [Colors.blue[400]],
                                      barWidth: 4.0,
                                      isStrokeCapRound: true,
                                      spots: widget.chartData.map((e) {
                                        double dateValue =
                                            DateTime.parse(e["key"])
                                                .millisecondsSinceEpoch
                                                .toDouble();
                                        double count =
                                            (e["count"] as int).toDouble() ?? 0;
                                        return FlSpot(
                                          dateValue,
                                          count,
                                        );
                                      }).toList(),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ],
                        ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
