import 'dart:convert';

import 'package:web_monitor_app/models/model_user_info.dart';

import 'model_login_user.dart';

class ModelProfile {
  ModelLoginUser loginUser;
  ModelUserInfo userInfo;
  int theme;

  ModelProfile({
    this.loginUser,
    this.userInfo,
    this.theme,
  });

  ModelProfile.fromJson(Map<String, dynamic> jsonStr)
      : loginUser = json.decode(jsonStr["loginUser"]),
        userInfo = json.decode(jsonStr["userInfo"]),
        theme = jsonStr["theme"];

  Map<String, dynamic> toJson() => <String, dynamic>{
        "loginUser": json.encode(loginUser),
        "userInfo": json.encode(userInfo),
        "theme": theme,
      };
}
