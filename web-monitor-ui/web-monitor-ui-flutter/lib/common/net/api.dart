import 'dart:collection';

import 'package:dio/dio.dart';
import 'package:web_monitor_app/common/net/result_data.dart';
import 'package:web_monitor_app/config/environment_config.dart';

import 'code.dart';
import 'interceptors/error_interceptor.dart';

class HttpManager {
  static const CONTENT_TYPE_JSON = "application/json";
  static const CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

  Dio _dio = new Dio(); // 使用默认配置

  HttpManager() {
    _dio.interceptors.add(new ErrorInterceptors(_dio));
  }

  /// 发起网络请求
  Future<dynamic> httpFetch(
    url, {
    params,
    Map<String, dynamic> header,
    Options option,
    noTip = false,
  }) async {
    Map<String, dynamic> headers = new HashMap();
    String _url = EnvironmentConfig.API_URL + url;

    if (header != null) {
      headers.addAll(header);
    }

    if (option != null) {
      option.headers = headers;
    } else {
      option = new Options(method: "get");
      option.headers = headers;
    }

    resultError(DioError e) {
      Response errorResponse;
      if (e.response != null) {
        errorResponse = e.response;
      } else {
        errorResponse = new Response(statusCode: 666);
      }
      if (e.type == DioErrorType.CONNECT_TIMEOUT ||
          e.type == DioErrorType.RECEIVE_TIMEOUT) {
        errorResponse.statusCode = Code.NETWORK_TIMEOUT;
      }
      return new ResultData(
          Code.errorHandleFunction(errorResponse.statusCode, e.message, noTip),
          false,
          errorResponse.statusCode);
    }

    Response response;
    try {
      response = await _dio.request(_url, data: params, options: option);
    } on DioError catch (e) {
      return resultError(e);
    }
    return response;
  }

  /// GET请求
  Future<dynamic> httpGet(
    url, {
    params,
    Map<String, dynamic> header,
    Options option,
    noTip = false,
  }) {
    return httpFetch(
      url,
      params: params ?? null,
      header: header ?? null,
      option: option ?? null,
      noTip: noTip ?? false,
    );
  }

  /// POST请求
  Future<dynamic> httpPost(
    url, {
    params,
    Map<String, dynamic> header,
    Options option,
    noTip = false,
  }) {
    Map<String, dynamic> headers = new HashMap();
    if (header != null) {
      headers.addAll(header);
    }

    if (option == null) {
      option = new Options(method: "post");
    }
    option.headers = headers;

    return httpFetch(
      url,
      params: params ?? null,
      header: header ?? null,
      option: option ?? null,
      noTip: noTip ?? false,
    );
  }
}

final HttpManager httpManager = new HttpManager();
