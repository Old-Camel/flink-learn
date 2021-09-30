package test.flink.source;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;
/**
 * Created by IntelliJ IDEA
 * TODO: TODO
 *
 * @author: 徐成
 * Date: 2021/9/29
 * Time: 5:42 下午
 * Email: old_camel@163.com
 */
public class Kafka {
    public static void main(String[] args) throws Exception {
        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置并行度1
        env.setParallelism(1);

        Properties properties = new Properties();
        //设置你kafka安装的地址
        properties.setProperty("bootstrap.servers", "222.30.195.176:9092");
        //properties.setProperty("bootstrap.servers", "localhost:9092");
        // 下面这些次要参数
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("auto.offset.reset", "latest");

        // flink添加外部数据源
        DataStream<String> dataStream = env.addSource(new FlinkKafkaConsumer<String>("sensor", new SimpleStringSchema(),properties));

        // 打印输出
        dataStream.print();

        env.execute();
    }
}
