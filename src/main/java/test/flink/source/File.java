package test.flink.source;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by IntelliJ IDEA
 * TODO: TODO
 *
 * @author: 徐成
 * Date: 2021/9/29
 * Time: 1:24 下午
 * Email: old_camel@163.com
 */
public class File {
    public static void main(String[] args) throws Exception {
        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 使得任务抢占同一个线程
        env.setParallelism(1);

        // 从文件中获取数据输出
        DataStream<String> dataStream = env.readTextFile("/Users/xucheng/data/code/flink-test/src/main/resources/sensor.txt");

        dataStream.print();

        env.execute();
    }
}
