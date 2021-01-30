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
    var res = await httpManager.bizHttpPost(
      _url,
      params: _params,
      defaultErrorTip: "登录失败",
    );
    if (res.success) {
      var _loginUser = LoginUser(
        id: res.data?.id,
        username: res.data?.username,
        isAdmin: res.data?.isAdmin,
        token: res.data?.token,
      );
      Provider.of<ModelLoginUser>(context, listen: false).loginUser =
          _loginUser;
    }
  }
}
