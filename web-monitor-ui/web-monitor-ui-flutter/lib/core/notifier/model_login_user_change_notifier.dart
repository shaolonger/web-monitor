import 'package:web_monitor_app/core/notifier/model_profile_change_notifier.dart';
import 'package:web_monitor_app/models/model_login_user.dart';

class ModelLoginUserChangeNotifier extends ModelProfileChangeNotifier {
  ModelLoginUser get loginUser => modelProfile.loginUser;

  // 若有用户信息，则表示已登录过
  bool get isLogin => loginUser != null;

  // 用户信息发生变化，则更新并通知所有子孙组件更新
  set loginUser(ModelLoginUser loginUser) {
    if (loginUser?.id != modelProfile.loginUser?.id) {
      modelProfile.loginUser = loginUser;
      notifyListeners();
    }
  }
}
