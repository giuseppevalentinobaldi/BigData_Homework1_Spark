package pairUser2;

public class Row {
	private String userId;
	private String productId;
	private int score;

	public Row(String userId, String productId, int score) {
		this.setUserId(userId);
		this.setProductId(productId);
		this.setScore(score);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

}
