package top5;

import scala.Serializable;

public class Amazon implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String productId;
	private String userId;
	private String profileName;
	private int helpfulnessNumerator;
	private int helpfulnessDenominator;
	private int score;
	private long timestamp; // bisogno convertirlo in data
	private String summary; // summary of the review
	private String text;

	public Amazon(int id, String productId, String userId, String profileName, int helpfulnessNumerator, int helpfulnessDenominator,
			int score, long timestamp, String summary, String text) {
		this.setId(id);
		this.setProductId(productId);
		this.setUserId(userId);
		this.setProfileName(profileName);
		this.setHelpfulnessNumerator(helpfulnessNumerator);
		this.setHelpfulnessDenominator(helpfulnessDenominator);
		this.setScore(score);
		this.setTimestamp(timestamp);
		this.setSummary(summary);
		this.setText(text);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public int getHelpfulnessNumerator() {
		return helpfulnessNumerator;
	}

	public void setHelpfulnessNumerator(int helpfulnessNumerator) {
		this.helpfulnessNumerator = helpfulnessNumerator;
	}

	public int getHelpfulnessDenominator() {
		return helpfulnessDenominator;
	}

	public void setHelpfulnessDenominator(int helpfulnessDenominator) {
		this.helpfulnessDenominator = helpfulnessDenominator;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		return this.getId() + ", " + this.getProductId() + ", " + this.getUserId() + ", " + this.getProfileName() + ", "
				+ this.getHelpfulnessNumerator() + ", " + this.getHelpfulnessDenominator() + ", " + this.getScore()
				+ ", " + this.getTimestamp() + ", " + this.getSummary() + ", " + this.getText();
	}

}
