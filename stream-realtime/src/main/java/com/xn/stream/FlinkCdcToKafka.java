package com.xn.stream;


import com.stream.common.utils.ConfigUtils;
import com.stream.common.utils.KafkaUtils;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkCdcToKafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        MySqlSource<String> mySqlCdcSource = MySqlSource.<String>builder()
                .hostname(ConfigUtils.getString("mysql.host"))
                .port(3306)
                .databaseList(ConfigUtils.getString("mysql.database"))
                .tableList("")
                .username(ConfigUtils.getString("mysql.user"))
                .password(ConfigUtils.getString("mysql.pwd"))
//              .serverTimeZone(ConfigUtils.getString("mysql.timezone"))
                .deserializer(new JsonDebeziumDeserializationSchema())
                .startupOptions(StartupOptions.latest())
                .includeSchemaChanges(true)
                .build();

        DataStreamSource<String> cdcStream = env.fromSource(mySqlCdcSource, WatermarkStrategy.noWatermarks(), "mysql_cdc_source");
        cdcStream.print();

        cdcStream.sinkTo(
                KafkaUtils.buildKafkaSink(
                        "cdh01:9092,cdh02:9092,cdh03:9092",
                        "test3"
                )
        ).uid("sink_to_kafka_realtime_v1_mysql_db")
         .name("sink_to_kafka_realtime_v1_mysql_db");




        env.execute();
    }
}
