package test.flink.FraudDetection;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;

/**
 * Created by IntelliJ IDEA
 * TODO: TODO
 *
 * @author: 徐成
 * Date: 2021/9/27
 * Time: 9:46 下午
 * Email: old_camel@163.com
 */

public class TimerStateValueStateFraudDetector extends FraudDetector {

    private static final long serialVersionUID = 1L;

    private static final double SMALL_AMOUNT = 1.00;
    private static final double LARGE_AMOUNT = 500.00;
    private static final long ONE_MINUTE = 60 * 1000;
    private transient ValueState<Boolean> flagState;
    private transient ValueState<Long> timerState;

    @Override
    public void open(Configuration parameters) {
        ValueStateDescriptor<Boolean> flagDescriptor = new ValueStateDescriptor<>(
                "flag",
                Types.BOOLEAN);
        flagState = getRuntimeContext().getState(flagDescriptor);


        ValueStateDescriptor<Long> timerDescriptor = new ValueStateDescriptor<>(
                "timer-state",
                Types.LONG);
        timerState = getRuntimeContext().getState(timerDescriptor);
    }
    @Override
    public void processElement(
            Transaction transaction,
            Context context,
            Collector<Alert> collector) throws Exception {
        // 获取当前key的状态值
        Boolean lastTransactionWasSmall = flagState.value();
        // 检查状态值是否存在
        if (lastTransactionWasSmall != null) {
            if (transaction.getAmount() > LARGE_AMOUNT) {
                // 输出账号id
                Alert alert = new Alert();
                alert.setId(transaction.getAccountId());

                collector.collect(alert);
            }

            // 清理一下状态
            cleanUp(context);
        }

        if (transaction.getAmount() < SMALL_AMOUNT) {
            // 更新状态ValueStateFraudDetector
            flagState.update(true);

            //更新一下定时器
            long timer = context.timerService().currentProcessingTime() + ONE_MINUTE;
            context.timerService().registerProcessingTimeTimer(timer);

            timerState.update(timer);
        }
    }
    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<Alert> out) {
        // 一分钟后删除状态
        timerState.clear();
        flagState.clear();
    }

    private void cleanUp(Context ctx) throws Exception {
        // 删除计时器
        Long timer = timerState.value();
        ctx.timerService().deleteProcessingTimeTimer(timer);

        // 清理所有的状态
        timerState.clear();
        flagState.clear();
    }
}

