import 'package:web_monitor_app/core/notifier/model_profile_change_notifier.dart';
import 'package:web_monitor_app/models/model_login_setting.dart';

class ModelLoginSettingChangeNotifier extends ModelProfileChangeNotifier {
  ModelLoginSetting get loginSetting => modelProfile.loginSetting;

  // 更新并通知所有子孙组件更新
  set loginSetting(ModelLoginSetting loginSetting) {
    modelProfile.loginSetting = loginSetting;
    notifyListeners();
  }
}
