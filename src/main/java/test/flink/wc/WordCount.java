package test.flink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
/**
 * Created by IntelliJ IDEA
 * TODO: TODO 批处理
 *
 * @author: 徐成
 * Date: 2021/9/27
 * Time: 10:39 下午
 * Email: old_camel@163.com
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        // 创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 从文件中读取数据
        String inputPath = "/Users/xucheng/data/hello.txt";
        DataSet<String> inputDataSet = env.readTextFile(inputPath);

        // 对数据集进行处理，按空格分词展开，转换成(word, 1)二元组进行统计
        // 按照第一个位置的word分组
        // 按照第二个位置上的数据求和
       /* Tuple （元组）是一个混合类型，包含固定数量的属性，
         并且每个属性类型可以不同。例如：二元组有 2 个属性，类名为 Tuple2；
         三元组有 3 个属性，类名为 Tuple3，以此类推。Java 支持的元组为 Tuple1 - Tuple25。
         访问属性可以通过属性名直接访问，如：tuple.f4 代表 tuple 的第 5 个属性。
         或者使用 tuple.getField(int position) 方法，参数 position 从 0 开始*/
        DataSet<Tuple2<String, Integer>> resultSet = inputDataSet.flatMap(new MyFlatMapper())
                .groupBy(0)
                .sum(1);

        resultSet.print();
    }

    // 自定义类，实现FlatMapFunction接口
    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String s, Collector<Tuple2<String, Integer>> out) throws Exception {
            // 按空格分词
            String[] words = s.split(" ");
            // 遍历所有word，包成二元组输出
            for (String str : words) {
                out.collect(new Tuple2<>(str, 1));
            }
        }
    }
}
