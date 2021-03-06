package viewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Loader;
import Algos.AnomalyReport;
import model.FeatureSettings;
import model.Model;
import model.TimeSeries;
import model.TimeSeriesAnomalyDetector;
import model.XmlComplete;
import model.XmlSettings;

public class ViewModel implements Observer 
{
	public HashMap<String, SimpleDoubleProperty> DisplayVar;
	public DoubleProperty rate;
	public DoubleProperty timeSlider;
	public IntegerProperty timeStep;
	public StringProperty videoTime;
	public StringProperty algoName;
	public StringProperty testPath,xmlPath;
	private Model model;
	private XmlSettings xs;
	private XmlComplete xc;
	public TimeSeries Train,Test;
	public TimeSeriesAnomalyDetector ad;
	public List<AnomalyReport> reports;
	public Runnable Play,Pause,Stop,Forward,Backward,DoubleForward,DoubleBackward,connect,disconnect;
	
	public ViewModel()
	{
		xs=null;
		xc=new XmlComplete();
		Train=null;
		Test=null;
		
		testPath = new SimpleStringProperty();
		xmlPath = new SimpleStringProperty();
		
		rate = new SimpleDoubleProperty(1.0);
		timeSlider = new SimpleDoubleProperty(0);
		timeStep = new SimpleIntegerProperty(0);
		videoTime=new SimpleStringProperty("00:00");
		model=new Model(timeStep,rate);
		DisplayVar = new HashMap<String, SimpleDoubleProperty>();
		algoName=new SimpleStringProperty();
		model.addObserver(this);
		
		rate.addListener((o,ov,nv)->{
			model.pause();
			model.setRate(nv.doubleValue());
			if(model.getIsFlightStart())
				model.play();
		});
		timeStep.addListener((o,ov,nv)->{
			Platform.runLater(()->{videoTime.set(toStringTime(nv.doubleValue()/10));});
		});
		
		
		Play=()->model.play();
		Pause=()->model.pause();
		Stop=()->model.stop();
		Forward=()->model.forward();
		DoubleForward=()->model.doubleForward();
		Backward=()->model.backward();
		DoubleBackward=()->model.doubleBackward();
		connect=()->model.Connect();
		disconnect=()->model.disConnect();
		
		
	}

	public ArrayList<String> getColTitels(){
		if (Test != null) 
			return Test.ColNames;
		return null;
	}
	
	public DoubleProperty getProperty(String name) { return this.DisplayVar.get(name);}
	public HashMap<String, SimpleDoubleProperty> getDisplayVar() { return DisplayVar;}
	public DoubleProperty getRate() { return rate;}
	public StringProperty getAlgoName() {return algoName;}
	public XmlSettings getXs() {return xs;}
	public XmlComplete getXc() {return xc;}
	public TimeSeries getTrain() {return Train;}
	public TimeSeries getTest() {return Test;}
	public TimeSeriesAnomalyDetector getAd() {return ad;}


	public void setDisplayVar(HashMap<String, SimpleDoubleProperty> displayVar) {DisplayVar = displayVar;}
	public void setRate(double rate) {this.rate.set(rate);}
	public void setAlgoName(StringProperty algoName) {this.algoName = algoName;}
	public void setXs(XmlSettings xs) {this.xs = xs;}
	public void setXc(XmlComplete xc) {this.xc = xc;}
	public void setTrain(TimeSeries train) {Train = train;}
	public void setTest(TimeSeries test) {Test = test;}
	public void setAd(TimeSeriesAnomalyDetector ad) {this.ad = ad;}


	public String toStringTime(Double object) {
        long seconds = object.longValue();
        long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        long remainingseconds = seconds - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d", minutes) + ":" + String.format("%02d", remainingseconds);
    }
	

	@Override
	public void update(Observable o, Object arg) {
		if (o == model && arg.equals("aileron")) {
			Platform.runLater(()->DisplayVar.get("aileron").setValue(model.getAileron()));}
		if (o == model && arg.equals("elevators")) {
			Platform.runLater(()->DisplayVar.get("elevator").setValue(model.getElevators()));}
		if (o == model && arg.equals("rudder")) {
			Platform.runLater(()->DisplayVar.get("rudder").setValue(model.getRudder()));}
		if (o == model && arg.equals("throttle")) {
			Platform.runLater(()->DisplayVar.get("throttle").setValue(model.getThrottle()));}
		if (o == model && arg.equals("yaw")) {
			Platform.runLater(()->DisplayVar.get("yaw").setValue(model.getYaw()));}
		if (o == model && arg.equals("height")) {
			Platform.runLater(()->DisplayVar.get("height").setValue(model.getHeigth()));}
		if (o == model && arg.equals("speed")) {
			Platform.runLater(()->DisplayVar.get("speed").setValue(model.getSpeed()));}
		if (o == model && arg.equals("direction")) {
			Platform.runLater(()->DisplayVar.get("direction").setValue(model.getDirection()));}
		if (o == model && arg.equals("roll")) {
			Platform.runLater(()->DisplayVar.get("roll").setValue(model.getRoll()));}
		if (o == model && arg.equals("pitch")) {
			Platform.runLater(()->DisplayVar.get("pitch").setValue(model.getPitch()));}	
	}
	
	public void openXml()
	{
		FileChooser fc = new FileChooser();
		fc.setTitle("chose");
		fc.setInitialDirectory(new File("./resources"));
		ExtensionFilter ef = new ExtensionFilter("XML Files (*.xml)","*.xml");
		fc.getExtensionFilters().add(ef);
		File chosen = fc.showOpenDialog(null);
		if(chosen!=null) {
			loadXml("resources/"+chosen.getName());
			xmlPath.set("resources/"+chosen.getName());
		}
	}
	
	
	protected void loadXml(String name) {
		
		xs = xc.LoadSettingsFromClient(name);
		if (xs != null && xs.getHost() != null && xs.getPort() != 0 && xs.getTO() != 0.0) {
			model.setClientSettings(xs);
			for (FeatureSettings setting : xs.getHf()) {
				DisplayVar.put(setting.getReal(), new SimpleDoubleProperty());
			}
			ArrayList<Double> checkSpeed = new ArrayList<Double>(Arrays.asList(0.25,0.5,0.75,1.0,1.25,1.5,1.75,2.0));
			if (checkSpeed.contains(xs.getTO())) {
				setRate(xs.getTO());
			}
			else {
				Alert a = new Alert(AlertType.WARNING);
				a.setHeaderText("no good speed");
				a.setContentText("set 1.0");
				a.showAndWait();
				setRate(1.0);
			}	
		}
	}
	
	public void openTrainCSVFile() {
		if (this.xs!=null) {
			FileChooser fc = new FileChooser();
			fc.setTitle("choose");
			fc.setInitialDirectory(new File("./resources"));
			ExtensionFilter ef = new ExtensionFilter("CSV Files (*.csv)","*.csv");
			fc.getExtensionFilters().add(ef);
			File chosen = fc.showOpenDialog(null);
			if(chosen!=null) {
				TrainloadCsv("resources/"+chosen.getName());
			}
		}
		else {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Xml no good");
			a.setContentText("need good xml");
			a.showAndWait();
			}
	}
	
	protected void TrainloadCsv(String nv) {
			this.Train = new TimeSeries(nv);
			if (this.Train.table == null) {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("no good csv");
				a.setContentText("no good csv");
				a.showAndWait();
				this.Train = null;
			}
			else {
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("csv good");
				a.setContentText("good.. load..");
				a.showAndWait();
				model.setTrain(Train);
			}
	}
	
	public void openTestCsv() {
		if (Train!= null) {
			FileChooser fc = new FileChooser();
			fc.setTitle("choose");
			fc.setInitialDirectory(new File("./resources"));
			ExtensionFilter ef = new ExtensionFilter("CSV Files (*.csv)","*.csv");
			fc.getExtensionFilters().add(ef);
			File chosen = fc.showOpenDialog(null);
			if(chosen!=null) {
				TestloadCsv("resources/"+chosen.getName());
				testPath.set("resources/"+chosen.getName());
			}
		}
		else {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("test csv no good");
			a.setContentText("no good csv");
			a.showAndWait();
		}
	}
	
	protected void TestloadCsv(String nv) {
		this.Test = new TimeSeries(nv);
		if (this.Test.table == null) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("no good");
			a.setContentText("try again");
			a.showAndWait();
			this.Test = null;
		}
		else {
			
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("csv good");
			a.setContentText("good");
			a.showAndWait();
			model.setTe(Test);
		}	
	}
	
	public void openCLASSFile() {
		if (Test!= null) {
			FileChooser fc = new FileChooser();
			fc.setTitle("choose");
			fc.setInitialDirectory(new File("./Algorithms/"));
			ExtensionFilter ef = new ExtensionFilter("Class Files (*.class)","*.class");
			fc.getExtensionFilters().add(ef);
			File chosen = fc.showOpenDialog(null);
			if(chosen!=null)
			{
				String path  = chosen.getPath().substring(0, 27);
				String fixPath =  FixPath(path);
				String name ="model."+chosen.getName().substring(0, chosen.getName().length()-6);
				loadAnomalyAlgo(fixPath,name);
				algoName.set(name);
				if (this.ad != null) {
					model.setAnomalyDetector(this.ad);
					LearnData();
					reports =  DetectAnomalies();
				}
			}
		}
		else {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("no good class");
			a.setContentText("test before");
			a.showAndWait();
		}
	}
	
	private String FixPath(String path) {

		String s = "";
		for (char c : path.toCharArray()) {
			if (c == '\\') {
				s += '/' ;
			}else {s += c ;}
		}
		return s;
	}

	protected void loadAnomalyAlgo(String p, String name) {
		try {
			this.ad = new Loader(p, name).getAlg();
		} catch (Exception e) {
			algoName.set("");
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("load fail");
			a.setContentText("try again");
			a.showAndWait();
			this.ad = null;
		}
		if (this.ad != null) {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Algo Success");
			a.setContentText("Success Algo Loading");
			a.showAndWait();
		}	
	}
	
	public String getCorllated(String selctedCol) {
		// TODO Auto-generated method stub
		String cor = model.getCor(selctedCol);
		if (cor != null) {
			return cor;
		}
		return null;
	}
	
	public void PaintTrainPoints(String selctedCol, String corlleatedCol, Series seriesC) {
					for (int i = 0; i < Train.NumOfRows; i++) {
						float y1 = Train.getVal(selctedCol, i);
						float y2 = Train.getVal(corlleatedCol, i);
						seriesC.getData().add(new XYChart.Data(y1,y2));
					}
	}

	public void PaintAlgo(String selctedCol, String corlleatedCol,Series Algo) {
		Series s = model.algos(selctedCol,corlleatedCol);
		if (s != null) {
				for (int i = 0; i < s.getData().size(); i++) {
					Algo.getData().add(s.getData().get(i));
				}
		}
		else {
			Algo.getData().clear();
		}
		
	}
	
	public void LearnData() {model.learnData();}
	public List<AnomalyReport> DetectAnomalies() {return model.DetectData();}
	
	public void paintFeature(String selctedCol, Number nv,Series s) {
			if (nv.intValue() < Test.NumOfRows) {
				
				float y = Test.getVal(selctedCol, nv.intValue());
				Platform.runLater(()->{
					s.getData().add(new XYChart.Data(nv.intValue(),y));
				});
						
			}
	}
	
	public void PaintTestPoints(String selctedCol, String corlleatedCol, int intValue, Series anomaly_Flight, Series anomalies) {
				if (intValue < Test.NumOfRows) {
					if (!isRepot(selctedCol,corlleatedCol,intValue)) {
						Platform.runLater(()->{
							anomaly_Flight.getData().add(new XYChart.Data(Test.getVal(selctedCol, intValue),Test.getVal(corlleatedCol, intValue)));
						});
						if (anomaly_Flight.getData().size() > 30) {
							Platform.runLater(()->{
								anomaly_Flight.getData().remove(0);
								anomalies.getData().clear();
							});
						}
					}
					else {
						Platform.runLater(()->{
							anomalies.getData().add(new XYChart.Data(Test.getVal(selctedCol, intValue),Test.getVal(corlleatedCol, intValue)));

						});
						
					}
				}
	}
	
	private boolean isRepot(String selctedCol, String corlleatedCol, int intValue) {
		if (reports != null && reports.size() != 0) {
			String discription = selctedCol + "-" +corlleatedCol;
			for (AnomalyReport anomalyReport : reports) {
				if (anomalyReport.des.equals(discription) && (int)anomalyReport.stenp == intValue) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isRepotZscore(String selctedCol, int intValue) {
		if (reports != null && reports.size() != 0) {
			String discription = selctedCol;
			for (AnomalyReport anomalyReport : reports) {
				if (anomalyReport.des.equals(discription) && (int)anomalyReport.stenp == intValue) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void FilluntillNow(String selctedCol, Series seriesA) {
			for (int i = 0; i < timeStep.intValue() && timeStep.intValue() < Test.NumOfRows; i++) {
				final int j = i;
					seriesA.getData().add(new XYChart.Data(j,Test.getVal(selctedCol,j)));
			}
}

	public void PaintZscoreTrain(String selectedCol, Series seriesRegularFlight) {
		for (int i = 0; i < Train.NumOfRows; i++) {
			float y1 = Train.getVal(selectedCol, i);
			seriesRegularFlight.getData().add(new XYChart.Data(i,y1));
		}
		
	}

	public void PaintTestZscorePoints(String selectedCol, int intValue, Series seriesAnomaliesFlight,Series seriesAnomaliesPoints) {
		if (intValue < Test.NumOfRows) {
			if (!isRepotZscore(selectedCol,intValue)) {
				Platform.runLater(()->{
					seriesAnomaliesFlight.getData().add(new XYChart.Data(intValue,Test.getVal(selectedCol, intValue)));
				});
				if (seriesAnomaliesFlight.getData().size() > 20) {
					Platform.runLater(()->{
						seriesAnomaliesFlight.getData().remove(0);
						seriesAnomaliesPoints.getData().clear();
					});
				}
			}
			else {
				Platform.runLater(()->{
					seriesAnomaliesPoints.getData().add(new XYChart.Data(intValue,Test.getVal(selectedCol, intValue)));

				});
				
			}
		}
		
		
	}

	public double getCoraleted(String selectedCol, String corlleatedCol) {
		return model.getCoraletion(selectedCol,corlleatedCol);
	}
	
}