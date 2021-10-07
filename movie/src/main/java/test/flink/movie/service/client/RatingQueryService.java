package test.flink.movie.service.client;

import test.flink.movie.bean.RatingQueryResultBean;

import java.util.List;

public interface RatingQueryService {
    List<RatingQueryResultBean> query(int movieId);
}
