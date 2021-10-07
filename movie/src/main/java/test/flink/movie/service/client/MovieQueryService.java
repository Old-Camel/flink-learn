package test.flink.movie.service.client;

import test.flink.movie.bean.MovieBean;

import java.util.List;

public interface MovieQueryService {
    List<MovieBean> query(double minAvgRating, int minCount, boolean useCount, int n);
}
