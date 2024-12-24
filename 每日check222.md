## 今日讲解

#### 一 自我介绍

​		我是谁，从业几年，会哪些技术、平台，擅长什么？实时/离线，会什么语言，主要应用在哪



#### 二 项目介绍

​		我是谁，从业几年，会哪些技术、平台，擅长什么？实时/离线，会什么语言，主要应用在哪



### 三 实时：

​		数据源，pipline，target，ETL，ELT，ELK，计算指标，为谁服务，状态处理

ETL：用来描述将数据从来源端经过抽取（extract）转换（transform）、加载（load）至目的端的过程

ELT：先将原始数据从源系统提取并加载到目标系统（如大数据平台），然后再进行数据的转换和处理

ELK：由 **Elasticsearch**、**Logstash** 和 **Kibana** 组成的一套开源日志收集、分析和展示的解决方案

 



面试官您好，我叫许晓楠 从事大数据开发已经有五年之久了 技术能力有三方面，一是数据采集与传输，精通flume，logstash，kafka等主流工具 二是数据存储与管理熟练驾驭hdfs，三是数据处理与分析在离线层面熟练运用spark核心api，擅长云平台，自建大数据平台，项目实战能力0-1项目搭建，专注于金融风控，互联网营销领域的大数据应用，擅长处理实时交易和监控主要的编程语言为java和python。

之前从事公司的一个实时项目金融交易风险只能护盾 首先数据源包括json csv等结构化与半结构化类型，Pipeline基于flume定制的source适配各数据源接口，kafka集群分流不同业务线数据topic，flink实时消费kafka数据，运用cep处理复杂事件，实时计算交易风险评分，综合考虑交易行为特征，历史交易习惯。服务对象是大型的商业银行零售与对业务部门，保障数百万客户免受风险。

离线项目电商销售全景洞察引擎，整合电商平台订单数据库，商品库存系统，用户浏览行为日志等全量记录业务轨迹，sqoop周期性抽取关系型数据库数据至hive flume持续汇聚日志至hdfs hive构建星型模型数据仓库，分层etl加工清洗数据，为电商运营团队提供每日每周 每月销售报表，剖析各品类销量走势



今日任务 

晨读了hbase知识内容

java的四大特性

封装 继承 多态 抽象

 

 **1.开启服务器和端口代码**

systemctl start cloudera-scm-server（cdh01）
systemctl start cloudera-scm-agent（cdh01，cdh02，cdh03）

**如果开启失败**

查看log tail -f /var/log/cloudera-scm-server/cloudera-scm-server.log



停止代码

## stop
systemctl stop cloudera-scm-server
systemctl stop cloudera-scm-agent



 代码：

主类

![1734925239661](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734925239661.png)

![1734925251955](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734925251955.png)





封装工具类 利用工具类去编写MySQL数据导出

![1735002002417](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1735002002417.png)

![1735002134550](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1735002134550.png)

以上是hbase存储代码 加关闭流程序





效果

![1735002177110](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1735002177110.png)

![1735002220987](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1735002220987.png)

![1735002274037](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1735002274037.png)





下面打包上传到yarn执行