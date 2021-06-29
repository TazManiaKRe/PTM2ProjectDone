package model;

import java.io.Serializable;

public class FeatureSettings  implements Serializable{
		public FeatureSettings() {};
		private String real;
		private String name;
		private double min;
		private double max;

		public String getReal() {return real;}
		public void setReal(String real) {this.real = real;}


		@Override
		    public String toString() {
		        return "FeatureSettings [real col =" + real + ", as name=" + name
		                + ", min=" + min + ", max=" + max  + "]";
		    }

	public void setMin(double min) {this.min = min;}
	public double getMax() {return max;}
		public String getName() {return name;}
		public void setName(String name) {this.name = name;}
		public double getMin() {return min;}



		public void setMax(double max) {this.max = max;}

}
