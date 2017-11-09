# sosoDbApi
基于sosoApi源码改写\n
支持多数据源配置
支持直接配置数据库地址账号密码即可获取对应的resful格式的API\n

照之前的README搭建环境
数据库在db文件夹下，我测试的库是bootdo

修改api-cfg.properties文件
#需要生成API数据库名
DB_NAME=bootdo
#需要生成API数据库描述
DB_DESC=获取不同的数据源API测试
#需要生成API数据源名称
DB_SOURCE=apiDataSource
#需要生成API数据库地址
jdbc.api.url=jdbc:mysql://localhost:3306/bootdo
jdbc.api.username=root
jdbc.api.password=root

#需要生成API服务监听端口
监听器路径com.dev.base.listener.AutoResListener
api.port=8081

#多数据源配置
在api-cfg.properties增加一个数据源的配置
修改spring-mybatis.xml文件
复制上面的bonecpDataSource修改成对应的数据源名称，参照apiDataSource
如何使用
在service调用之前，设置如下代码
DataSourceContextHolder.setCurrent(数据源名称);



