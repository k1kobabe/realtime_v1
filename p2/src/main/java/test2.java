import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

public class test2 {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setParallelism(1);
//        // 设置 3s 的 checkpoint 间隔
//        env.enableCheckpointing(3000);
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("192.168.10.103")
                .port(3306)
                .databaseList("xxn") // 设置捕获的数据库， 如果需要同步整个数据库，请将 tableList 设置为 ".*".
                .tableList("xxn.teacher") // 设置捕获的表
                .username("root")
                .password("root")
//                .startupOptions(StartupOptions.latest())
//                .startupOptions(StartupOptions.earliest())
                .startupOptions(StartupOptions.initial())
                .deserializer(new JsonDebeziumDeserializationSchema()) // 将 SourceRecord 转换为 JSON 字符串
                .build();
        DataStreamSource<String> ds1 = env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source");
        ds1.print();


        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.10.101:9092");
        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<>(
                "test3",             // target topic
                new SimpleStringSchema(),    // serialization schema
                properties); // fault-tolerance

        ds1.addSink(myProducer);


        try {
            env.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
