import 'package:connectivity/connectivity.dart';
import 'package:dio/dio.dart';

import '../code.dart';
import '../result_data.dart';

class ErrorInterceptors extends InterceptorsWrapper {
  final Dio _dio;

  ErrorInterceptors(this._dio);

  @override
  Future onRequest(RequestOptions options) async {
    // 没有网络的情况
    var connectivityResult = await (Connectivity().checkConnectivity());
    if (connectivityResult == ConnectivityResult.none) {
      return _dio.reject<ResultData>(
        ResultData(
          success: false,
          data: null,
          msg: "网络连接异常",
          statusCode: Code.NETWORK_ERROR,
        ),
      );
    }
    return options;
  }
}
