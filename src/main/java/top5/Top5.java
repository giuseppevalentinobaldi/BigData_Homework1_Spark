package top5;

import java.util.Iterator;
//import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public final class Top5 {

	private static JavaSparkContext sparkContext;

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.err.println("Usage: Top5 <file>");
			System.exit(1);
		}

		// create spark configuration and spark context
		SparkConf conf = new SparkConf().setAppName("Top5");
		sparkContext = new JavaSparkContext(conf);

		long startTime = System.currentTimeMillis();

		JavaRDD<Row> amazon = sparkContext.textFile(args[0]).map(convertToRDD);

		JavaPairRDD<String, Integer> groupProductbyKey = sparkContext
				.parallelizePairs(amazon.mapToPair(productDateScore).collect());

		JavaPairRDD<String, Tuple2<Integer, Integer>> reducedAggregateCount = groupProductbyKey
				.mapValues(value -> new Tuple2<Integer, Integer>(value, 1)).reduceByKey(
						(tuple1, tuple2) -> new Tuple2<Integer, Integer>(tuple1._1 + tuple2._1, tuple1._2 + tuple2._2));

		JavaPairRDD<String, Iterable<Product>> averagePair = reducedAggregateCount.mapToPair(getAverageByKey).groupByKey();
		
		JavaPairRDD<String, Iterable<Product>> output = averagePair.mapToPair(top5).sortByKey();
		
		output.saveAsTextFile("output");
		
		/*
		List<Tuple2<String, Iterable<Product>>> output = output.collect();

		for (Tuple2<String, Iterable<Product>> tuple : output) {
			tuple._2().forEach(data -> {
				System.out.println(tuple._1() + "\t" + data.toString());
			});
		}*/

		long endTime = System.currentTimeMillis();
		sparkContext.stop();
		sparkContext.close();

		System.out.println("Job Finished in " + (endTime - startTime) / 1000.0 + " seconds");
	}

	private static Function<String, Row> convertToRDD = line -> {
		String[] fields = line.split("\t");
		Row sd = new Row(fields[1], Integer.parseInt(fields[6]), Long.parseLong(fields[7]));
		return sd;
	};

	private static PairFunction<Row, String, Integer> productDateScore = line -> {
		return new Tuple2<>(ConvertDate.convertTimestampToDate((line.getTimestamp())) + " " + line.getProductId(),
				line.getScore());
	};

	private static PairFunction<Tuple2<String, Tuple2<Integer, Integer>>, String, Product> getAverageByKey = (tuple) -> {
		Tuple2<Integer, Integer> val = tuple._2;
		String[] s = tuple._1().split(" ");
		int total = val._1;
		int count = val._2;
		Tuple2<String, Product> averagePair = new Tuple2<String, Product>(s[0], (new Product(s[1],(float) total / (float) count)));
		return averagePair;
	};

	private static PairFunction<Tuple2<String, Iterable<Product>>, String, Iterable<Product>> top5 = rank -> {
		Favorite favorite = new Favorite();
		Iterator<Product> p = rank._2().iterator();
		while (p.hasNext()) {
			favorite.addProduct(p.next());
		}
		Iterable<Product> iterable = favorite.toList();
		return new Tuple2<String, Iterable<Product>>(rank._1(), iterable);
	};

}
// spark-submit --class "top5.Top5" --master local[4]
// /home/giuseppe/Workspace/Spark/target/Spark-0.0.1-SNAPSHOT.jar
// /home/giuseppe/Scrivania/amazon/1999_2006.csv