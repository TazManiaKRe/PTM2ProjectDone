package view;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart.Series;
import view.viewlist.Viewlist;
import viewModel.ViewModel;
import view.openfiles.Openfiles;
import view.graphs.Graphs;
import view.joystick.Joystick;
import view.buttons.Buttons;

public class WindowController {

	@FXML public Viewlist viewlist;
	@FXML public Openfiles openfiles;
	@FXML public Graphs graphs;
	@FXML public Joystick joystick;
	@FXML public Buttons buttons;
	
	public Series s1;
	public Series s2;
	public Series s3;
	public Series s4;
	public Series s5;
	public Series s6;
	
	String sel, coll;
	
	
	public void init() {
		ViewModel vm = new ViewModel();
		
		buttons.coll.onPlay=vm.Play;
		buttons.coll.onPause=vm.Pause;
		buttons.coll.onStop=vm.Stop;
		buttons.coll.clearGraphs=()->clearSeries(s5, s6, s2, s1);
		buttons.coll.clearSelect=()->viewlist.hf.getSelectionModel().clearSelection();
		buttons.coll.onForward=vm.Forward;
		buttons.coll.onDoubleForward=vm.DoubleForward;
		buttons.coll.onBackward=vm.Backward;
		buttons.coll.onDoubleBackward=vm.DoubleBackward;
		openfiles.coll.connect=vm.connect;
		openfiles.coll.disconnect=vm.disconnect;
		
		openfiles.coll.xmlFile=()->vm.openXml();
		openfiles.coll.classFile=()->vm.openCLASSFile();
		openfiles.coll.trainCSVFile=()->vm.openTrainCSVFile();
		openfiles.coll.testCSVFile=()->vm.openTestCsv();
		
		s1 = new Series();
		s1.setName("Feature");
		s2 = new Series();
		s2.setName("Correlated");;
		s3 = new Series();
		s3.setName("Anomaly Algo");
		s4 = new Series();
		s4.setName("Regular Flight");
		s5 = new Series();
		s5.setName("Anomaly Flight");
		s6 = new Series();
		s6.setName("Anomalies Points");
		
		
		
		graphs.fchart.getData().add(s1);
		graphs.fchart.setAnimated(false);
		graphs.cor.getData().add(s2);
		
		graphs.cor.setAnimated(false);
		graphs.algos.getData().addAll(s3, s4, s5, s6);
		graphs.algos.setAnimated(false);
		
		sel =null;
		coll =null;

		
		vm.rate.bindBidirectional(buttons.speed.valueProperty());
		buttons.time.valueProperty().bindBidirectional(vm.timeStep);
		buttons.video.textProperty().bind(vm.videoTime);
		
		vm.timeStep.addListener((o,ov,nv)->{
			if (nv.intValue() == vm.getTest().NumOfRows) {
				if(Thread.currentThread().getName().equals("JavaFx Application Thread"))
					clearSeries(s3, s5, s6, s2, s1, s4);
				else
					Platform.runLater(()->clearSeries(s3, s5, s6, s2, s1, s4));
			}
			if(sel !=null) {
				if(ov.intValue()+1==nv.intValue()) 
						vm.paintFeature(sel, nv, s1);
				else if(nv.intValue() != 0) {
					clearSeries(s1);
					vm.FilluntillNow(sel, s1);
				}
			
				else
					Platform.runLater(()->clearSeries(s1));
			
			if(coll !=null) {
				if(ov.intValue()+1==nv.intValue())
					vm.paintFeature(coll, nv, s2);
				else if(nv.intValue() != 0) {
					clearSeries(s2);
					vm.FilluntillNow(coll, s2);
				}
			}
			else {
				if(Thread.currentThread().getName().equals("JavaFx Application Thread"))
					clearSeries(s2);
				}
			if (sel != null && coll != null && vm.getAd() != null && vm.getAd().getName().equals("Linear")) {
				vm.PaintTestPoints(sel, coll, nv.intValue(), s5, s6);
 			}
			else if (sel != null && vm.getAd() != null && vm.getAd().getName().equals("Zscore")) {
				vm.PaintTestZscorePoints(sel, nv.intValue(), s5, s6);
			}
			else if(vm.getAd() != null && vm.getAd().getName().equals("Hybrid")) {
				if((sel !=null&& coll ==null) || (sel !=null && coll !=null &&vm.getCoraleted(sel, coll)<0.5))
					vm.PaintTestZscorePoints(sel, nv.intValue(), s5, s6);
				else if((sel !=null && coll !=null &&vm.getCoraleted(sel, coll)>=0.5))
					vm.PaintTestPoints(sel, coll, nv.intValue(), s5, s6);
					
			}
			if(nv.intValue()==0) {
				Platform.runLater(()->{
					viewlist.hf.getSelectionModel().clearSelection();
					graphs.cor.setTitle(null);
				});
				
			}
			}
		});
		
		vm.testPath.addListener((o,ov,nv)->{
			ArrayList<String> titles = vm.getColTitels();
			if (titles != null) {
				ObservableList<String> list = FXCollections.observableArrayList(titles);
				viewlist.hf.setItems(list);
				buttons.time.setMax(vm.getTrain().NumOfRows);
				buttons.time.setMin(0);
			}
		});
		
		vm.xmlPath.addListener((o,ov,nv)->{
			if(vm.getXs()!=null) {
				joystick.SetMaxMinForSliders(
						vm.getXs().getSetting("rudder").getMax(),
						vm.getXs().getSetting("rudder").getMin(),
						vm.getXs().getSetting("throttle").getMax(),
						vm.getXs().getSetting("throttle").getMin());
				joystick.setMaxMinForClock(
					vm.getXs().getSetting("direction").getMax(),
					vm.getXs().getSetting("direction").getMin(),
					vm.getXs().getSetting("height").getMax(),
					vm.getXs().getSetting("height").getMin(),
					vm.getXs().getSetting("pitch").getMax(),
					vm.getXs().getSetting("pitch").getMin(),
					vm.getXs().getSetting("roll").getMax(),
					vm.getXs().getSetting("roll").getMin(),
					vm.getXs().getSetting("speed").getMax(),
					vm.getXs().getSetting("speed").getMin(),
					vm.getXs().getSetting("yaw").getMax(),
					vm.getXs().getSetting("yaw").getMin());
				
				joystick.aileron.bind(vm.DisplayVar.get("aileron"));
				joystick.elevator.bind(vm.DisplayVar.get("elevator"));
				joystick.rudder.valueProperty().bind(vm.DisplayVar.get("rudder"));
				joystick.throttle.valueProperty().bind(vm.DisplayVar.get("throttle"));
				joystick.dir.valueProperty().bindBidirectional(vm.DisplayVar.get("direction"));
				joystick.altitude.valueProperty().bindBidirectional(vm.DisplayVar.get("heigth"));
				joystick.pitch.valueProperty().bindBidirectional(vm.DisplayVar.get("pitch"));
				joystick.roll.valueProperty().bindBidirectional(vm.DisplayVar.get("roll"));
				joystick.speed.valueProperty().bindBidirectional(vm.DisplayVar.get("speed"));
				joystick.yaw.valueProperty().bindBidirectional(vm.DisplayVar.get("yaw"));
			}
		});
		vm.algoName.addListener((o,ov,nv)->{
			if(vm.getAd()!=null) {
				if(nv.length() > 6) {
					graphs.algos.setTitle(nv.substring(6));
					if(viewlist.hf.getSelectionModel()!=null) {
						String index =viewlist.hf.getSelectionModel().getSelectedItem();
						viewlist.hf.selectionModelProperty().get().clearSelection();
						graphs.cor.setTitle(null);
					}
				}
			}
		});
		joystick.aileron.addListener((o,ov,nv)->{
			double max = vm.getXs().getSetting("aileron").getMax();
			double min = vm.getXs().getSetting("aileron").getMin();
			double a=joystick.canvas.getLayoutX()-joystick.canvas.getRadius();
			double b=joystick.canvas.getLayoutX()+joystick.canvas.getRadius();
			nv=joystick.NormlaizeJoystic((double)nv.doubleValue() ,max,min,a,b);
			joystick.moving.setLayoutX((double) nv);
		});
		
		joystick.elevator.addListener((o,ov,nv)->{
			double max = vm.getXs().getSetting("elevator").getMax();
			double min = vm.getXs().getSetting("elevator").getMin();
			double a=joystick.canvas.getLayoutY()-joystick.canvas.getRadius();
			double b=joystick.canvas.getLayoutY()+joystick.canvas.getRadius();
			nv=joystick.NormlaizeJoystic((double)nv ,max,min,a,b);
			joystick.moving.setLayoutY((double) nv);
		});
		
		viewlist.hf.getSelectionModel().selectedItemProperty().addListener((o, ov, nv)->{
			clearSeries(s3, s5, s6, s2, s1, s4);
			sel =nv;
			
			graphs.fchartx.setUpperBound(vm.getTest().NumOfRows);
			graphs.fchartx.setLowerBound(0);
			graphs.fchartx.setAutoRanging(false);
			graphs.fchartx.setTickUnit(10);
			
			graphs.fcharty.setUpperBound((int)vm.getTest().getMax(sel)+1);
			graphs.fcharty.setLowerBound((int)vm.getTest().getMin(sel)-1);
			graphs.fcharty.setAutoRanging(false);
			graphs.fcharty.setTickUnit(10);
			
			
			graphs.fchart.setTitle(sel);
			if(sel !=null) {
				vm.FilluntillNow(sel, s1);
				coll =vm.getCorllated(sel);
				if(coll ==null) {
					graphs.cor.setTitle("");
					clearSeries(s2);
				}
				else {
					graphs.axis.setUpperBound(vm.getTest().NumOfRows);
					graphs.axis.setLowerBound(0);
					graphs.axis.setAutoRanging(false);
					graphs.axis.setTickUnit(10);
					
					graphs.axis1.setUpperBound((int)vm.getTest().getMax(coll)+1);
					graphs.axis1.setLowerBound((int)vm.getTest().getMin(coll)-1);
					graphs.axis1.setAutoRanging(false);
					graphs.axis1.setTickUnit(10);
					
					graphs.cor.setTitle(coll);
					clearSeries(s2);
					vm.FilluntillNow(coll, s2);
				}
			if(vm.getAd() != null && coll != null && vm.getAd().getName().equals("Linear")) {
				clearSeries(s4, s3);
				vm.PaintTrainPoints(sel, coll, s4);
			    vm.PaintAlgo(sel, coll, s3);
			}
			if(vm.getAd() != null && vm.getAd().getName().equals("Zscore")) {
				clearSeries(s4, s3);
				vm.PaintZscoreTrain(sel, s4);
			    vm.PaintAlgo(sel, coll, s3);
			}
			if(vm.getAd() != null && vm.getAd().getName().equals("Hybrid")) {
				clearSeries(s4, s3);
				if((sel !=null&& coll ==null) || (sel !=null && coll !=null &&vm.getCoraleted(sel, coll)<0.5))
					vm.PaintZscoreTrain(sel, s1);
				else if((sel !=null && coll !=null &&vm.getCoraleted(sel, coll)>=0.5))
					vm.PaintTrainPoints(sel, coll, s4);
			    vm.PaintAlgo(sel, coll, s3);
			}
			}
		});
		
		
	}
	public void clearSeries(Series...series) {
		for (Series s : series) {
				s.getData().clear();
		}
	}
	
}
