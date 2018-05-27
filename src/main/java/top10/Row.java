package top10;

public class Row {
	private String user;
	private String product;
	private int score;
	public String getUser() {
		return user;
	}
	public Row(String user, String product, int score){
		this.setProduct(product);
		this.setUser(user);
		this.setScore(score);
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
}
