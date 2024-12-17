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



### 离线：

​		SQL，复杂指标，数据建模方法论，设计优化，组件对比，对接报表，指标平台



面试官您好，我叫许晓楠 从事大数据开发已经有五年之久了 技术能力有三方面，一是数据采集与传输，精通flume，logstash，kafka等主流工具 二是数据存储与管理熟练驾驭hdfs，三是数据处理与分析在离线层面熟练运用spark核心api，擅长云平台，自建大数据平台，项目实战能力0-1项目搭建，专注于金融风控，互联网营销领域的大数据应用，擅长处理实时交易和监控主要的编程语言为java和python。

之前从事公司的一个实时项目金融交易风险只能护盾 首先数据源包括json csv等结构化与半结构化类型，Pipeline基于flume定制的source适配各数据源接口，kafka集群分流不同业务线数据topic，flink实时消费kafka数据，运用cep处理复杂事件，实时计算交易风险评分，综合考虑交易行为特征，历史交易习惯。服务对象是大型的商业银行零售与对业务部门，保障数百万客户免受风险。

离线项目电商销售全景洞察引擎，整合电商平台订单数据库，商品库存系统，用户浏览行为日志等全量记录业务轨迹，sqoop周期性抽取关系型数据库数据至hive flume持续汇聚日志至hdfs hive构建星型模型数据仓库，分层etl加工清洗数据，为电商运营团队提供每日每周 每月销售报表，剖析各品类销量走势



今日任务 

完成dws传输数据，再通过dws层数据传入到doris中

1.建表

![1734398928660](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734398928660.png)





![1734399043362](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734399043362.png)

![1734399055956](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734399055956.png)

![1734399064126](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734399064126.png)

以下是查询的表数据效果

![img](file:///E:\下载\Tencent Files\Tencent Files\2845339829\nt_qq\nt_data\Pic\2024-12\Thumb\75458ec3fa154c0457e7d41744f3549a_720.png)

