package test.flink.movie.service.client;

import test.flink.movie.bean.RatingBean;

public interface PlayerService {
    void rate(RatingBean bean);
}
