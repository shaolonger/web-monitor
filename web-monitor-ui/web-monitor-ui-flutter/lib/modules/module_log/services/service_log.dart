import 'package:flutter/material.dart';
import 'package:web_monitor_app/core/net/api.dart';
import 'package:web_monitor_app/core/net/url_address.dart';
import 'package:web_monitor_app/modules/module_log/models/model_log_count_between_diff_date.dart';

class ServiceLog {
  /// 获取两个日期之间的对比数据
  static Future<ModelLogCountBetweenDiffDate> getLogCountBetweenDiffDate({
    @required String projectIdentifier,
    @required String logTypeList,
    @required String indicatorList,
    @required String startTime,
    @required String endTime,
    @required int timeInterval,
  }) async {
    ModelLogCountBetweenDiffDate model = ModelLogCountBetweenDiffDate();
    var res = await httpManager.bizHttpGet(
      UrlAddress.getLogCountBetweenDiffDate,
      params: {
        "projectIdentifier": projectIdentifier,
        "logTypeList": logTypeList,
        "indicatorList": indicatorList,
        "startTime": startTime,
        "endTime": endTime,
        "timeInterval": timeInterval,
      },
      defaultErrorTip: "获取两个日期之间的对比数据失败",
    );
    if (res.success) {
      model = ModelLogCountBetweenDiffDate.fromJson(res.data);
    }
    return model;
  }

  /// 获取两个日期之间的设备、操作系统、浏览器、网络类型、状态码、资源类型的统计数据
  static Future<List<dynamic>> getLogDistributionBetweenDiffDate({
    @required String projectIdentifier,
    @required String logType,
    @required String indicator,
    @required String startTime,
    @required String endTime,
  }) async {
    List<dynamic> resultList = [];
    var res = await httpManager.bizHttpGet(
      UrlAddress.getLogDistributionBetweenDiffDate,
      params: {
        "projectIdentifier": projectIdentifier,
        "logType": logType,
        "indicator": indicator,
        "startTime": startTime,
        "endTime": endTime,
      },
      defaultErrorTip: "获取两个日期之间的对比数据失败",
    );
    if (res.success) {
      resultList = res.data;
    }
    return resultList;
  }
}
