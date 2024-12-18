**1.编写dwd代码到kafka**

效果：

![1734269657132](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734269657132.png)





代码：![1734269689474](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734269689474.png)![1734269704880](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734269704880.png)



![1734269716668](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734269716668.png)

![1734269736728](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734269736728.png)

这个表示订单取消表 以这个为例子

首先定义一个main方法 是java程序入口点

在main方法中创建了DwdTtradeOrderCancelDetai类的实例

通过调用new DwdTtradeOrderCancelDetai（）

然后调用这个实例 的start方法 传入了三个参数



之后实现了handle的公共方法 接收了三个参数 分别是env tableenv String类型的groupid实现了一系列的数据处理操作，经过过滤 关联 最终处理好的数据写入kafka的主题



接下来定义私有静态方法 extracted1 接受了一个streamtableenvironment类型的参数 tableenv

执行了一条sql语句 在streamtableenvironment创建了一个叫constant.topic_dwd_trade_order_cancel的表并配置了相关数据结构以及kafka作为数据输出端 目的是写入对应的kafka主题中





**2.dws打印到控制台**

![1734270529195](C:\Users\许晓楠\AppData\Roaming\Typora\typora-user-images\1734270529195.png)

在main方法中创建了DwsTtradeCartAddUuWindow类的实例

通过调用new DwsTtradeCartAddUuWindow（）

然后调用这个实例 的start方法 传入了三个参数



然后使用handle方法定义了一套完整的数据处理流程 接受流处理执行环境以及原始数据输入流作为参数 依次对数据进行清洗 添加水位线 分组聚合 开窗聚合 最后将处理好的数据通过相应的映射和写入配置 存入到doris数据库（现在只是到后台程序当中）



