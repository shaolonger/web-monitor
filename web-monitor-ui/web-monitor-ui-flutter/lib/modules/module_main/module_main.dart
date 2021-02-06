import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:provider/provider.dart';
import 'package:web_monitor_app/config/global_config.dart';
import 'package:web_monitor_app/core/Global.dart';
import 'package:web_monitor_app/models/model_login_user.dart';
import 'package:web_monitor_app/models/model_theme.dart';
import 'package:web_monitor_app/modules/module_startup/module_startup.dart';
import 'package:web_monitor_app/routes/routes.dart';

class ModuleMain extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: ModelTheme()),
        ChangeNotifierProvider.value(value: ModelLoginUser()),
      ],
      child: Consumer<ModelTheme>(
        builder: (BuildContext context, ModelTheme modelTheme, Widget child) {
          return MaterialApp(
            title: GlobalConfig.APP_NAME_SHORT,
            theme: ThemeData(
              primarySwatch: modelTheme.theme,
            ),
            // 路由表定义
            routes: routes,
            navigatorKey: Global.navigatorKey,
            supportedLocales: [
              Locale("zh","CH"),
              Locale("en","US"),
            ],
            localizationsDelegates: [
              GlobalMaterialLocalizations.delegate,
            ],
            home: ModuleStartup(),
            builder: EasyLoading.init(),
          );
        },
      ),
    );
  }
}
