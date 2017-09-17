/* File: Cabbage.java
 * Author: Stanley Pieda
 * Date: August, 2017
 * Description: Simple data transfer object.
 */
public class Cabbage {
	private int id;
	private int lineNumber;
	private String alpha;
	private String beta;
	private String charlie;
	private String delta;
	
	public Cabbage() {
		this(0,0,"","","","");
	}
	
	public Cabbage(int id, int lineNumber, String alpha, String beta, String charlie, String delta) {
		this.id = id;
		this.lineNumber = lineNumber;
		this.alpha = alpha;
		this.beta = beta;
		this.charlie = charlie;
		this.delta = delta;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getAlpha() {
		return alpha;
	}
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
	public String getBeta() {
		return beta;
	}
	public void setBeta(String beta) {
		this.beta = beta;
	}
	public String getCharlie() {
		return charlie;
	}
	public void setCharlie(String charlie) {
		this.charlie = charlie;
	}
	public String getDelta() {
		return delta;
	}
	public void setDelta(String delta) {
		this.delta = delta;
	}
}
