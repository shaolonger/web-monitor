import 'package:flutter/material.dart';
import 'package:web_monitor_app/modules/module_mine/module_mine.dart';
import 'package:web_monitor_app/modules/module_overview/module_overview.dart';
import 'package:web_monitor_app/modules/module_search/module_search.dart';
import 'package:web_monitor_app/routes/routes.dart';

class ModuleHome extends StatefulWidget {
  @override
  _ModuleHomeState createState() => _ModuleHomeState();
}

class _ModuleHomeState extends State<ModuleHome> {
  var _currentIndex = 0;
  var _pageController = PageController();
  final List<Map<String, Object>> _navigationItemList = [
    {
      "name": "总览",
      "icon": Icon(Icons.analytics),
      "routeName": moduleOverview,
      "widget": ModuleOverview(),
    },
    {
      "name": "查询",
      "icon": Icon(Icons.screen_search_desktop),
      "routeName": moduleHome,
      "widget": ModuleSearch(),
    },
    {
      "name": "我的",
      "icon": Icon(Icons.portrait),
      "routeName": moduleHome,
      "widget": ModuleMine(),
    },
  ];

  void _onBottomNavigationBarItemClick(int index) {
    _pageController.jumpToPage(index);
  }

  void _handlePageChange(int index) {
    setState(() {
      _currentIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_navigationItemList[_currentIndex]["name"]),
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _currentIndex,
        items: _navigationItemList
            .map((e) => BottomNavigationBarItem(
                  icon: e["icon"],
                  label: e["name"],
                ))
            .toList(),
        onTap: _onBottomNavigationBarItemClick,
      ),
      body: PageView(
        controller: _pageController,
        children:
            _navigationItemList.map((e) => e["widget"] as Widget).toList(),
        onPageChanged: _handlePageChange,
      ),
    );
  }
}
