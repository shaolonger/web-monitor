import 'package:web_monitor_app/common/model_profile_change_notifier.dart';
import 'package:web_monitor_app/models/model_profile.dart';

class ModelLoginUser extends ModelProfileChangeNotifier {
  LoginUser get loginUser => modelProfile.loginUser;

  // 若有用户信息，则表示已登录过
  bool get isLogin => loginUser != null;

  // 用户信息发生变化，则更新并通知所有子孙组件更新
  set loginUser(LoginUser loginUser) {
    if (loginUser?.id != modelProfile.loginUser?.id) {
      modelProfile.loginUser = loginUser;
      notifyListeners();
    }
  }
}
