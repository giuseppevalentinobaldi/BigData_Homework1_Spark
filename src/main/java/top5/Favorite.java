package top5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class Favorite {
	private PriorityQueue<Product> topProduct;
	private static final int TOP_K = 5;

	public Favorite() {
		this.setTopProduct(new PriorityQueue<Product>(Comparator.comparing(p -> p.getAverage())));
	}

	public void addProduct(Product p) {
		this.getTopProduct().add(p);
		if(this.getTopProduct().size()>TOP_K)
			this.getTopProduct().remove();
	}

	public List<Product> toList() {
		List<Product> output = new ArrayList<Product>();
		for (int i = 0; i < TOP_K; i++)
			output.add(this.getTopProduct().remove());
		return output;
	}

	public int getTopK() {
		return TOP_K;
	}

	public Stack<Product> sort(Stack<Product> s) {

		if (s.isEmpty()) {
			return s;
		}
		Product pivot = s.pop();

		// partition
		Stack<Product> left = new Stack<Product>();
		Stack<Product> right = new Stack<Product>();
		while (!s.isEmpty()) {
			Product y = s.pop();
			if (y.getAverage() < pivot.getAverage()) {
				left.push(y);
			} else {
				right.push(y);
			}
		}
		sort(left);
		sort(right);

		// merge
		Stack<Product> tmp = new Stack<Product>();
		while (!right.isEmpty()) {
			tmp.push(right.pop());
		}
		tmp.push(pivot);
		while (!left.isEmpty()) {
			tmp.push(left.pop());
		}
		while (!tmp.isEmpty()) {
			s.push(tmp.pop());
		}
		return s;
	}

	public PriorityQueue<Product> getTopProduct() {
		return topProduct;
	}

	public void setTopProduct(PriorityQueue<Product> topProduct) {
		this.topProduct = topProduct;
	}

}
