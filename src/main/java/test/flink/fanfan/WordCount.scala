package test.flink.fanfan;
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, createTypeInformation}
import scala.concurrent.ExecutionContext

/**
 * @Author littlepaike
 * @Date 2021/10/1 16:13
 * @Description: test.flink.fanfan
 * @version: 1.0
 * */
object WordCount {
  def main(args: Array[String]): Unit = {
    // 创建一个批处理执行环境
    val env = ExecutionEnvironment.getExecutionEnvironment

    // 从文件中读取数据
    val inputPath = "/Users/xucheng/data/hello.txt"
    val inputDataSet = env.readTextFile(inputPath)

    // 分词、转换、求和
    val wordCountDataSet = inputDataSet.flatMap(_.split(" "))
      .map((_, 1))
      .groupBy(0)
      .sum(1)

    // 打印
    wordCountDataSet.print()
  }
}
