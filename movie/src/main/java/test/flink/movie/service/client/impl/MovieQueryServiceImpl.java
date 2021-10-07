package test.flink.movie.service.client.impl;

import test.flink.movie.bean.MovieBean;
import test.flink.movie.dao.MovieQueryMapper;
import test.flink.movie.service.client.MovieQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieQueryServiceImpl implements MovieQueryService {
    @Override
    public List<MovieBean> query(double minAvgRating, int minCount, boolean useCount, int n) {
        String order = useCount ? "count" : "avg_rating";
        return MovieQueryMapper.queryMovies(minAvgRating, minCount, order, n);
    }
}
