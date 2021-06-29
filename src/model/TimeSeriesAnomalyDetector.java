package model;

import java.util.List;

import Algos.AnomalyReport;
import javafx.scene.chart.XYChart.Series;

public interface TimeSeriesAnomalyDetector {
	
	public void learnNormal(TimeSeries ts);
	public List<AnomalyReport> detect(TimeSeries ts);
	public Series  paint(String ...strings );
	public String getName();
}
