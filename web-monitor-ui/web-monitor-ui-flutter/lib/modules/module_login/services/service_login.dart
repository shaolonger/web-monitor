import 'package:web_monitor_app/common/net/api.dart';
import 'package:web_monitor_app/models/model_login_user.dart';

class ServiceLogin {
  /// 登录
  static Future<ModelLoginUser> login(String username, String password) async {
    String _url = "/user/login";
    var _params = {
      "username": username,
      "password": password,
    };
    var res = await httpManager.httpPost(_url, params: _params);
    return res;
  }
}