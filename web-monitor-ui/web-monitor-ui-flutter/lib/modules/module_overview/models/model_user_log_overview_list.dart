import 'package:web_monitor_app/modules/module_overview/models/model_project_log_overview.dart';

class ModelUserLogOverviewList {}

class ModelUserLogOverviewItem {
  String projectName;
  ModelProjectLogOverview data;

  ModelUserLogOverviewItem({
    this.projectName,
    this.data,
  });
}
