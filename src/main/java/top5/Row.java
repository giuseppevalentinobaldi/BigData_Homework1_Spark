package top5;

import scala.Serializable;

public class Row implements Serializable {
	private static final long serialVersionUID = 1L;
	private String productId;
	private int score;
	private long timestamp;

	public Row(String productId, int score, long timestamp) {
		this.setProductId(productId);
		this.setScore(score);
		this.setTimestamp(timestamp);
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

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
