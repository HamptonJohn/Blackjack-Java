package blackjack;

public class Message {
	
	String mes = "";
	String who = "";
	
	public Message(String m, String w) {
		this.mes = m;
		this.who = w;
	}
	
	public String getMessage() {
		return this.mes;
	}
	
	public String getWho() {
		return this.who;
	}
	
}
