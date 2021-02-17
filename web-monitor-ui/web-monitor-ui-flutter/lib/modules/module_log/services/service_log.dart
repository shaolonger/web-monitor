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
}
