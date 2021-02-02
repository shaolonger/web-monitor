import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:web_monitor_app/common/net/api.dart';
import 'package:web_monitor_app/common/net/url_address.dart';
import 'package:web_monitor_app/models/model_login_user.dart';
import 'package:web_monitor_app/models/model_profile.dart';
import 'package:web_monitor_app/routes/routes.dart';

class ServiceLogin {
  /// 登录
  static void login(BuildContext context, String username, String password) async {
    var _params = {
      "username": username,
      "password": password,
    };
    var res = await httpManager.bizHttpPost(
      UrlAddress.login,
      params: _params,
      defaultErrorTip: "登录失败",
    );
    if (res.success) {
      var _loginUser = LoginUser.fromJson(res.data);
      Provider.of<ModelLoginUser>(context, listen: false).loginUser =
          _loginUser;
      Navigator.pushReplacementNamed(context, moduleHome);
    }
  }
}
