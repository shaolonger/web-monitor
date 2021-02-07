import 'dart:convert';

import 'model_login_user.dart';

class ModelProfile {
  ModelLoginUser loginUser;
  int theme;

  ModelProfile({
    this.loginUser,
    this.theme,
  });

  ModelProfile.fromJson(Map<String, dynamic> jsonStr)
      : loginUser = json.decode(jsonStr["loginUser"]),
        theme = jsonStr["theme"];

  Map<String, dynamic> toJson() => <String, dynamic>{
        "loginUser": json.encode(loginUser),
        "theme": theme,
      };
}
