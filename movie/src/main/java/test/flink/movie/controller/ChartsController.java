package test.flink.movie.controller;

import test.flink.movie.bean.web.ChartsPackage;
import test.flink.movie.service.client.ChartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChartsController {
    @Autowired
    private ChartsService service;

    @CrossOrigin
    @RequestMapping("/charts/query")
    public ChartsPackage getChartsData() {
        return service.query();
    }
}
