package top10;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

public class Favorite {
	private Map<String, Integer> product;

	public Favorite() {
		this.product = new HashMap<String, Integer>();
	}

	public void addProduct(Product p) {
		if (product.containsKey(p.getProductId()))
			product.put(p.getProductId(),
					new Integer(Math.max(product.get(p.getProductId()).intValue(), p.getScore())));
		else
			product.put(p.getProductId(), new Integer(p.getScore()));
	}

	public List<Product> productIterable() {
		Set<Entry<String, Integer>> es = entriesSortedByValues(this.product);
		Iterator<Entry<String, Integer>> i1 = es.iterator();
		Entry<String, Integer> tempentry;
		int count = 0;
		List<Product> output = new ArrayList<Product>();
		while (i1.hasNext() && count < 10) {
			tempentry = i1.next();
			output.add(new Product(tempentry.getKey(), tempentry.getValue()));
			count++;
		}
		return output;
	}

	public <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				int res = e1.getValue().compareTo(e2.getValue());
				return res != 0 ? res : 1;
			}
		});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

}
