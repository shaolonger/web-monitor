import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:web_monitor_app/core/config/config_global.dart';
import 'package:web_monitor_app/core/notifier/model_login_setting_change_notifier.dart';
import 'package:web_monitor_app/core/notifier/model_user_info_change_notifier.dart';
import 'package:web_monitor_app/models/model_login_setting.dart';
import 'package:web_monitor_app/routes/routes.dart';

class ModulePersonalInfo extends StatefulWidget {
  @override
  _ModulePersonalInfoState createState() => _ModulePersonalInfoState();
}

class _ModulePersonalInfoState extends State<ModulePersonalInfo> {
  List<Map<String, String>> _infoList = [];

  @override
  void initState() {
    super.initState();
    _setInfoList();
  }

  void _setInfoList() {
    final genderMap = {
      0: "未知",
      1: "男",
      2: "女",
    };
    var userInfo =
        Provider.of<ModelUserInfoChangeNotifier>(context, listen: false)
            .userInfo;
    if (userInfo != null) {
      setState(() {
        _infoList = [
          {
            "label": "用户名",
            "key": "username",
            "value": userInfo.username,
          },
          {
            "label": "电话",
            "key": "phone",
            "value": userInfo.phone,
          },
          {
            "label": "性别",
            "key": "gender",
            "value": genderMap[userInfo.gender],
          },
          {
            "label": "邮箱",
            "key": "email",
            "value": userInfo.email,
          },
          {
            "label": "注册时间",
            "key": "createTime",
            "value": userInfo.createTime,
          },
        ];
      });
    }
  }

  /// 退出
  void _logout() {
    // 退出后，则取消登录配置中的【自动登录】，否则退出返回登录页后，又触发自动登录
    var loginSetting = Provider.of<ModelLoginSettingChangeNotifier>(context, listen: false).loginSetting;
    if (loginSetting.isAutoLogin) {
      var newLoginSetting = ModelLoginSetting.fromExisted(loginSetting, isAutoLogin: false);
      Provider.of<ModelLoginSettingChangeNotifier>(context, listen: false)
          .loginSetting = newLoginSetting;
    }
    ConfigGlobal.navigatorKey.currentState
        .pushNamedAndRemoveUntil(moduleLogin, (route) => false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("个人信息"),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          children: [
            Icon(
              Icons.account_circle_rounded,
              size: 100.0,
              color: Colors.grey[400],
            ),
            ..._infoList.map((e) {
              return Container(
                padding: EdgeInsets.symmetric(vertical: 16.0),
                decoration: BoxDecoration(
                    border: Border(
                  bottom: BorderSide(
                    width: 1.0,
                    color: Colors.grey[200],
                  ),
                )),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      e["label"] ?? "",
                      style: TextStyle(
                        color: Colors.grey[500],
                      ),
                    ),
                    Text(
                      e["value"] ?? "",
                    ),
                  ],
                ),
              );
            }).toList(),
            Padding(
              padding: EdgeInsets.only(top: 30.0),
              child: SizedBox(
                width: double.infinity,
                child: RaisedButton(
                  child: Text(
                    "退出",
                    style: TextStyle(color: Colors.white),
                  ),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(Radius.circular(50.0)),
                  ),
                  color: Colors.blue,
                  onPressed: _logout,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
