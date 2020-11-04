#!/bin/bash

# 这里可替换为你自己的执行程序，其他代码无需更改
# 应用组名
group_name='web'
# 应用名称
app_name='web-monitor-ui'
# 应用版本
app_version='1.0.0'
# 应用运行端口
app_port='9001'
# 应用在服务器上的部署地址
deploy_server_location='/home/ubuntu/web-monitor-ui'
# docker构建的镜像名称
docker_image_name=${group_name}/${app_name}-${app_version}
# docker启动的容器名称
docker_container_name=${group_name}__${app_name}

# 停止当前正在运行的docker容器
docker stop ${app_name}
echo '----stop container success----'

# 删除当前正在运行的docker容器
docker rm ${docker_container_name}
echo '----rm container success----'

# 删除当前正在运行的docker镜像
docker rmi ${docker_image_name}
echo '----rmi image success----'

# 打包编译docker镜像
docker build -t ${docker_image_name} . -f Dockerfile.multi --force-rm --network=host
echo '----build image success----'

# 删除多阶段构建(multi-stage build)的中间镜像
docker rmi $(docker images -q -f dangling=true)
echo '----rmi multi-stage build images success----'

# 启动一个新的容器
docker run -d --name ${docker_container_name} -p ${app_port}:80 ${docker_image_name}
