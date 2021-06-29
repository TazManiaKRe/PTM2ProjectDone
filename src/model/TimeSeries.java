package model;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class TimeSeries {
	
	public String csvFileName;
	public ArrayList<Feature> table = new ArrayList<Feature>();
	public ArrayList<String> ColNames = new ArrayList<String>();
	public int NumOfRows = 0;
	

	public TimeSeries(String csvFileName) {
		this.csvFileName = csvFileName;
		try {
			Path pathToFile = Paths.get(csvFileName);
			BufferedReader br = Files.newBufferedReader(pathToFile,
					StandardCharsets.US_ASCII);
			String line = br.readLine();
			String[] titles = line.split(",");
			int k = 0;
			for (String title : titles) {
				Feature temp  = new Feature(title);
				this.ColNames.add(title);
				temp.name = "" + k;
				this.table.add(temp);
				k++;
			}
			line = br.readLine();
			while (line != null) {
				String[] data = line.split(",");
				for (int i = 0; i < data.length; i++) {
					float f = Float.parseFloat(data[i]);
					table.get(i).add_sample(f);
				}
				line = br.readLine();
			}
			br.close();
			this.NumOfRows = this.table.get(0).getSam().size();
		}catch (Exception e) {
			this.table = null;
		}
	}
	
	public TimeSeries() {
		super();
	}


	public TimeSeries(Feature ... features) {
		ArrayList<Feature> fs = new ArrayList<Feature>();
		for (Feature feature : features) {
			fs.add(feature);
		}
		this.table = fs;
		
	}
	public ArrayList<Feature> getTable() {return table;}

	public ArrayList<Float> getName(String s){
		for (Feature feature : table) {
			String n = feature.getName(); 
			if (n.equals(s)) {
				return feature.getSam();
			}
		}
		return null;	
	}
	
	public Feature getName2(String s){
		for (Feature feature : table) {
			String n = feature.getName(); 
			if (n.equals(s)) {
				return feature;	
			}
		}
		return null;	
	}
	public float getVal(String ColName , int time) {
		Feature f = this.getId(ColName);
		if (f == null) {
			return (Float) null;
		}
		if (time > f.getSam().size()) {
			return (Float) null;
		}
		return f.getSam().get(time);
	}


	public Feature getId(String s){
		for (Feature feature : table) {
			String n = feature.getId();
			if (n.equals(s)) {
				return feature;	
			}
		}
		return null;	
	}
	


	public float getMax(String selctedCol) {
		Feature f = getId(selctedCol);
		if (f!=null) {
			float max = f.getSam().get(0);
			for (int i = 1; i < f.sam.size(); i++) {
				if (f.sam.get(i) > max) {
					max = f.sam.get(i);
				}

			}
			return max;
		}
		return 0;
	}
	public ArrayList<Float> readLine(int temp){
		ArrayList<Float> line = new ArrayList<Float>();
		for (int i = 0; i < this.ColNames.size(); i++) {
			line.add(this.table.get(i).getSam().get(i));
		}
		return line;
	}

	public int getCol(String s) {
		for (int i = 0; i < ColNames.size(); i++) {
			if (ColNames.get(i).equals(s)) {
				return i;
			}
		}
		return -1;
	}
	public float getMin(String selctedCol) {
		Feature f = getId(selctedCol);
		if (f!=null) {
			float min = f.getSam().get(0);
			for (int i = 1; i < f.sam.size(); i++) {
				if (f.sam.get(i) < min) {
					min  = f.sam.get(i);
				}
				
			}
			return min;
		}
		
		return 0;
	}

		
}


