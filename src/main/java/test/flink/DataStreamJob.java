package test.flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by IntelliJ IDEA
 * TODO: TODO
 *
 * @author: 徐成
 * Date: 2021/9/27
 * Time: 2:23 下午
 * Email: old_camel@163.com
 */
public class DataStreamJob {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        org.apache.flink.streaming.api.datastream.DataStream<Person> flintstones = env.fromElements(
                new Person("张三", 35),
                new Person("王五", 35),
                new Person("赵六", 2));

        org.apache.flink.streaming.api.datastream.DataStream<Person> adults = flintstones.filter(new FilterFunction<Person>() {
            @Override
            public boolean filter(Person person) throws Exception {
                return person.age >= 18;
            }
        });

        adults.print();

        env.execute();
    }

    public static class Person {
        public String name;
        public Integer age;

        public Person() {
        }



        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }



        public String toString() {
            return this.name.toString() + ": age " + this.age.toString();
        }


    }
}