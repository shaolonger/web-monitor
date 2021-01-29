import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:provider/provider.dart';
import 'package:web_monitor_app/models/model_theme.dart';
import 'package:web_monitor_app/modules/module_startup/module_startup.dart';
import 'package:web_monitor_app/routes/routes.dart';

class ModuleMain extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: ModelTheme()),
      ],
      child: Consumer<ModelTheme>(
        builder: (BuildContext context, ModelTheme modelTheme, Widget child) {
          return MaterialApp(
            title: 'web monitor',
            theme: ThemeData(
              primarySwatch: modelTheme.theme,
            ),
            // 路由表定义
            routes: routes,
            home: ModuleStartup(),
            builder: EasyLoading.init(),
          );
        },
      ),
    );
  }
}
