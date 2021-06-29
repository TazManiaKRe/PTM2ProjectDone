package view.graphs;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;

public class Graphs extends AnchorPane

{
	public final GraphsController coll;
	public LineChart algos;
	public LineChart cor;
	public LineChart fchart;
	public NumberAxis fchartx;
	public NumberAxis fcharty;
	public NumberAxis axis;
	public NumberAxis axis1;
	public NumberAxis axis2;
	public NumberAxis axis3;
	
	public Graphs() 
	{
		FXMLLoader fxl = new FXMLLoader();
		AnchorPane ap= null;
		try {
			ap = fxl.load(getClass().getResource("Graphs.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}
		
		if(ap!=null) {
			coll =fxl.getController();
			fchart = coll.FeatureChart;
			cor = coll.CorrelatedChart;
			algos = coll.algoChart;
			fchartx = coll.FchartX;
			fcharty = coll.FchartY;
			axis = coll.CorxAxis;
			axis1 = coll.CoryAxis;
			axis3 = coll.algoAxisX;
			axis2 = coll.algoAxisY;
			this.getChildren().add(ap);
		}
		else
			coll =null;
	}

}
