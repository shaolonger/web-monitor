import 'package:flutter/material.dart';
import 'package:web_monitor_app/common/Global.dart';
import 'package:web_monitor_app/models/model_profile.dart';

class ModelProfileChangeNotifier extends ChangeNotifier {
  ModelProfile get modelProfile => Global.modelProfile;

  @override
  void notifyListeners() {
    Global.saveModelProfile();
    super.notifyListeners();
  }
}