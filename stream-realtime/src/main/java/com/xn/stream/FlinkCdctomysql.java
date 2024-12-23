package com.xn.stream;

import com.stream.common.utils.ConfigUtils;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;

public class FlinkCdctomysql {
    public static MySqlSource<String> abc(String database, String table,String username,String pwd, StartupOptions model){
        return MySqlSource.<String>builder()
                .hostname(ConfigUtils.getString("mysql.host"))
                .port(3306)
                .databaseList(database)
                .tableList(table)
                .username(username)
                .password(pwd)
//              .serverTimeZone(ConfigUtils.getString("mysql.timezone"))
                .deserializer(new JsonDebeziumDeserializationSchema())
                .startupOptions(model)
                .includeSchemaChanges(true)
                .build();
    }
}
