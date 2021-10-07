package test.flink.movie.service.client.impl;

import test.flink.movie.bean.HotBean;
import test.flink.movie.dao.MovieMapper;
import test.flink.movie.service.client.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    @Override
    public List<HotBean> queryHot() {
        return MovieMapper.queryHotMovies();
    }
}
