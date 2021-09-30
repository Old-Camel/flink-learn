package test.flink;

import java.util.Random;

/**
 * Created by IntelliJ IDEA
 * TODO: TODO
 *
 * @author: 徐成
 * Date: 2021/9/28
 * Time: 11:22 上午
 * Email: old_camel@163.com
 */
public class Testy {
    public static void main(String[] args) {
        //System.out.println("hello world ");
        Random random = new Random();

        while (true){
            System.out.println(random.nextGaussian());
        }
    }

}
