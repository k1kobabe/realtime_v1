package com.bw.dwd.app;

import com.bw.common.Constant;
import com.bw.utils.SQLUtil;
import lombok.SneakyThrows;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Date;

public class DwdTradeOrderDetailApp {


    @SneakyThrows
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        readOdsDb1(tableEnv,"xh" + new Date().getTime());
        filterOrderDetailInfo(tableEnv);
        filterOrderInfo(tableEnv);
        filterOrderActivity(tableEnv);
        filterOrderCoupon(tableEnv);
        Table table = OrderJoin(tableEnv);
        createKafkaSinkTable(tableEnv);
//        table.insertInto(Constant.TOPIC_DWD_TRADE_ORDER_DETAIL);
        tableEnv.toDataStream(table).print();
    }


    public static void readOdsDb1(StreamTableEnvironment tableEnv,String groupId){
        tableEnv.executeSql(SQLUtil.getKafkaTopicDb(groupId));
    }

    private static void filterOrderDetailInfo(StreamTableEnvironment tableEnv) {
        Table odTable = tableEnv.sqlQuery("select \n" +
                "  `data`['id'] id, \n" +
                "  `data`['order_id'] order_id, \n" +
                "  `data`['sku_id'] sku_id, \n" +
                "  `data`['sku_name'] sku_name, \n" +
                "  `data`['order_price'] order_price, \n" +
                "  `data`['sku_num'] sku_num, \n" +
                "  `data`['create_time'] create_time, \n" +
                "  `data`['split_total_amount'] split_total_amount, \n" +
                "  `data`['split_activity_amount'] split_activity_amount, \n" +
                "  `data`['split_coupon_amount'] split_coupon_amount, \n" +
                "  ts\n" +
                "from topic_db\n" +
                "where `database`='gmall'\n" +
                "and `table`='order_detail'\n" +
                "and `type` in ('bootstrap-insert','insert')");
        tableEnv.createTemporaryView("order_detail_info", odTable);
    }

    private static void filterOrderInfo(StreamTableEnvironment tableEnv) {
        Table oiTable = tableEnv.sqlQuery("select \n" +
                "  `data`['id'] id, \n" +
                "  `data`['user_id'] user_id, \n" +
                "  `data`['province_id'] province_id \n" +
                "from topic_db\n" +
                "where `database`='gmall'\n" +
                "and `table`='order_info'\n" +
                "and `type` in ('bootstrap-insert','insert')");
        tableEnv.createTemporaryView("order_info", oiTable);
    }

    private static void filterOrderActivity(StreamTableEnvironment tableEnv) {
        Table odaTable = tableEnv.sqlQuery("select \n" +
                "  `data`['order_detail_id'] id, \n" +
                "  `data`['activity_id'] activity_id, \n" +
                "  `data`['activity_rule_id'] activity_rule_id\n" +
                "from topic_db\n" +
                "where `database`='gmall'\n" +
                "and `table`='order_detail_activity'\n" +
                "and `type` in ('bootstrap-insert','insert')");
        tableEnv.createTemporaryView("order_detail_activity", odaTable);

    }

    private static void filterOrderCoupon(StreamTableEnvironment tableEnv) {
        Table odcTable = tableEnv.sqlQuery("select \n" +
                "  `data`['order_detail_id'] id, \n" +
                "  `data`['coupon_id'] coupon_id\n" +
                "from topic_db\n" +
                "where `database`='gmall'\n" +
                "and `table`='order_detail_coupon'\n" +
                "and `type` in ('bootstrap-insert','insert')");
        tableEnv.createTemporaryView("order_detail_coupon", odcTable);
    }

    private static Table OrderJoin(StreamTableEnvironment tableEnv) {
        return tableEnv.sqlQuery("select \n" +
                "  od.id,\n" +
                "  order_id,\n" +
                "  sku_id,\n" +
                "  user_id,\n" +
                "  province_id,\n" +
                "  activity_id,\n" +
                "  activity_rule_id,\n" +
                "  coupon_id,\n" +
                "  sku_name,\n" +
                "  order_price,\n" +
                "  sku_num,\n" +
                "  create_time,\n" +
                "  split_total_amount,\n" +
                "  split_activity_amount,\n" +
                "  split_coupon_amount,\n" +
                "  ts \n" +
                "from order_detail_info od\n" +
                "join order_info oi\n" +
                "on od.order_id = oi.id\n" +
                "left join order_detail_activity oda\n" +
                "on oda.id = od.id\n" +
                "left join order_detail_coupon odc\n" +
                "on odc.id = od.id ");
    }

    private static void createKafkaSinkTable(StreamTableEnvironment tableEnv) {
        if (!isValidPrimaryKey(tableEnv, "id")) {
            throw new IllegalArgumentException("Invalid primary key configuration for Kafka sink table.");
        }
        tableEnv.executeSql("create table " + Constant.TOPIC_DWD_TRADE_ORDER_DETAIL + " (\n" +
                "  id  STRING,\n" +
                "  order_id  STRING,\n" +
                "  sku_id  STRING,\n" +
                "  user_id  STRING,\n" +
                "  province_id  STRING,\n" +
                "  activity_id  STRING,\n" +
                "  activity_rule_id  STRING,\n" +
                "  coupon_id  STRING,\n" +
                "  sku_name  STRING,\n" +
                "  order_price  STRING,\n" +
                "  sku_num  STRING,\n" +
                "  create_time  STRING,\n" +
                "  split_total_amount  STRING,\n" +
                "  split_activity_amount  STRING,\n" +
                "  split_coupon_amount  STRING,\n" +
                "  ts bigint,\n" +
                "  PRIMARY KEY (id) NOT ENFORCED \n" +
                ")" + SQLUtil.getUpsertKafkaSinkSQL(Constant.TOPIC_DWD_TRADE_ORDER_DETAIL));
        //因为有回撤流，所以要使用upsert-kafka 并且要指定主键
    }

    private static boolean isValidPrimaryKey(StreamTableEnvironment tableEnv, String primaryKey) {
        // 这里可以编写逻辑，比如查询数据验证主键的唯一性等，简单示例如下（可能不准确，需根据实际情况完善）
        Table validationTable = tableEnv.sqlQuery("SELECT COUNT(DISTINCT " + primaryKey + ") as unique_count, COUNT(*) as total_count FROM order_detail_info");
        // 执行查询并获取结果，这里简化处理，实际可能需要更严谨地获取和判断结果集
//        validationTable.execute().print();
        // 假设验证逻辑是唯一计数等于总计数则认为主键有效，实际要按业务准确判断
        return true;
    }

}
