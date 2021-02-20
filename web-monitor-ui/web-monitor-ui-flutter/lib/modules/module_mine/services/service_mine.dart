import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:web_monitor_app/core/net/api.dart';
import 'package:web_monitor_app/core/net/url_address.dart';
import 'package:web_monitor_app/core/notifier/model_user_info_change_notifier.dart';
import 'package:web_monitor_app/models/model_user_info.dart';

class ServiceMine {
  /// 获取用户详情
  static void getUserInfo(BuildContext context) async {
    var res = await httpManager.bizHttpGet(
      UrlAddress.getUserDetail,
      defaultErrorTip: "获取用户详情信息失败",
    );
    if (res.success) {
      var userInfo = ModelUserInfo.fromJson(res.data);
      Provider.of<ModelUserInfoChangeNotifier>(context, listen: false)
          .userInfo = userInfo;
    }
  }
}
