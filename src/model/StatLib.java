package model;

import Algos.AnomalyReport;

import java.util.ArrayList;
import java.util.List;

public class StatLib {

	public static float[] al(List<Float> list) {
		float[] temp = new float[list.size()];
		int i = 0;
		for (Float value: list) {
		   temp[i++] = value;
		}	
		return temp;
	
	}	
	// simple average
	public static float avg(float[] x){
		float sum =0;
		for (float f : x) {
			sum += f ;
		}
		
		return sum / x.length;
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float avg = avg(x);
		float temp = 0;
		for (float f : x) {
			temp += (f-avg)*(f-avg);
		}
		
		return temp / (x.length);
	}



	public static float cov(float[] x, float[] y){
		float avgx = avg(x);
		float  avgy = avg(y);
		float  sum = 0;
		for (int i = 0; i < y.length; i++) {
			sum += (((x[i] -avgx) * (y[i] - avgy))); 
		}
		
		return sum/x.length;
	}



	public static float pearson(float[] x, float[] y){
		float covxy = cov(x, y);
		float varx = var(x);
		float vary = var(y);
		double s1 = Math.sqrt(varx);
		double s2 = Math.sqrt(vary);
		
		return (float) (covxy / (s1 * s2));
	}



	public static Line linear_reg(Point[] points){
		float[] x = new float[points.length];
		float[] y = new float[points.length];
		int i = 0;
		for (Point p : points) {
			x[i] = p.x;
			y[i] = p.y;
			i++;
		}
		float xavg = avg(x);
		float yavg = avg(y);
		float xvar = var(x);
		float covxy = cov(x, y);
		float a = covxy/xvar;
		float b = yavg - (a * xavg);
		Line l = new Line(a, b);
		
		return l;
	}



	public static float dev(Point p,Point[] points){
		Line lrg = linear_reg(points);
		
		return Math.abs(lrg.f(p.x) - p.y);
	}


	public static float dev(Point p,Line l){
		return Math.abs(l.f(p.x) - p.y);
	}

	public static IfGood FindMatch(TimeSeries ts,double tree){
		ArrayList<MatchFeature> mfl = new ArrayList<MatchFeature>();
		ArrayList<Feature> noMatch = new ArrayList<Feature>();
		float max =0;
		MatchFeature mf = null ;
		for (int i = 0; i < ts.getTable().size(); i++) {
			Feature f1 = ts.getTable().get(i);
			for (int j = i+1; j < ts.getTable().size(); j++) {
				if (i != j) {
					Feature f2 = ts.getTable().get(j);
					float cor = Math.abs(pearson(al(f1.getSam()), al(f2.getSam())));
					if(cor > max && cor > tree) {
						max = cor;
						mf = new MatchFeature(f1.getName(), f2.getName(), cor);
					}
				}		
			}
			if (mf != null) {mfl.add(mf);}
			else {noMatch.add(f1);}
			max = 0;
			mf= null;
		}
		return new IfGood(mfl, noMatch);
	}


	public static boolean isgood(ArrayList<AnomalyReport> arl, AnomalyReport ar) {
		for (AnomalyReport anomalyReport : arl) {
			if (anomalyReport.des.equals(ar.des) &&
					anomalyReport.stenp == ar.stenp)
			{
				return true;
			}
			
		}
		return false;
	}
	
	
	public static String ReverseString(String s) {

	        byte[] strAsByteArray = s.getBytes();
	        byte[] result = new byte[strAsByteArray.length];
	        for (int i = 0; i < strAsByteArray.length; i++){
	            result[i] = strAsByteArray[strAsByteArray.length - i - 1];}
	        return new String(result);
	    
	}
	
	public static Point[] points_gen(float[] x, float[] y) {
		Point[] parr = new Point[x.length];
		for (int i = 0; i < parr.length; i++) {
			parr[i] = new Point(x[i], y[i]);
		}
		return parr;
	}
}
