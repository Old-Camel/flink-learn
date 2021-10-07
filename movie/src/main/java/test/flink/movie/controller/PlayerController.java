package test.flink.movie.controller;

import test.flink.movie.bean.RatingBean;
import test.flink.movie.service.client.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService service;

    @CrossOrigin
    @RequestMapping("/player/rate")
    public void rate(@RequestBody RatingBean bean) {
        service.rate(bean);
    }
}
