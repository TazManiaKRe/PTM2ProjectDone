package model;

import java.util.ArrayList;

public class Feature{
	public ArrayList<Float> sam = new ArrayList<Float>();
	public String name;
	public int size;
	public String id;

	public String getId() {return id;}
	
	protected void setId(String id) {this.id = id;}

	public void add_sample(Float sam) {
		this.sam.add(sam);
		this.size++;
	}

	public Feature() {
		super();
	}

	public Feature(ArrayList<Float> samples) {this.sam = samples;}
	public Feature(String name) {this.id = name;}

	public ArrayList<Float> getSam() {return sam;}

	public void setSam(ArrayList<Float> sam) {
		this.sam = sam;
	}

	public String getName() {return name;}

	public void setName(String name) {this.name = name;}

}
