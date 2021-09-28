package test.flink.FraudDetection;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.walkthrough.common.sink.AlertSink;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;
import org.apache.flink.walkthrough.common.source.TransactionSource;

/**
 * Created by IntelliJ IDEA
 * TODO: TODO
 *
 * @author: 徐成
 * Date: 2021/9/27
 * Time: 9:39 下午
 * Email: old_camel@163.com
 */
public class FraudDetectionJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Transaction> transactions = env
                .addSource(new TransactionSource())
                .name("transactions");

        DataStream<Alert> alerts = transactions
                .keyBy(Transaction::getAccountId)
                //.process(new FraudDetector())
                //.process(new ValueStateFraudDetector())
                .process(new TimerStateValueStateFraudDetector())
                .name("fraud-detector");

        alerts
                .addSink(new AlertSink())
                .name("send-alerts");

        env.execute("Fraud Detection");
    }
}
