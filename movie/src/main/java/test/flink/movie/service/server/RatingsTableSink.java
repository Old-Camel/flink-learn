package test.flink.movie.service.server;

import test.flink.movie.bean.RatingBean;
import test.flink.movie.dao.DataBase;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class RatingsTableSink extends RichSinkFunction<RatingBean> {
    @Override
    public void invoke(RatingBean value, Context context) {
        DataBase.newRating(value);
    }
}
