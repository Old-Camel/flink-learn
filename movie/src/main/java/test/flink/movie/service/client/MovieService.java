package test.flink.movie.service.client;

import test.flink.movie.bean.HotBean;

import java.util.List;

public interface MovieService {
    List<HotBean> queryHot();
}
