package top10;

import scala.Serializable;

public class Product implements Serializable{
	private static final long serialVersionUID = 1L;
	private String productId;
	private int score;

	public Product(String productId,int score){
		this.setProductId(productId);
		this.setScore(score);
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int compareTo(Product p) {
		if (this.getScore() > p.getScore()) {
			return 1;
		}
		if (this.getScore() < p.getScore()) {
			return -1;
		}
		return 0;
	}
	
	public String toString(){
		return this.getProductId() +"\t"+this.getScore();
	}

}
