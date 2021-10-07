package test.flink.movie;

import test.flink.movie.bean.RatingBean;
import test.flink.movie.dao.DataBase;
import test.flink.movie.service.sim.SimSource;
import test.flink.movie.util.Message;
import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;
import java.util.Scanner;

public class DataSimulator {
    /**
     * 执行前先创建movie的mysql数据库,执行resource文件夹下面的movie.sql文件
     * @param args
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print(">>> ");
        String code = in.next();
        if (code.equals("init")) {
            initDataBase();
            Message.info("Database init finished");
        } else if (code.equals("run")) {
            produce();
        }


    }

    public static void produce() {
        try {

            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            /**
             * 在Flink1.12.0有个重要的功能，批流一体，就是可以用之前流处理的API处理批数据。
             *
             * 只需要调用setRuntimeMode(RuntimeExecutionMode executionMode)。
             *
             * RuntimeExecutionMode 就是一个枚举，有流、批和自动。
             */
            env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
            DataStreamSource<RatingBean> ds = env.addSource(new SimSource()).setParallelism(2);
            SingleOutputStreamOperator<String> jsonDS = ds.map((MapFunction<RatingBean, String>) JSON::toJSONString);
            // 输出到控制台
            jsonDS.print();
            // 创建 Kafka Producer
            Properties prop = new Properties();
            prop.setProperty("bootstrap.servers", "222.30.195.176:9092");
            FlinkKafkaProducer<String> kafka = new FlinkKafkaProducer<>("movie", new SimpleStringSchema(), prop);
            jsonDS.addSink(kafka);
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initDataBase() {
        DataBase.init();
    }
}
