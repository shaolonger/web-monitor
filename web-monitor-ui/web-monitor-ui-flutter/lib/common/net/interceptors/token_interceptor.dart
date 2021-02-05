import 'package:dio/dio.dart';
import 'package:web_monitor_app/common/auth/auth_manager.dart';

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
      } else {
        // TODO 若请求的接口非无需token的白名单，则需要跳转登录页
      }
      options.headers["token"] = _token;
    }
    return options;
  }
}
