import 'dart:collection';

import 'package:dio/dio.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:web_monitor_app/common/net/interceptors/token_interceptor.dart';
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
    _dio.interceptors.add(TokenInterceptor());
  }

  /// 通用的网络请求
  Future<ResultData> httpFetch(
    String url, {
    dynamic params,
    Map<String, dynamic> header,
    Options option,
    bool showLoading = false,
    bool showErrorTip = true,
    String defaultErrorTip,
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

    ResultData _resultData;
    // 显示loading
    if (showLoading) {
      EasyLoading.show(status: "加载中，请稍候", maskType: EasyLoadingMaskType.black);
    }
    try {
      var _response = await _dio.request(url, queryParameters: params, options: option);
      _resultData = ResultData.success(_response);
    } on DioError catch (e) {
      _resultData = ResultData.error(e);
      if (showErrorTip) {
        EasyLoading.showError(_resultData.msg ?? defaultErrorTip);
      }
    }
    // 隐藏loading
    EasyLoading.dismiss();
    return _resultData;
  }

  /// 业务GET请求
  /// 说明：用于发起业务端的http请求
  Future<BizResultData> bizHttpGet(
    String url, {
    dynamic params,
    bool showLoading,
    bool showErrorTip,
    String defaultErrorTip,
  }) async {
    var resultData = await httpFetch(
      url,
      params: params,
      showLoading: showLoading ?? false,
      showErrorTip: showErrorTip ?? true,
      defaultErrorTip: defaultErrorTip,
    );
    return getBizResultDataByResultData(resultData,
        showErrorTip: showErrorTip ?? true, defaultErrorTip: defaultErrorTip);
  }

  /// 业务POST请求
  /// 说明：用于发起业务端的http请求
  Future<BizResultData> bizHttpPost(
    String url, {
    dynamic params,
    bool showLoading,
    bool showErrorTip,
    String defaultErrorTip,
  }) async {
    var option = Options(method: "post");
    var resultData = await httpFetch(
      url,
      params: params,
      option: option,
      showLoading: showLoading ?? true,
      showErrorTip: showErrorTip ?? true,
      defaultErrorTip: defaultErrorTip,
    );
    return getBizResultDataByResultData(resultData,
        showErrorTip: showErrorTip ?? true, defaultErrorTip: defaultErrorTip);
  }

  /// 从通用的ResultData中解构出BizResultData
  /// 说明：该方法用于统一从ResultData结构出真实的业务返回数据
  BizResultData getBizResultDataByResultData(
    ResultData resultData, {
    bool showErrorTip = true,
    String defaultErrorTip,
  }) {
    if (resultData.success) {
      BizResultData bizResultData = BizResultData.fromJson(resultData.data);
      if (!bizResultData.success && showErrorTip) {
        EasyLoading.showError(bizResultData.msg ?? defaultErrorTip);
      }
      return bizResultData;
    } else {
      return BizResultData.error(resultData.msg);
    }
  }
}

final HttpManager httpManager = HttpManager();
