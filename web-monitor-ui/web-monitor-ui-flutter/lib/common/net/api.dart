import 'dart:collection';

import 'package:dio/dio.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:web_monitor_app/common/net/result_data.dart';
import 'package:web_monitor_app/config/environment_config.dart';

import 'interceptors/error_interceptor.dart';

class HttpManager {
  static const CONTENT_TYPE_JSON = "application/json";
  static const CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

  Dio _dio = Dio(BaseOptions(
    baseUrl: EnvironmentConfig.API_URL,
    connectTimeout: 5000,
    receiveTimeout: 5000,
  ));

  HttpManager() {
    _dio.interceptors.add(ErrorInterceptors(_dio));
  }

  /// 发起网络请求
  Future<ResultData<T>> httpFetch<T>(
    url, {
    params,
    Map<String, dynamic> header,
    Options option,
    noTip = false,
  }) async {
    Map<String, dynamic> headers = HashMap();

    if (header != null) {
      headers.addAll(header);
    }

    if (option != null) {
      option.headers = headers;
    } else {
      option = Options(method: "get");
      option.headers = headers;
    }

    ResultData<T> _resultData;
    // 显示loading
    EasyLoading.show(status: "加载中，请稍候", maskType: EasyLoadingMaskType.black);
    try {
      var _response = await _dio.request(url, data: params, options: option);
      _resultData = ResultData<T>.success(_response);
    } on DioError catch (e) {
      _resultData = ResultData<T>.error(e);
    }
    // 隐藏loading
    EasyLoading.dismiss();
    return _resultData;
  }

  /// 业务GET请求
  /// 说明：用于发起业务端的http请求
  Future<ResultData<BizResultData>> httpGet(
    url, {
    params,
    Map<String, dynamic> header,
    Options option,
    noTip = false,
  }) {
    return httpFetch<BizResultData>(
      url,
      params: params ?? null,
      header: header ?? null,
      option: option ?? null,
      noTip: noTip ?? false,
    );
  }

  /// 业务POST请求
  /// 说明：用于发起业务端的http请求
  Future<ResultData<BizResultData>> httpPost<BizResultData>(
    url, {
    params,
    Map<String, dynamic> header,
    Options option,
    noTip = false,
  }) {
    Map<String, dynamic> headers = HashMap();
    if (header != null) {
      headers.addAll(header);
    }

    if (option == null) {
      option = Options(method: "post");
    }
    option.headers = headers;

    return httpFetch<BizResultData>(
      url,
      params: params ?? null,
      header: header ?? null,
      option: option ?? null,
      noTip: noTip ?? false,
    );
  }

  /// 获取业务返回数据
  /// 说明：该方法用于统一从ResultData结构出真实的业务返回数据
  Future<dynamic> getBizResultData(ResultData<BizResultData> resultData) async {
    if (resultData.success && resultData.data?.success) {
      return resultData.data?.data;
    } else {
      EasyLoading.showError(resultData.data?.msg?.toString() ?? "请求失败");
      return null;
    }
  }
}

final HttpManager httpManager = HttpManager();
