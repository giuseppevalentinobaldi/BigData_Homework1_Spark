package top5;

import scala.Serializable;

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	private String productId;
	private float average;

	public Product(String i, float average) {
		this.setProductId(i);
		this.setAverage(average);
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public float getAverage() {
		return average;
	}

	public void setAverage(float average) {
		this.average = average;
	}

	public int compareTo(Product p) {
		if (this.getAverage() > p.getAverage()) {
			return 1;
		}
		if (this.getAverage() < p.getAverage()) {
			return -1;
		}
		return 0;
	}

	public String toString() {
		return this.getProductId() + "\t" + this.getAverage();
	}

}
