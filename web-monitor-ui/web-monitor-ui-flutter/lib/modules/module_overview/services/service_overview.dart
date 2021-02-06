import 'package:web_monitor_app/common/net/api.dart';
import 'package:web_monitor_app/common/net/url_address.dart';
import 'package:web_monitor_app/modules/module_overview/models/model_overview_item.dart';
import 'package:web_monitor_app/utils/util_date_time.dart';

class ServiceOverview {
  /// 获取用户关联的所有项目的统计情况列表
  static Future<List<ModelOverviewListItem>> getAllRelatedProjectOverview() async {
    List<ModelOverviewListItem> overviewList = [];
    var endDateTime = DateTime.now().toLocal();
    var startDateTime = endDateTime.subtract(Duration(minutes: 1));
    String startTime =
        UtilDateTime.getDateStrByFormatAndDateTime(dateTime: startDateTime);
    String endTime =
        UtilDateTime.getDateStrByFormatAndDateTime(dateTime: endDateTime);
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
