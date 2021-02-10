import 'package:flutter/material.dart';
import 'package:web_monitor_app/core/net/api.dart';
import 'package:web_monitor_app/core/net/url_address.dart';
import 'package:web_monitor_app/models/model_project_info.dart';

class ServiceProjectManage {
  /// 根据用户获取关联的项目
  static Future<List<ModelProjectInfo>> getRelatedProjectList(BuildContext context) async {
    var res = await httpManager.bizHttpGet(
      UrlAddress.getRelatedProjectList,
      defaultErrorTip: "获取用户关联的项目失败",
    );
    List<ModelProjectInfo> list = [];
    if (res.success) {
      if (res.data is List) {
        (res.data as List).forEach((element) {
          ModelProjectInfo projectInfo = ModelProjectInfo.fromJson(element);
          list.add(projectInfo);
        });
      }
    }
    return list;
  }
}