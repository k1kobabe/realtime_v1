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

晨读了hdfs知识内容

java的四大特性

封装 继承 多态 抽象

java的数据结构

数组 列表 集合 映射 栈和队列 堆 树 图

HDFS由客户端、名称节点、数据节点辅助名称节点组成；Cient负责响应用户的各种请求比如上
传、下载等、NameNode负责存储HDFS的元数据和处理用户的读写请求，比如数据块存储在数据节点的哪个地方；数据节点负责存储实际的数据块和数据的读写功能，辅助名称节点主要是辅助名称节点，分相其T
作量；定期合并fimage和fsedits，推送给名称节点；在紧急情况下，可辅助恢复名称节点

hdfs小文件问题

入库前:数据采集或标准入库之前，将小文件进行合并大文件再上传入库
存储: Hadoop Archive归档->将多个小文件打包成一个HAR文件，减少对NN内存的使用
计算方面: CombineTextInputFormat用于将多个小文件在切片过程中生成一个单独的切片或者少量的切片



压缩格式

gzip压缩:压缩率比较高，而且压缩/解压速度也比较快;但
是不支持split。当每个文件压缩之后在130M以内的(1个块大
小内)，都可以考虑用gzip压缩格式
lzo压缩:压缩/解压速度也比较快，合理的压缩率;支持
split，是hadoop中最流行的压缩格式。一个很大的文本文
件，压缩之后还大于200M以上的可以考虑，而且单个文件越
大，Izo优点越越明显
snappy压缩:高速压缩速度和合理的压缩率;不支持split;
压缩率比gzip要低。当mapreduce作业的map输出的数据比较
大的时候，作为map到reduce的中间数据的压缩格式;或者
作为一个mapreduce作业的输出和另外一个mapreduce作业
的输入。
bzip2压缩:支持split;具有很高的压缩率，比gzip压缩率都
高;压缩/解压速度慢。适合对速度要求不高，但需要较高的
压缩率的时候，可以作为mapreduce作业的输出格式。

 **1.开启服务器和端口代码**

systemctl start cloudera-scm-server（cdh01）
systemctl start cloudera-scm-agent（cdh01，cdh02，cdh03）

**如果开启失败**

查看log tail -f /var/log/cloudera-scm-server/cloudera-scm-server.log



停止代码

## stop
systemctl stop cloudera-scm-server
systemctl stop cloudera-scm-agent



 

**编写日志代码**

![1735088109354](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1735088109354.png)

1.  获取执行环境 
2. 配置mysql数据源
3. 创建基于myysql源的数据流

![1735088121091](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1735088121091.png)

1.数据格式转换与操作配置

**数据转换**：通过调用 `map` 方法并传入 `JSONObject::parseObject`，意味着对从之前创建的 `stream_db` 这个数据流中的每一条字符串类型的数据（因为 `stream_db` 是 `DataStreamSource<String>` 类型）进行解析，尝试将其转换为 `JSONObject` 格式。

2.维度数据格式转换与输出打印

**数据转换**：和之前对主数据的处理类似，通过 `map` 操作调用 `JSONObject::parseObject` 方法将 `stream_dim` 数据流（从代码上下文推测应该也是从 MySQL 读取的维度相关数据的流，不过此处没有展示其创建过程）中的数据转换为 `JSONObject` 格式，方便后续以 JSON 对象形式进行处理。同样设置了唯一标识符、可读名称以及并行度为 1，作用与之前介绍的对应设置一致，便于任务管理与执行控制



**数据打印**：使用 `print` 方法将转换后的维度数据打印输出，用于调试查看维度数据转换后的内容是否符合预期，不过同样要考虑如之前提到的在生产环境中打印操作的性能影响及输出信息筛选等问题

3.唯独数据列清洗操作





目前进程还是在解决报错问题 没有效果