package test.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.MultipleParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.flink.util.Preconditions;
/**
 * Created by IntelliJ IDEA
 * TODO: TODO
 *
 * @author: 徐成
 * Date: 2021/9/27
 * Time: 5:27 下午
 * Email: old_camel@163.com
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
String[] words =
        new String[] {
                "To be, or not to be,--that is the question:--",
                "Whether 'tis nobler in the mind to suffer",
                "The slings and arrows of outrageous fortune",
                "Or to take arms against a sea of troubles,",
                "And by opposing end them?--To die,--to sleep,--",
                "No more; and by a sleep to say we end",
                "The heartache, and the thousand natural shocks",
                "That flesh is heir to,--'tis a consummation",
                "Devoutly to be wish'd. To die,--to sleep;--",
                "To sleep! perchance to dream:--ay, there's the rub;",
                "For in that sleep of death what dreams may come,",
                "When we have shuffled off this mortal coil,",
                "Must give us pause: there's the respect",
                "That makes calamity of so long life;",
                "For who would bear the whips and scorns of time,",
                "The oppressor's wrong, the proud man's contumely,",
                "The pangs of despis'd love, the law's delay,",
                "The insolence of office, and the spurns",
                "That patient merit of the unworthy takes,",
                "When he himself might his quietus make",
                "With a bare bodkin? who would these fardels bear,",
                "To grunt and sweat under a weary life,",
                "But that the dread of something after death,--",
                "The undiscover'd country, from whose bourn",
                "No traveller returns,--puzzles the will,",
                "And makes us rather bear those ills we have",
                "Than fly to others that we know not of?",
                "Thus conscience does make cowards of us all;",
                "And thus the native hue of resolution",
                "Is sicklied o'er with the pale cast of thought;",
                "And enterprises of great pith and moment,",
                "With this regard, their currents turn awry,",
                "And lose the name of action.--Soft you now!",
                "The fair Ophelia!--Nymph, in thy orisons",
                "Be all my sins remember'd."
        };
        // Checking input parameters
        final MultipleParameterTool params = MultipleParameterTool.fromArgs(args);

        // set up the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);

        // get input data
        DataStream<String> text = null;
        if (params.has("input")) {
            // union all the inputs from text files
            for (String input : params.getMultiParameterRequired("input")) {
                if (text == null) {
                    text = env.readTextFile(input);
                } else {
                    text = text.union(env.readTextFile(input));
                }
            }
            Preconditions.checkNotNull(text, "Input DataStream should not be null.");
        } else {
            System.out.println("Executing WordCount example with default input data set.");
            System.out.println("Use --input to specify file input.");
            // get default test text data
            text = env.fromElements(words);
        }

        DataStream<Tuple2<String, Integer>> counts =
                // split up the lines in pairs (2-tuples) containing: (word,1)
                text.flatMap(new Tokenizer())
                        // group by the tuple field "0" and sum up tuple field "1"
                        .keyBy(value -> value.f0)
                        .sum(1);

        // emit result
        if (params.has("output")) {
            counts.writeAsText(params.get("output"));
        } else {
            System.out.println("Printing result to stdout. Use --output to specify output path.");
            counts.print();
        }
        // execute program
        env.execute("Streaming WordCount");
    }

    // *************************************************************************
    // USER FUNCTIONS
    // *************************************************************************

    /**
     * Implements the string tokenizer that splits sentences into words as a user-defined
     * FlatMapFunction. The function takes a line (String) and splits it into multiple pairs in the
     * form of "(word,1)" ({@code Tuple2<String, Integer>}).
     */
    public static final class Tokenizer
            implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // normalize and split the line 匹配数字和字母下划线的多个字符；
            String[] tokens = value.toLowerCase().split("\\W+");

            // emit the pairs
            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<>(token, 1));
                }
            }
        }
    }
}
