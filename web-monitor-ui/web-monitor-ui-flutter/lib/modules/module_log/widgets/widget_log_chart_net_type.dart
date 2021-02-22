import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_log/consts/const_log.dart';
import 'package:web_monitor_app/modules/module_log/widgets/widget_log_chart_indicator.dart';

class WidgetLogChartNetType extends StatefulWidget {
  final List<dynamic> chartData;

  WidgetLogChartNetType(this.chartData);

  @override
  _WidgetLogChartNetTypeState createState() =>
      _WidgetLogChartNetTypeState();
}

class _WidgetLogChartNetTypeState extends State<WidgetLogChartNetType> {
  final String _title = "网络类型";
  int _touchedIndex;

  /// 获取数据
  List<PieChartSectionData> _getSections() {
    int len = widget.chartData.length;
    return List.generate(len, (i) {
      final isTouched = i == _touchedIndex;
      final double fontSize = isTouched ? 25 : 16;
      final double radius = isTouched ? 60 : 50;
      var item = widget.chartData[i];
      return PieChartSectionData(
        color: ConstLog.CHART_COLORS_LIST[i],
        value: (item["count"] as int).toDouble(),
        title: (item["count"] as int).toString(),
        radius: radius,
        titleStyle: TextStyle(
          fontSize: fontSize,
          fontWeight: FontWeight.bold,
          color: const Color(0xffffffff),
        ),
      );
    });
  }

  /// 获取图例
  List<Widget> _getIndicators() {
    List<Widget> list = [];
    for (int i = 0; i < widget.chartData.length; i++) {
      var indicator = WidgetLogChartIndicator(
        color: ConstLog.CHART_COLORS_LIST[i],
        text: widget.chartData[i]["key"],
        isSquare: true,
      );
      list.add(indicator);
      if (i % 2 == 0) {
        list.add(SizedBox(
          height: 4,
        ));
      }
    }
    return list;
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
                _title,
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
                              child: AspectRatio(
                                aspectRatio: 1.0,
                                child: PieChart(
                                  PieChartData(
                                    pieTouchData: PieTouchData(
                                      touchCallback: (pieTouchResponse) {
                                        setState(() {
                                          if (pieTouchResponse.touchInput
                                                  is FlLongPressEnd ||
                                              pieTouchResponse.touchInput
                                                  is FlPanEnd) {
                                            _touchedIndex = -1;
                                          } else {
                                            _touchedIndex = pieTouchResponse
                                                .touchedSectionIndex;
                                          }
                                        });
                                      },
                                    ),
                                    borderData: FlBorderData(show: false),
                                    sectionsSpace: 0,
                                    centerSpaceRadius: 40,
                                    sections: _getSections(),
                                  ),
                                ),
                              ),
                            ),
                            Padding(
                              padding: EdgeInsets.only(
                                right: 10.0,
                                bottom: 10.0,
                              ),
                              child: Column(
                                mainAxisSize: MainAxisSize.max,
                                mainAxisAlignment: MainAxisAlignment.end,
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: _getIndicators(),
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
