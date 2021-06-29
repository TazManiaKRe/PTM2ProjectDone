package model;

public class MatchFeature {

	public String f1;
	public String f2;
	public float corr;
	
	
	public MatchFeature(String a, String b , float num) {
		this.f1 = a;
		this.f2 = b;
		this.corr = num;
	}
	
	protected String getF1() {return f1;}
	protected void setF1(String f1) {this.f1 = f1;}
	protected String getF2() {return f2;}
	protected void setF2(String f2) {this.f2 = f2;}
	protected float getCorr() {return corr;}
	protected void setCorr(float corr) {this.corr = corr;}
	
}
