import 'package:flutter/material.dart';
import 'package:web_monitor_app/routes/routes.dart';

class ModuleStartup extends StatefulWidget {
  @override
  _ModuleStartupState createState() => _ModuleStartupState();
}

class _ModuleStartupState extends State<ModuleStartup> {
  @override
  void initState() {
    super.initState();
    autoNavigate();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              "Web Monitor App",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 34.0),
            ),
            Padding(
              padding: EdgeInsets.only(
                top: 30.0,
                left: 20.0,
                right: 20.0,
              ),
              child: Text(
                "A monitor platform for both mobile and desktop web applications",
                textAlign: TextAlign.center,
              ),
            ),
          ],
        ),
      ),
    );
  }

  /// 自动跳转总览页
  void autoNavigate() {
    Future.delayed(Duration(seconds: 2)).then((value) {
      Navigator.pushReplacementNamed(this.context, moduleLogin);
    });
  }
}
