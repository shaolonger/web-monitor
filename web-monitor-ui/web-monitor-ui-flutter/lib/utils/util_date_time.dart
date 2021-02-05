import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class UtilDateTime {
  /// 根据日期格式，返回指定时间的日期字符串
  static String getDateStrByFormatAndDateTime({
    String pattern = "yyyy-MM-dd HH:mm:ss",
    @required DateTime dateTime,
  }) {
    return DateFormat(pattern).format(dateTime);
  }
}
