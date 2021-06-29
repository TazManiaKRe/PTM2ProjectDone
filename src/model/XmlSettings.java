
package model;

import java.io.Serializable;
import java.util.ArrayList;

public class XmlSettings implements Serializable{
	private ArrayList<FeatureSettings> hf;
	protected String host; 
	protected int port;
	protected double TO;

	public XmlSettings() {}

	public String getHost() {return host;}
	public void setHost(String host) {this.host = host;}
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
	public double getTO() {return TO;}
	public void setTO(double TO) {this.TO = TO;}
	public ArrayList<FeatureSettings> getHf() {return hf;}
	public void setHf(ArrayList<FeatureSettings> hf) {this.hf = hf;}

	 public String toString() {
		 	String output ="Xml- " + host + " = Host " + port + " = Port " + TO + " = read" ;
		 	
	        for (FeatureSettings featureSettings : hf) {
				output += featureSettings.toString();
			}
	        
	        output += "-";
	        return output;
	        
	    }
	public FeatureSettings getSetting(String name) {
		try {
			for (FeatureSettings featureSettings : hf) {
				if (featureSettings.getReal().equals(name)) {
					return featureSettings;
				}
			}
		}catch (Exception e) {System.out.println("no good name");}
		return null;
	}



	public String getAssociate(String name) {
		for (FeatureSettings featureSettings : hf) {
			if (featureSettings.getReal().equals(name)) {
				return featureSettings.getName();
			}
		}
		System.out.println("no good name");
		return null;
	}

}