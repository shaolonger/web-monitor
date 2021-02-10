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
  Future onResponse(Response response) async {
    var jsonData = response.data;
    if (jsonData != null) {
      // 登录后，将新的token存入本类成员变量_token中，更新token
      // ，避免_token缓存问题
      if (jsonData["data"] != null &&
          jsonData["data"] is Map &&
          jsonData["data"]["token"] != null) {
        String token = jsonData["data"]["token"];
        _token = token;
        await AuthManager.saveToken(token);
      }

      // 若token已失效，则跳转回首页重新登录
      if (jsonData["msg"] == GlobalConfig.TOKEN_EXPIRE_SERVER_MSG) {
        ConfigGlobal.navigatorKey.currentState
            .pushNamedAndRemoveUntil(moduleLogin, (route) => false);
      }
    }
    return super.onResponse(response);
  }
}
