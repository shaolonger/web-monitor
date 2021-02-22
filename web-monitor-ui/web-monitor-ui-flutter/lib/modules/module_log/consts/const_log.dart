import 'dart:ui';

class ConstLog {
  /// 日志类型，包括jsErrorLog、httpErrorLog、resourceLoadErrorLog、customErrorLog
  static const LOG_TYPE_LIST_MAP = {
    "JS": "jsErrorLog",
    "HTTP": "httpErrorLog",
    "RES": "resourceLoadErrorLog",
    "CUS": "customErrorLog",
  };
  /// 统计类型
  static const INDICATOR_LIST_MAP = {
    // 设备类型
    "DEVICE_NAME": "device_name",
    // 操作系统
    "OS": "os",
    // 浏览器
    "BROWSER_NAME": "browser_name",
    // 网络类型
    "NET_TYPE": "net_type",
    // 状态码
    "STATUS": "status",
    // 资源类型
    "RESOURCE_TYPE": "resource_type",
  };
  /// 图标颜色集合
  static const CHART_COLORS_LIST = [
    Color(0xff0293ee),
    Color(0xfff8b250),
    Color(0xff845bef),
    Color(0xff13d38e),
  ];
}
