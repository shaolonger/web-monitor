import 'package:web_monitor_app/common/config/config.dart';
import 'package:web_monitor_app/common/local/local_storage.dart';
import 'package:web_monitor_app/common/net/url_address.dart';

class AuthManager {

  /// 无需token鉴权的请求白名单
  static final List<String> authIgnoreList = [
    UrlAddress.login,
  ];

  /// 判断是否无需token
  static bool checkIsNoNeedAuth(String url) {
    return authIgnoreList.any((element) => url.indexOf(element) != -1);
  }

  /// 从本地缓存中获取token
  static Future<String> getToken() async {
    return await LocalStorage.get(Config.TOKEN_KEY);
  }
}