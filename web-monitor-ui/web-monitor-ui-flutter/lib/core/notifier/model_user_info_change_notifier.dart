import 'package:web_monitor_app/core/notifier/model_profile_change_notifier.dart';
import 'package:web_monitor_app/models/model_user_info.dart';

class ModelUserInfoChangeNotifier extends ModelProfileChangeNotifier {
  ModelUserInfo get userInfo => modelProfile.userInfo;

  // 用户信息发生变化，则更新并通知所有子孙组件更新
  set userInfo(ModelUserInfo userInfo) {
    if (userInfo?.id != modelProfile.userInfo?.id) {
      modelProfile.userInfo = userInfo;
      notifyListeners();
    }
  }
}
