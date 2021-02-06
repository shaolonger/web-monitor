import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:web_monitor_app/models/model_profile.dart';

// 五套可选的主题色
const _themes = <MaterialColor>[
  Colors.blue,
  Colors.cyan,
  Colors.teal,
  Colors.green,
  Colors.red,
];

class Global {
  static const _preferenceKey = "modelProfile";
  static SharedPreferences _preferences;
  static ModelProfile modelProfile = ModelProfile();

  /// 用于全局路由控制
  static final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

  /// 可选的主题列表
  static List<MaterialColor> get themes => _themes;

  /// 初始化全局信息，在APP启动时执行
  static Future init() async {
    _preferences = await SharedPreferences.getInstance();
    var _modelProfile = _preferences.getString(_preferenceKey);
    if (_modelProfile != null) {
      try {
        modelProfile = ModelProfile.fromJson(jsonDecode(_modelProfile));
      } catch (e) {
        print(e);
      }
    }
  }

  /// 保存Profile信息
  static saveModelProfile() {
    _preferences.setString(_preferenceKey, jsonEncode(modelProfile.toJson()));
  }
}