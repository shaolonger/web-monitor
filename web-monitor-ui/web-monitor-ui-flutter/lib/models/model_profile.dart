import 'dart:convert';

import 'package:web_monitor_app/models/model_login_setting.dart';
import 'package:web_monitor_app/models/model_user_info.dart';

import 'model_login_user.dart';

class ModelProfile {
  ModelLoginUser loginUser;
  ModelUserInfo userInfo;
  int theme;
  ModelLoginSetting loginSetting;

  ModelProfile({
    this.loginUser = const ModelLoginUser(),
    this.userInfo = const ModelUserInfo(),
    this.theme,
    this.loginSetting = const ModelLoginSetting(),
  });

  ModelProfile.fromJson(Map<String, dynamic> jsonStr) {
    this.loginUser = ModelLoginUser.fromJson(json.decode(jsonStr["loginUser"]));
    this.userInfo = ModelUserInfo.fromJson(json.decode(jsonStr["userInfo"]));
    this.theme = jsonStr["theme"];
    this.loginSetting =
        ModelLoginSetting.fromJson(json.decode(jsonStr["loginSetting"]));
  }

  Map<String, dynamic> toJson() => <String, dynamic>{
        "loginUser": json.encode(loginUser),
        "userInfo": json.encode(userInfo),
        "theme": theme,
        "loginSetting": json.encode(loginSetting),
      };
}
