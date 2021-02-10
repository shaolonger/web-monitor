import 'package:shared_preferences/shared_preferences.dart';

/// 利用SharedPreferences插件，实现本地存储
class LocalStorage {

  /// 保存本地缓存
  static Future<bool> save(String key, String value) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.setString(key, value);
  }

  /// 获取本地缓存
  static Future<String> get(String key) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.get(key);
  }

  /// 移除本地缓存
  static void remove(String key) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.remove(key);
  }
}