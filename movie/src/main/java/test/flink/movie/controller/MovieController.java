package test.flink.movie.controller;

import test.flink.movie.bean.HotBean;
import test.flink.movie.service.client.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {
    @Autowired
    private MovieService service;

    @CrossOrigin
    @RequestMapping("/movie/hot")
    public List<HotBean> getHot() {
        return service.queryHot();
    }
}
