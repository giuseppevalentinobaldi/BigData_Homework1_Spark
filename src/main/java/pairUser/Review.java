package pairUser;

import scala.Serializable;

public class Review implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String user;
	private String product;
	private int score;
	
	public Review(String user, String product, int score){
		
		this.user = user;
		this.product = product;
		this.score = score;
		
	}
	
	public String getUser(){
		return this.user;
	}
	
	public String getProduct(){
		return this.product;
	}
	
	public int getScore(){
		return this.score;
	}
	
	public String toString(){
		return this.user + "\t" + this.product + "\t" + this.score;
	}

}
