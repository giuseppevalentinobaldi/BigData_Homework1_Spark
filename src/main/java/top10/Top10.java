package top10;

import java.util.Iterator;
//import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public final class Top10 {

	private static JavaSparkContext sparkContext;

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.err.println("Usage: Top10 <file>");
			System.exit(1);
		}
		// create spark configuration and spark context
		SparkConf conf = new SparkConf().setAppName("Top10");
		sparkContext = new JavaSparkContext(conf);

		long startTime = System.currentTimeMillis();

		JavaRDD<Row> amazon = sparkContext.textFile(args[0]).map(convertToRDD);

		JavaPairRDD<String, Iterable<Product>> userProducts = amazon
				.mapToPair(row -> new Tuple2<>(row.getUser(), new Product(row.getProduct(), row.getScore())))
				.groupByKey();
		
		JavaPairRDD<String, Iterable<Product>> output= userProducts.mapToPair(top10).sortByKey();
		
		output.saveAsTextFile("output");
		
		/*List<Tuple2<String, Iterable<Product>>> output = output.collect();

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
		Row sd = new Row(fields[2], fields[1], Integer.parseInt(fields[6]));
		return sd;
	};

	private static PairFunction<Tuple2<String, Iterable<Product>>, String, Iterable<Product>> top10 = rank -> {
		Favorite favorite = new Favorite();
		Iterator<Product> p = rank._2().iterator();
		while (p.hasNext()) {
			favorite.addProduct(p.next());
		}
		Iterable<Product> iterable = favorite.productIterable();
		return new Tuple2<String, Iterable<Product>>(rank._1(), iterable);
	};
}
// spark-submit --class "top10.Top10" --master local[4]
// /home/giuseppe/Workspace/Spark/target/Spark-0.0.1-SNAPSHOT.jar
// /home/giuseppe/Scrivania/amazon/1999_2006.csv