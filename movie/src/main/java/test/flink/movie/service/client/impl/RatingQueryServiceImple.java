package test.flink.movie.service.client.impl;

import test.flink.movie.bean.RatingQueryResultBean;
import test.flink.movie.dao.RatingQueryMapper;
import test.flink.movie.service.client.RatingQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingQueryServiceImple implements RatingQueryService {
    @Override
    public List<RatingQueryResultBean> query(int movieId) {
        return RatingQueryMapper.queryRatings(movieId);
    }
}
