import 'package:flutter/material.dart';
import 'package:web_monitor_app/models/model_project_info.dart';
import 'package:web_monitor_app/modules/module_project_manage/services/service_project_manage.dart';
import 'package:web_monitor_app/modules/module_project_manage/widgets/widget_project_item.dart';

class ModuleProjectManage extends StatefulWidget {
  @override
  _ModuleProjectManageState createState() => _ModuleProjectManageState();
}

class _ModuleProjectManageState extends State<ModuleProjectManage> {
  List<ModelProjectInfo> _list = [];

  @override
  void initState() {
    super.initState();
    _setList();
  }

  /// 获取用户关联的项目列表
  void _setList() async {
    var list = await ServiceProjectManage.getRelatedProjectList(context);
    setState(() {
      _list = list;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("项目管理"),
      ),
      body: ListView(
        padding: EdgeInsets.symmetric(vertical: 10.0),
        children: _list.map((e) => WidgetProjectItem(projectInfo: e)).toList(),
      ),
    );
  }
}
