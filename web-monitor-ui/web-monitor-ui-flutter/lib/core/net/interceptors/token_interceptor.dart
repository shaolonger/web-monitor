import 'package:dio/dio.dart';
import 'package:web_monitor_app/config/global_config.dart';

import 'package:web_monitor_app/core/auth/auth_manager.dart';
import 'package:web_monitor_app/core/config/config_global.dart';
import 'package:web_monitor_app/routes/routes.dart';

class TokenInterceptor extends InterceptorsWrapper {
  String _token;

  @override
  Future onRequest(RequestOptions options) async {
    String url = options.path;
    // 先判断请求地址是否在白名单内
    bool isNoNeedAuth = AuthManager.checkIsNoNeedAuth(url);
    if (!isNoNeedAuth) {
      if (_token == null) {
        String token = await AuthManager.getToken();
        if (token != null) {
          _token = token;
        }
      }
      options.headers["token"] = _token;
    }
    return options;
  }

  @override
  Future onResponse(Response response) {
    var jsonData = response.data;
    // 若token已失效，则跳转回首页重新登录
    if (jsonData != null &&jsonData["msg"] == GlobalConfig.TOKEN_EXPIRE_SERVER_MSG) {
      ConfigGlobal.navigatorKey.currentState
          .pushNamedAndRemoveUntil(moduleLogin, (route) => false);
    }
    return super.onResponse(response);
  }
}
