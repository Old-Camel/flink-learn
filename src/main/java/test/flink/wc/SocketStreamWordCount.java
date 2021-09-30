package test.flink.wc;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.client.program.StreamContextEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by IntelliJ IDEA
 * TODO: TODO
 *
 * @author: 徐成
 * Date: 2021/9/27
 * Time: 5:27 下午
 * Email: old_camel@163.com
 */
public class SocketStreamWordCount {

    public static void main(String[] args) throws Exception {

        // 创建流处理执行环境
        StreamExecutionEnvironment env = StreamContextEnvironment.getExecutionEnvironment();

        // 设置并行度，默认值 = 当前计算机的CPU逻辑核数（设置成1即单线程处理）
        // env.setMaxParallelism(32);

        // 从文件中读取数据
        //String inputPath = "/Users/xucheng/data/hello.txt";
        //DataStream<String> inputDataStream = env.readTextFile(inputPath);
        DataStream<String> inputDataStream = env.socketTextStream("localhost", 8888);
        // 基于数据流进行转换计算
        DataStream<Tuple2<String, Integer>> resultStream = inputDataStream
                .flatMap(new WordCount.MyFlatMapper())
                .slotSharingGroup("a")
                .keyBy(item -> item.f0)
                .sum(1)
                .setParallelism(2).slotSharingGroup("b");

        resultStream.print();

        // 执行任务
        env.execute();
    }

}



