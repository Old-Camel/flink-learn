package test.flink.movie.service.server;

import test.flink.movie.bean.RatingBean;
import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.io.Serializable;
import java.util.Properties;

public class Processor implements Serializable {
    public void run() {
        try {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            Properties prop = new Properties();
            prop.setProperty("bootstrap.servers", "222.30.195.176:9092");
            prop.setProperty("group.id", "flink");
            prop.setProperty("auto.offset.reset", "latest");
            prop.setProperty("flink.partition-discovery.interval-millis", "5000");
            prop.setProperty("enable.auto.commit", "true");
            prop.setProperty("auto.commit.interval.ms", "2000");
            FlinkKafkaConsumer<String> kafka = new FlinkKafkaConsumer<>("movie", new SimpleStringSchema(), prop);
            kafka.setStartFromGroupOffsets(); // 设置从 offset 开始消费
            DataStreamSource<String> kafkaDS = env.addSource(kafka);
            SingleOutputStreamOperator<RatingBean> beanDS = kafkaDS.map((MapFunction<String, RatingBean>) s
                    -> JSON.parseObject(s, RatingBean.class));
            // 将评分数据插入 ratings 表
            beanDS.addSink(new RatingsTableSink());
            // 计算平均分, 评分次数并更新 movies 表
            SingleOutputStreamOperator<Tuple2<Integer, Tuple2<Double, Integer>>> dataDS = beanDS
                    .map(bean -> new Tuple2<>(bean.getMovieId(), new Tuple2<>(bean.getRating(), 1)))
                    .returns(Types.TUPLE(Types.INT, Types.TUPLE(Types.DOUBLE, Types.INT)))
                    /**   在一般情况下，Java会擦除泛型类型信息。 Flink尝试使用Java保留的少量位（主要是函数签名和子类信息）通过反射重建尽可能多的类型信息。对于函数的返回类型取决于其输入类型的情况，此逻辑还包含一些简单类型推断,在Flink无法重建已擦除的泛型类型信息的情况下，Java API提供所谓的类型提示。类型提示告诉系统函数生成的数据流或数据集的类型：
                     这个地方要注意，在map这种参数里有泛型算子中。
                     如果用lambda表达式，必须将参数的类型显式地定义出来。
                     并且要有returns，指定返回的类型
                     详情可以参考Flink官方文档：https://ci.apache.org/projects/flink/flink-docs-release-1.6/dev/java_lambdas.html
                     */
                    .keyBy(tuple -> tuple.f0)
                    .reduce((ReduceFunction<Tuple2<Integer, Tuple2<Double, Integer>>>) (t1, t2)
                            -> new Tuple2<>(t1.f0, new Tuple2<>((t1.f1.f0 * t1.f1.f1 + t2.f1.f0 * t2.f1.f1) / (t1.f1.f1 + t2.f1.f1), t1.f1.f1 + t2.f1.f1)));
            dataDS.addSink(new MoviesTableSink());
            // 滚动窗口, 统计最近 n 时间的热门电影
            SingleOutputStreamOperator<Tuple2<Integer, Integer>> windowDS1 = beanDS
                    .map(bean -> new Tuple2<>(bean.getMovieId(), 1))
                    .returns(Types.TUPLE(Types.INT, Types.INT))
                    .keyBy(tuple -> tuple.f0)
                    .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                    .sum(1);
            windowDS1.addSink(new HotTableSink());
            // 滚动窗口, 统计最近 n 时间的评分次数
            SingleOutputStreamOperator<Tuple2<String, Integer>> windowDS2 = beanDS
                    .map(bean -> new Tuple2<>("window", 1))
                    .returns(Types.TUPLE(Types.STRING, Types.INT))
                    .keyBy(tuple -> tuple.f0)
                    .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                    .sum(1);
            windowDS2.addSink(new TsRatingsTableSink());
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
