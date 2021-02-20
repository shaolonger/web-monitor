import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:provider/provider.dart';
import 'package:web_monitor_app/config/global_config.dart';
import 'package:web_monitor_app/core/config/config_global.dart';
import 'package:web_monitor_app/core/notifier/model_login_user_change_notifier.dart';
import 'package:web_monitor_app/core/notifier/model_theme_change_notifier.dart';
import 'package:web_monitor_app/core/notifier/model_user_info_change_notifier.dart';
import 'package:web_monitor_app/modules/module_startup/module_startup.dart';
import 'package:web_monitor_app/routes/routes.dart';

class ModuleMain extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: ModelThemeChangeNotifier()),
        ChangeNotifierProvider.value(value: ModelLoginUserChangeNotifier()),
        ChangeNotifierProvider.value(value: ModelUserInfoChangeNotifier()),
      ],
      child: Consumer<ModelThemeChangeNotifier>(
        builder: (BuildContext context, ModelThemeChangeNotifier modelTheme, Widget child) {
          return MaterialApp(
            title: GlobalConfig.APP_NAME_SHORT,
            theme: ThemeData(
              primarySwatch: modelTheme.theme,
            ),
            // 路由表定义
            routes: routes,
            navigatorKey: ConfigGlobal.navigatorKey,
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
