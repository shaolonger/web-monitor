import 'package:web_monitor_app/core/notifier/model_profile_change_notifier.dart';
import 'package:web_monitor_app/models/model_login_user.dart';
import 'package:web_monitor_app/models/model_user_info.dart';

class ModelLoginUserChangeNotifier extends ModelProfileChangeNotifier {
  ModelLoginUser get loginUser => modelProfile.loginUser;
  ModelUserInfo get userInfo => modelProfile.userInfo;

  // 若有用户信息，则表示已登录过
  bool get isLogin => loginUser != null;

  // 用户信息发生变化，则更新并通知所有子孙组件更新
  set loginUser(ModelLoginUser loginUser) {
    if (loginUser?.id != modelProfile.loginUser?.id) {
      modelProfile.loginUser = loginUser;
      notifyListeners();
    }
  }

  // 用户信息发生变化，则更新并通知所有子孙组件更新
  set userInfo(ModelUserInfo userInfo) {
    if (userInfo?.id != modelProfile.userInfo?.id) {
      modelProfile.userInfo = userInfo;
      notifyListeners();
    }
  }
}
