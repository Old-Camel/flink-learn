package test.flink.movie.service.client.impl;

import test.flink.movie.bean.RatingBean;
import test.flink.movie.service.client.PlayerService;
import test.flink.movie.util.Kafka;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Override
    public void rate(RatingBean bean) {
        Kafka.send(Kafka.DEFAULT_TOPIC, "key", JSON.toJSONString(bean));
    }
}
