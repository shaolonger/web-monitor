import 'package:flutter/material.dart';
import 'package:web_monitor_app/common/Global.dart';
import 'package:web_monitor_app/modules/module_main/module_main.dart';

void main() => Global.init().then((value) => runApp(ModuleMain()));