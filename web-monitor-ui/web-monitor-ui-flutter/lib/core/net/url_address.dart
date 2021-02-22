class UrlAddress {
  /// 登录
  static const String login = "/user/login";

  /// 获取用户详情
  static const String getUserDetail = "/user/getDetail";

  /// 根据用户获取关联的项目
  static const String getRelatedProjectList = "/user/getRelatedProjectList";

  /// 获取用户关联的所有项目的统计情况列表
  static const String getAllRelatedProjectOverview =
      "/statistic/getAllProjectOverviewListBetweenDiffDate";

  /// 获取两个日期之间的对比数据
  static const String getLogCountBetweenDiffDate =
      "/statistic/getLogCountBetweenDiffDate";

  /// 获取两个日期之间的设备、操作系统、浏览器、网络类型、状态码、资源类型的统计数据
  static const String getLogDistributionBetweenDiffDate =
      "/statistic/getLogDistributionBetweenDiffDate";
}
