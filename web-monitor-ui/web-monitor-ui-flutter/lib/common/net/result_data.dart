import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:web_monitor_app/common/net/code.dart';

/// 请求响应的结果
class ResultData {
  /// 请求结果是否成功，true-成功，false-失败
  bool success;

  /// 请求返回的数据
  dynamic data;

  /// 信息
  String msg;

  /// 请求结果的statusCode
  int statusCode;

  ResultData({
    @required this.success,
    @required this.data,
    this.msg,
    this.statusCode,
  });

  /// 成功
  ResultData.success(Response response) {
    success = true;
    data = response.data;
    statusCode = response.statusCode;
  }

  /// 错误
  ResultData.error(DioError e) {
    Response errorResponse;
    if (e.response != null) {
      errorResponse = e.response;
    } else {
      errorResponse = Response(statusCode: Code.NO_RESPONSE_ERROR);
    }
    if (e.type == DioErrorType.CONNECT_TIMEOUT ||
        e.type == DioErrorType.RECEIVE_TIMEOUT) {
      errorResponse.statusCode = Code.NETWORK_TIMEOUT;
    }
    success = false;
    data = null;
    msg = e.message;
    statusCode = errorResponse.statusCode;
  }
}

/// 业务返回数据
class BizResultData {
  /// 请求结果是否成功，true-成功，false-失败
  bool success;

  /// 数据
  dynamic data;

  /// 请求结果描述
  String msg;

  BizResultData({
    this.success = true,
    this.data,
    this.msg = "",
  });

  BizResultData.error(String msg) {
    this.success = false;
    this.msg = msg;
  }

  BizResultData.fromJson(Map<String, dynamic> json) {
    this.success = json["success"];
    this.data = json["data"];
    this.msg = json["msg"];
  }
}
