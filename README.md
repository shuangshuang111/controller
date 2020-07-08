# 电商项目单机版

电商后台管理系统，用于管理配置用户端电商项目，辅助电商项目正常运营。

## 后端结构

> 基于JDK1.8、Maven3、SSM、MySQL、Elasticsearch、Redis开发，通过swagger2生产接口文档，Tomcat8.5部署运行。

![manger-web](F:\work\dn\vip\8-大型电商项目实战\project\dongnao-mall-standalone\README.assets\manger-web.png)

## 外部依赖

准备好这些服务：MySQL、Redis、ElasticSearch、FastDFS

MySQL、Redis、ElasticSearch安装参考前专题内容

FastDFS安装，参考[博客地址](https://blog.csdn.net/m0_37797991/article/details/73381648)



## 前端技术

前台页面为基于h-ui静态模板 + JSP实现



## 本地开发运行

- 下载zip直接解压或安装git后执行克隆命令

- 安装运行外部依赖组件，参考mall-dev-evn的内容

- 建立数据库，运行数据库脚本sql文件

  工程根目录下的：doc/dn_mall_standalone.sql

  ElasticSearch脚本：doc/item_index_mapping.json

- 将源码导入IDE

- 修改配置及相应依赖的配置

- Maven编译打包

- 部署在Tomcat中

- 访问项目





