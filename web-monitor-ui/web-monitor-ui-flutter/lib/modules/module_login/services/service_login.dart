import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:web_monitor_app/common/net/api.dart';
import 'package:web_monitor_app/models/model_login_user.dart';
import 'package:web_monitor_app/models/model_profile.dart';

class ServiceLogin {
  /// 登录
  static login(BuildContext context, String username, String password) async {
    String _url = "/user/login";
    var _params = {
      "username": username,
      "password": password,
    };
    var res = await httpManager.httpPost(_url, params: _params);
    var data = await httpManager.getBizResultData(res);
    if (data != null) {
      var _loginUser = LoginUser(
        id: data["id"],
        username: data["username"],
        isAdmin: data["isAdmin"],
        token: data["token"],
      );
      Provider.of<ModelLoginUser>(context, listen: false).loginUser = _loginUser;
    }
  }
}