package test.flink.source;

/**
 * Created by IntelliJ IDEA
 * TODO: 温度传感器
 *
 * @author: 徐成
 * Date: 2021/9/29
 * Time: 1:19 下午
 * Email: old_camel@163.com
 */
public class SensorReading {
    // 属性：id，时间戳，温度值
    private String id;
    private long timestamp;
    private double temperature;

    public SensorReading() {

    }

    public SensorReading(String id, long timestamp, double temperature) {
        this.id = id;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", temperature=" + temperature +
                '}';
    }
}
