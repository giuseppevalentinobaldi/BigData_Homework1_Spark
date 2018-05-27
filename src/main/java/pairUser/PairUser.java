package pairUser;

import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.spark.api.java.function.Function;

public class PairUser {

	private static final int numberProdMin = 3;
	private static final int ScoreMin = 4;

	private static Function<String, Review> convertToRDD = line -> {
		String[] tokens = line.split("\t");
		Review rv = new Review(tokens[2], tokens[1], Integer.parseInt(tokens[6]));
		return rv;
	};

	private static Function<Tuple2<String, Iterable<String>>, Tuple2<String, Set<String>>> convertToSet = tuple -> {
		Set<String> set = new HashSet<String>();
		for (String s : tuple._2()) {
			set.add(s);
		}
		return new Tuple2<String, Set<String>>(tuple._1(), set);
	};

	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Usage: JavaPairUser <file>");
			System.exit(1);
		}

		long startTime = System.currentTimeMillis();

		// inizializzazione sparkConf e sparkContext
		SparkConf conf = new SparkConf().setAppName("JavaPairUser");
		JavaSparkContext sc = new JavaSparkContext(conf);

		// lettura del file di input e creazione di oggetti Review
		JavaRDD<Review> reviewRDD = sc.textFile(args[0]).map(convertToRDD).filter(rv -> rv.getScore() >= ScoreMin);

		// user e lista dei suoi prodotti
		JavaPairRDD<String, Iterable<String>> userProdRDD = reviewRDD
				.mapToPair(rv -> new Tuple2<>(rv.getUser(), rv.getProduct())).distinct().groupByKey().sortByKey();

		// inserimento dei prodotti in un set (rimane ordinato per utente)
		JavaRDD<Tuple2<String, Set<String>>> userSetRDD = userProdRDD.map(convertToSet);

		// lista di Tuple
		List<Tuple2<String, Set<String>>> listUserSet = new LinkedList<Tuple2<String, Set<String>>>(
				userSetRDD.collect());

		String user;
		Set<String> setUser;
		Set<String> copySet;
		while (!listUserSet.isEmpty()) {

			// prendo il primo utente della lista
			user = listUserSet.get(0)._1();

			// prendo il set del primo utente
			setUser = listUserSet.get(0)._2();

			for (Tuple2<String, Set<String>> tuple : listUserSet) {

				if (user.compareTo(tuple._1()) >= 0) {
					continue;
				}

				copySet = new HashSet<String>(setUser);

				copySet.retainAll(tuple._2());

				if (copySet.size() >= numberProdMin) {

					String prod;
					Iterator<String> it = copySet.iterator();

					prod = it.next();
					while (it.hasNext()) {
						prod += "\t" + it.next();
					}

					System.out.println(user + "\t" + tuple._1() + "\t" + prod);
				}

			}

			listUserSet.remove(0);

		}

		// chiusura del context
		sc.stop();
		sc.close();

		long stopTime = System.currentTimeMillis();

		System.out.println("Job Finished in " + (stopTime - startTime) / 1000.0 + " seconds");

	}

}
