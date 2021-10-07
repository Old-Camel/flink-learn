package test.flink.movie;

import test.flink.movie.service.server.Processor;

public class ServerApplication {
    public static void main(String[] args) {
        Processor processor = new Processor();
        processor.run();
    }
}
