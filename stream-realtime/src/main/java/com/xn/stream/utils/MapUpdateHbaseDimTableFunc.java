package com.xn.stream.utils;

import com.alibaba.fastjson.JSONObject;

import com.xn.stream.utils.HbaseUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.hadoop.hbase.client.Connection;

/**
 * @Package com.stream.func.MapUpdateHbaseDimTable
 * @Author zhou.han
 * @Date 2024/12/19 14:11
 * @description:
 */
public class MapUpdateHbaseDimTableFunc extends RichMapFunction<JSONObject,JSONObject> {

    private Connection connection;
    private final String hbaseNameSpace;
    private final String zkHostList;
    private HbaseUtils hbaseUtils;

    public MapUpdateHbaseDimTableFunc(String cdhZookeeperServer, String cdhHbaseNameSpace) {
        this.zkHostList = cdhZookeeperServer;
        this.hbaseNameSpace = cdhHbaseNameSpace;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        hbaseUtils = new HbaseUtils(zkHostList);
        connection = hbaseUtils.getConnection();
    }

    @Override
    public JSONObject map(JSONObject jsonObject) throws Exception {
        String op = jsonObject.getString("op");
        if ("d".equals(op)){
            hbaseUtils.deleteTable(jsonObject.getJSONObject("before").getString("sink_table"));
        }else if ("r".equals(op) || "c".equals(op)){
            String[] columnName = jsonObject.getJSONObject("after").getString("sink_columns").split(",");
            String tableName = jsonObject.getJSONObject("after").getString("sink_table");
            hbaseUtils.createTable(hbaseNameSpace,tableName,columnName);
        }else {
            hbaseUtils.deleteTable(jsonObject.getJSONObject("before").getString("sink_table"));
            String[] columnName = jsonObject.getJSONObject("after").getString("sink_columns").split(",");
            String tableName = jsonObject.getJSONObject("after").getString("sink_table");
            hbaseUtils.createTable(hbaseNameSpace,tableName,columnName);
        }
        return jsonObject;
    }

    @Override
    public void close() throws Exception {
        super.close();
        connection.close();
    }
}
