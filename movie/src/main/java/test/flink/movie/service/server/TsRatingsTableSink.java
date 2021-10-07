package test.flink.movie.service.server;

import test.flink.movie.dao.DataBase;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.LinkedList;
import java.util.Queue;

public class TsRatingsTableSink extends RichSinkFunction<Tuple2<String, Integer>> {
    private Queue<Integer> queue = new LinkedList<>();

    @Override
    public void invoke(Tuple2<String, Integer> value, Context context) {
        if (queue.size() == 10) {
            queue.poll();
        }
        queue.offer(value.f1);
        DataBase.clearTsRatings();
        queue.forEach(DataBase::updateTsRatings);
    }
}
