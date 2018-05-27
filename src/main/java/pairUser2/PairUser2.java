package pairUser2;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public final class PairUser2 {

	private static JavaSparkContext sparkContext;

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.err.println("Usage: PairUser <file>");
			System.exit(1);
		}
		long startTime = System.currentTimeMillis();
		SparkConf conf = new SparkConf().setAppName("PairUser");
		sparkContext = new JavaSparkContext(conf);

		JavaRDD<Row> amazon = sparkContext.textFile(args[0]).map(convertToRDD).filter(row -> row.getScore() > 3);

		JavaPairRDD<String, Iterable<String>> productUsers = amazon
				.mapToPair(row -> new Tuple2<>(row.getProductId(), row.getUserId())).groupByKey();

		JavaPairRDD<String, Iterable<String>> semi = productUsers.flatMapToPair(pairUser).groupByKey();

		JavaPairRDD<String,String> output = semi.mapToPair(iterableToSet).filter(affinityCut);
		
		TreeMap<String,String> pd = new TreeMap<String,String>(output.collectAsMap());
		
		for(Entry<String, String> e: pd.entrySet())
			System.out.println(e.getKey()+"\t"+e.getValue());
		//output.saveAsTextFile("output");
		
		sparkContext.stop();
		sparkContext.close();

		long stopTime = System.currentTimeMillis();
		System.out.println("Job Finished in " + (stopTime - startTime) / 1000.0 + " seconds");
	}

	private static Function<String, Row> convertToRDD = line -> {
		String[] tokens = line.split("\t");
		Row rv = new Row(tokens[2], tokens[1], Integer.parseInt(tokens[6]));
		return rv;
	};

	private static PairFlatMapFunction<Tuple2<String, Iterable<String>>, String, String> pairUser = line -> {
		ArrayList<Tuple2<String, String>> o = new ArrayList<Tuple2<String, String>>();
		String product = line._1();
		line._2().forEach(i1 -> {
			String user1 = i1;
			line._2().forEach(i2 -> {
				String user2 = i2;
				if (!user1.equals(user2)) {
					if (user1.compareTo(user2) < 0)
						o.add(new Tuple2<String, String>(user1 + "\t" + user2, product));
				}
			});
		});
		return o.iterator();
	};

	private static PairFunction<Tuple2<String, Iterable<String>>, String,String> iterableToSet = line -> {
		HashSet<String> set = new HashSet<String>();
		line._2.forEach(o -> set.add(o));
		if (set.size() > 2)
			return new Tuple2<String,String>(line._1(),set.toString());
		else
			return new Tuple2<String,String>(line._1(),"");
	};

	private static Function<Tuple2<String,String>, Boolean> affinityCut = bool -> !bool._2().equals("");
}
// spark-submit --class "affini.Affini" --master local[4]
// /home/giuseppe/Workspace/Spark/target/Spark-0.0.1-SNAPSHOT.jar
// /home/giuseppe/Scrivania/amazon/1999_2006.csv