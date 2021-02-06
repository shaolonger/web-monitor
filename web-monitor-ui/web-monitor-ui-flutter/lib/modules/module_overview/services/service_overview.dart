import 'package:flutter/material.dart';
import 'package:web_monitor_app/core/net/api.dart';
import 'package:web_monitor_app/core/net/url_address.dart';
import 'package:web_monitor_app/modules/module_overview/models/model_overview_item.dart';

class ServiceOverview {
  /// 获取用户关联的所有项目的统计情况列表
  static Future<List<ModelOverviewListItem>> getAllRelatedProjectOverview({
    @required String startTime,
    @required String endTime,
  }) async {
    List<ModelOverviewListItem> overviewList = [];
    var res = await httpManager.bizHttpGet(
      UrlAddress.getAllRelatedProjectOverview,
      params: {
        "startTime": startTime,
        "endTime": endTime,
      },
      defaultErrorTip: "获取项目统计总览列表失败",
    );
    if (res.success) {
      (res.data as List).forEach((element) {
        ModelOverviewListItem item = ModelOverviewListItem.fromJson(element);
        overviewList.add(item);
      });
    }
    return overviewList;
  }
}
