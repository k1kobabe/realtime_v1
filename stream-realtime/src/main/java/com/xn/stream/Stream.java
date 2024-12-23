package com.xn.stream;

import com.alibaba.fastjson.JSONObject;
import com.stream.common.utils.ConfigUtils;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.xn.stream.utils.MapUpdateHbaseDimTableFunc;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Stream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        MySqlSource<String> abc = FlinkCdctomysql.abc(
                ConfigUtils.getString("mysql.database"),
                ConfigUtils.getString("mysql.table"),
                ConfigUtils.getString("mysql.user"),
                ConfigUtils.getString("mysql.pwd"),
                StartupOptions.initial()

        );

        DataStreamSource<String> stream_db = env.fromSource(
                abc, WatermarkStrategy.noWatermarks(), "MysqlMainSource"
        );

        SingleOutputStreamOperator<JSONObject> cdcDbMainStreamMap = stream_db.map(JSONObject::parseObject)
                .uid("db_data_convert_json")
                .name("db_data_convert_json")
                .setParallelism(1);

        cdcDbMainStreamMap.print();

        // Read gmall_config
        MySqlSource<String> stream_config = FlinkCdctomysql.abc(
                ConfigUtils.getString("mysql.config_database"),
                "",
                ConfigUtils.getString("mysql.user"),
                ConfigUtils.getString("mysql.pwd"),
                StartupOptions.initial()
        );
//
        DataStreamSource<String> stream_dim = env.fromSource(
                stream_config, WatermarkStrategy.noWatermarks(), "MysqlDimSource"
        );

        SingleOutputStreamOperator<JSONObject> cdcDbDimStreamMap = stream_dim.map(JSONObject::parseObject)
                .uid("dim_data_convert_json")
                .name("dim_data_convert_json")
                .setParallelism(1);

        cdcDbDimStreamMap.print();


        SingleOutputStreamOperator<JSONObject> DimCleanColumMap = cdcDbDimStreamMap.map(t -> {

                    JSONObject resJson = new JSONObject();
                    if ("d".equals(t.getString("op"))) {
                        resJson.put("before", t.getJSONObject("before"));
                    } else {
                        resJson.put("after", t.getJSONObject("after"));
                    }
                    resJson.put("op", t.getString("op"));
                    return resJson;

                }).uid("clean_colum_map")
                .name("clean_colum_map");

        SingleOutputStreamOperator<JSONObject> DS = DimCleanColumMap.map(new MapUpdateHbaseDimTableFunc(ConfigUtils.getString("zookeeper.server.host.list"), ConfigUtils.getString("hbase.namespace")))
                .uid("Create_Hbase_Dim_table")
                .name("Create_Hbase_Dim_table");

        MapStateDescriptor<String, JSONObject> mapState = new MapStateDescriptor<>("mapState", String.class, JSONObject.class);
        BroadcastStream<JSONObject> broadcast = DS.broadcast(mapState);
        BroadcastConnectedStream<JSONObject, JSONObject> connectDs = cdcDbMainStreamMap.connect(broadcast);
        connectDs.process(new ProcessSpiltStreamToHBaseDim(mapState));
        env.execute();
    }
}
