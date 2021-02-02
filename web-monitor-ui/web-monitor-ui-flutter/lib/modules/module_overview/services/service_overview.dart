import 'package:web_monitor_app/common/net/api.dart';
import 'package:web_monitor_app/common/net/url_address.dart';

class ServiceOverview {

  /// 获取用户关联的所有项目的统计情况列表
  static Future<List> getAllRelatedProjectOverview() async {
    var res = await httpManager.bizHttpPost(
      UrlAddress.getAllRelatedProjectOverview,
      defaultErrorTip: "获取项目统计总览列表失败",
    );
    if (res.success) {
    }
  }
}