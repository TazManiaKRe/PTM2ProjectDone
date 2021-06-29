package model;

import model.Line;

public class CorrelatedFeatures {
	public final String f1, f2;
	public final float corr;
	public final Line reg;
	public final float tree;

	
	public CorrelatedFeatures(String f2, String f1, float corr, Line reg, float tree) {
		this.f1 = f2;
		this.f2 = f1;
		this.corr = corr;
		this.reg = reg;
		this.tree = tree;
	}
}
