import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:web_monitor_app/core/notifier/model_login_user_change_notifier.dart';

class WidgetUserBlock extends StatefulWidget {
  @override
  _WidgetUserBlockState createState() => _WidgetUserBlockState();
}

class _WidgetUserBlockState extends State<WidgetUserBlock> {
  String _username = "";

  @override
  void initState() {
    super.initState();
    getUsername();
  }

  /// 获取用户名
  void getUsername() {
    var loginUser =
        Provider.of<ModelLoginUserChangeNotifier>(this.context, listen: false)
            .loginUser;
    setState(() {
      this._username = loginUser.username;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.all(16.0),
      padding: EdgeInsets.symmetric(
        vertical: 30.0,
        horizontal: 20.0,
      ),
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.all(Radius.circular(10.0)),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Row(
            children: [
              Icon(
                Icons.account_circle,
                size: 66.0,
              ),
              Padding(
                padding: EdgeInsets.only(left: 8.0),
                child: Text(
                  _username,
                  style: TextStyle(
                    fontSize: 20.0,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ],
          ),
          Icon(
            Icons.arrow_forward_ios,
            color: Colors.grey[500],
            size: 26.0,
          ),
        ],
      ),
    );
  }
}
