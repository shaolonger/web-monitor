import 'package:flutter/material.dart';

class ModuleLogin extends StatefulWidget {
  @override
  _ModuleLoginState createState() => _ModuleLoginState();
}

class _ModuleLoginState extends State<ModuleLogin> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              "Web Monitor",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 34.0),
            ),
          ],
        ),
      ),
    );
  }
}