import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:web_monitor_app/config/environment_config.dart';
import 'package:web_monitor_app/config/global_config.dart';
import 'package:web_monitor_app/core/notifier/model_login_setting_change_notifier.dart';
import 'package:web_monitor_app/models/model_login_setting.dart';
import 'package:web_monitor_app/modules/module_login/services/service_login.dart';

class ModuleLogin extends StatefulWidget {
  @override
  _ModuleLoginState createState() => _ModuleLoginState();
}

class _ModuleLoginState extends State<ModuleLogin> {
  final String _environment = EnvironmentConfig.ENV;
  bool _isRememberPassword = false;
  bool _isAutoLogin = false;
  var _username = TextEditingController();
  var _password = TextEditingController();
  var _formKey = GlobalKey<FormState>();

  /// 获取登录配置
  void _getLoginSetting() {
    var loginSetting =
        Provider.of<ModelLoginSettingChangeNotifier>(context, listen: false)
            .loginSetting;
    setState(() {
      _isRememberPassword = loginSetting.isRememberPassword;
      _isAutoLogin = loginSetting.isAutoLogin;
    });
    // 若选择了【记住密码】，则回显账号密码
    if (loginSetting.isRememberPassword) {
      _username.text = loginSetting.username;
      _password.text = loginSetting.password;
    }
    // 若选择了【自动登录】，则直接登录
    if (loginSetting.isAutoLogin) {
      // 这里用延时是因为需要等待上面的setState生效
      Future.delayed(Duration.zero).then((value) => _login());
    }
  }

  /// 保存登录配置
  void _saveLoginSetting() {
    var loginSetting =
        Provider.of<ModelLoginSettingChangeNotifier>(context, listen: false)
            .loginSetting;
    var newLoginSetting = ModelLoginSetting.fromExisted(
      loginSetting,
      isRememberPasswordNew: _isRememberPassword,
      isAutoLogin: _isAutoLogin,
      username: _username.text,
      password: _password.text,
    );
    Provider.of<ModelLoginSettingChangeNotifier>(context, listen: false)
        .loginSetting = newLoginSetting;
  }

  /// 保存【记住密码】
  void _saveIsRememberPassword(bool value) {
    setState(() {
      _isRememberPassword = value;
    });
    _saveLoginSetting();
  }

  /// 保存【记住密码】
  void _saveIsAutoLogin(bool value) {
    setState(() {
      _isAutoLogin = value;
    });
    _saveLoginSetting();
  }

  /// 登录
  void _login() {
    if ((_formKey.currentState).validate()) {
      ServiceLogin.login(this.context, _username.text, _password.text)
          .then((success) {
        if (success) {
          // 登录成功后，保存登录配置
          _saveLoginSetting();
        }
      });
    }
  }

  @override
  void initState() {
    super.initState();
    _getLoginSetting();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  GlobalConfig.APP_NAME_SHORT,
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 34.0),
                ),
                Padding(
                  padding: EdgeInsets.all(20.0),
                  child: FractionallySizedBox(
                    widthFactor: 0.9,
                    child: Form(
                      key: _formKey,
                      child: Column(
                        children: [
                          TextFormField(
                            controller: _username,
                            decoration: InputDecoration(
                              labelText: "用户名",
                              hintText: "请输入用户名",
                              icon: Icon(Icons.person),
                            ),
                            validator: (v) {
                              return v.trim().length > 0 ? null : "用户名不能为空";
                            },
                          ),
                          TextFormField(
                            controller: _password,
                            obscureText: true,
                            decoration: InputDecoration(
                              labelText: "密码",
                              hintText: "请输入密码",
                              icon: Icon(Icons.lock),
                            ),
                            validator: (v) {
                              return v.trim().length > 0 ? null : "密码不能为空";
                            },
                          ),
                          Padding(
                            padding: EdgeInsets.only(top: 16.0),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                              children: [
                                Row(
                                  children: [
                                    Checkbox(
                                      value: _isRememberPassword,
                                      onChanged: (value) =>
                                          _saveIsRememberPassword(value),
                                    ),
                                    Text("记住密码"),
                                  ],
                                ),
                                Row(
                                  children: [
                                    Checkbox(
                                      value: _isAutoLogin,
                                      onChanged: (value) =>
                                          _saveIsAutoLogin(value),
                                    ),
                                    Text("自动登录"),
                                  ],
                                ),
                              ],
                            ),
                          ),
                          SizedBox(
                            width: double.infinity,
                            child: Padding(
                              padding: EdgeInsets.only(top: 40.0),
                              child: RaisedButton(
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.all(
                                    Radius.circular(50.0),
                                  ),
                                ),
                                color: Colors.blue,
                                textColor: Colors.white,
                                padding: EdgeInsets.all(15.0),
                                child: Text("登录"),
                                onPressed: _login,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
          Positioned.fill(
            bottom: 10.0,
            child: Align(
              alignment: Alignment.bottomCenter,
              child: Text("当前运行环境：$_environment"),
            ),
          ),
        ],
      ),
    );
  }
}
