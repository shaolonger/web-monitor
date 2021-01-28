import 'package:flutter/material.dart';
import 'package:web_monitor_app/common/Global.dart';
import 'package:web_monitor_app/modules/module_main/module_main.dart';

void main() {
  // 注意，在main里调用async函数需要首先执行下面这行
  // 参考文章：https://stackoverflow.com/questions/57689492/flutter-unhandled-exception-servicesbinding-defaultbinarymessenger-was-accesse
  WidgetsFlutterBinding.ensureInitialized();

  Global.init().then((value) => runApp(ModuleMain()));
}
