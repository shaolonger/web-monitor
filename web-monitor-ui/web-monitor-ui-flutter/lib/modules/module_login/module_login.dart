import 'package:flutter/material.dart';
import 'package:web_monitor_app/config/environment_config.dart';

class ModuleLogin extends StatefulWidget {
  @override
  _ModuleLoginState createState() => _ModuleLoginState();
}

class _ModuleLoginState extends State<ModuleLogin> {
  final String _environment = EnvironmentConfig.ENV;

  var _username = TextEditingController();
  var _password = TextEditingController();
  var _formKey = GlobalKey<FormState>();

  /// 登录
  void _login() {
    if ((_formKey.currentState).validate()) {
      print("username: ${_username.text}");
      print("password: ${_password.text}");
    }
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
                  "Web Monitor",
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
