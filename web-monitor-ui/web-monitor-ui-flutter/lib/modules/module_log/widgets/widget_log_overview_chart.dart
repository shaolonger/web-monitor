import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';

class WidgetLogOverviewChart extends StatefulWidget {
  final List<dynamic> chartData;

  WidgetLogOverviewChart(this.chartData);

  @override
  _WidgetLogOverviewChartState createState() => _WidgetLogOverviewChartState();
}

class _WidgetLogOverviewChartState extends State<WidgetLogOverviewChart> {
  /// 获取X轴显示文本
  String _getBottomTiles(double value) {
    return "";
  }

  @override
  Widget build(BuildContext context) {
    return widget.chartData.length == 0
        ? Container()
        : Padding(
            padding: EdgeInsets.symmetric(vertical: 16.0),
            child: LineChart(
              LineChartData(
                titlesData: FlTitlesData(
                  leftTitles: SideTitles(
                    showTitles: true,
                    reservedSize: 10.0,
                  ),
                  bottomTitles: SideTitles(
                    showTitles: true,
                    getTitles: (value) => _getBottomTiles(value),
                  ),
                ),
                borderData: FlBorderData(
                  show: true,
                  border: Border.all(color: const Color(0xff37434d), width: 1),
                ),
                lineBarsData: [
                  LineChartBarData(
                    isCurved: true,
                    colors: [Colors.blue[400]],
                    barWidth: 4.0,
                    isStrokeCapRound: true,
                    spots: widget.chartData.map((e) {
                      double dateValue = DateTime.parse(e["key"])
                          .millisecondsSinceEpoch
                          .toDouble();
                      double count = (e["count"] as int).toDouble() ?? 0;
                      return FlSpot(
                        dateValue,
                        count,
                      );
                    }).toList(),
                  ),
                ],
              ),
            ),
          );
  }
}
