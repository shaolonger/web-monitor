import 'package:flutter/material.dart';
import 'package:web_monitor_app/common/Global.dart';
import 'package:web_monitor_app/common/model_profile_change_notifier.dart';

class ModelTheme extends ModelProfileChangeNotifier {
  // 获取当前主题，如果未设置主题，则默认使用蓝色主题
  ColorSwatch get theme => Global.themes.firstWhere(
      (element) => element.value == modelProfile.theme,
      orElse: () => Colors.blue);

  // 主题改变后，通知其依赖项，新主题会立即生效
  set theme(ColorSwatch colorSwatch) {
    if (colorSwatch != theme) {
      modelProfile.theme = colorSwatch[500].value;
      notifyListeners();
    }
  }
}
