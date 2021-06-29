package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import Algos.AnomalyReport;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Model extends Observable {

	private double aileron ;
	private double elevators;
	private double rudder;
	private double throttle;
	private double pitch;
	private double yaw;
	private double direction ;
	private double speed;
	private double roll;
	private double height;
	private boolean hasPause=true;
	private boolean hasStop = true;
	private boolean flagConnect=false;
	private boolean flightStart=false;

	private Timer t = null;
	private Connact fg;
	protected IntegerProperty timee;
	protected int rate = 0;
	protected TimeSeriesAnomalyDetector ad;
	protected XmlSettings ClientSettings;
	protected TimeSeries Train = null;
	protected TimeSeries te = null;

	public Model(IntegerProperty time,DoubleProperty ra) {
		this.timee =time;
		this.rate=(int) (100/ra.doubleValue());
	}
	
	public void play() {
		if(t==null) {
			if(te !=null) {
				flightStart=true;
				hasPause=false;
				hasStop=false;
				t=new Timer();
				t.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						if(flagConnect) {
							if(timee.getValue()+1> te.NumOfRows) {
								stop();
							}
							else{
								updateValues(timee.getValue());
								fg.send(te.readLine(timee.get()));
								timee.set(timee.getValue()+1);
							}
						}
						else {
							if(timee.getValue()+1> te.NumOfRows) {
								stop();
							}else {
							updateValues(timee.getValue());
							timee.set(timee.getValue()+1);}
						}
					}
				}, 0,this.getRate());
			}
			else {
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Cant do");
				a.setContentText("load files.");
				a.showAndWait();
			}
		}
	}
	public void stop() {
		if(te !=null) {
			if(hasStop==false) {
				if(hasPause==false)t.cancel();
				t=null;
				timee.set(0);
				hasStop=true;
				resetValues();
			}
		}
	}
	public void forward() {
		if(te !=null && timee.get()+ 20<= te.NumOfRows)
			timee.set(timee.get()+ 20);
	}

	public void pause() {
		if(te !=null) {
			if(hasStop==false && hasPause==false && flightStart == true) {
				t.cancel();
				hasPause=true;
				t=null;
			}
		}
	}


	public void doubleForward() {
		if(te != null && timee.get()+40<= te.NumOfRows)
			timee.set(timee.get()+40);
	}
	public void backward() {
		if(timee.get()-20>=0)
			timee.set(timee.get()-20);
	}
	public void doubleBackward() {
		if(timee.get()-40>=0)
			timee.set(timee.get()-40);
	}
	

	public void setRate(double rate) { this.rate=(int) (100/rate);}
	public int getRate() {return this.rate;}
	
	public void setAlieron(double val) {this.aileron=val; this.setChanged(); this.notifyObservers("aileron");}
	public void setElevators(double val) {this.elevators=val; this.setChanged();this.notifyObservers("elevators");}
	public void setRudder(double val) {this.rudder=val; this.setChanged();this.notifyObservers("rudder");}
	public void setThrottle(double val) {this.throttle=val;this.setChanged();this.notifyObservers("throttle");}
	public void setYaw(double val) {this.yaw=val;this.setChanged();this.notifyObservers("yaw");}
	public void setPitch(double val) {this.pitch=val;this.setChanged();this.notifyObservers("pitch");}
	public void setRoll(double val) {this.roll=val;this.setChanged();this.notifyObservers("roll");}
	public void setheight(double val) {this.height=val;this.setChanged();this.notifyObservers("height");}
	public void setDirection(double val) {this.direction=val;this.setChanged();this.notifyObservers("direction");}
	public void setSpeed(double val) {this.speed=val;this.setChanged();this.notifyObservers("speed");}
	public void setClientSettings(XmlSettings cl) {ClientSettings = cl;}
	public void setTrain(TimeSeries tr) { Train = tr;}
	public void setTe(TimeSeries te) {
		this.te = te;resetValues();}
	
	public double getAileron() {return aileron;}
	public double getRudder() {return rudder;}
	public double getElevators() {return elevators;}
	public double getThrottle() {return throttle;}
	public double getPitch() {return pitch;}
	public double getYaw() {return yaw;}
	public double getDirection() {return direction;}
	public double getSpeed() {return speed;}
	public boolean getIsFlightStart() {return flightStart;}

	public double getRoll() {return roll;}
	public double getHeigth() {return height;}
	public XmlSettings getClientSettings() {return ClientSettings;}
	


	public void updateValues(int i) {
		setAlieron(te.getVal(getClientSettings().getAssociate("aileron"),i));
		setElevators(te.getVal(getClientSettings().getAssociate("elevator"),i));
		setRudder(te.getVal(getClientSettings().getAssociate("rudder"), i));
		setThrottle(te.getVal(getClientSettings().getAssociate("throttle"), i));
		setYaw(te.getVal(getClientSettings().getAssociate("yaw"), i));
		setPitch(te.getVal(getClientSettings().getAssociate("pitch"), i));
		setRoll(te.getVal(getClientSettings().getAssociate("roll"), i));
		setheight(te.getVal(getClientSettings().getAssociate("height"), i));
		setDirection(te.getVal(getClientSettings().getAssociate("direction"), i));
		setSpeed(te.getVal(getClientSettings().getAssociate("speed"), i));
	}
	
	public void resetValues() {
		double x = (this.ClientSettings.getSetting("aileron").getMax()+this.ClientSettings.getSetting("aileron").getMin())/2;
		setAlieron(x);
		x=(this.ClientSettings.getSetting("elevator").getMax()+this.ClientSettings.getSetting("elevator").getMin())/2;
		setElevators(x);
		setRudder(0);
		setThrottle(0);
		setYaw(0);
		setPitch(0);
		setRoll(0);
		setheight(0);
		setDirection(0);
		setSpeed(0);
	}
	
	public String getCor(String sel) {
		String cor = null;
		IfGood mAm = StatLib.FindMatch(Train,0);
		ArrayList<MatchFeature> Ma = mAm.m1;
		for (MatchFeature matchFeature : Ma) {
			String n1 = te.getName2(matchFeature.f1).id;
			String n2 = te.getName2(matchFeature.f2).id;
			if (n1.equals(sel)) return n2;
		}
		return null;
	}
	
	
	public void learnData() {this.ad.learnNormal(Train);}
	public List<AnomalyReport> DetectData() {return ad.detect(te);}



	public void Connect() {
		try {
			this.fg = new Connact(ClientSettings);
			flagConnect = true;
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("connect");
			a.showAndWait();
		} catch (Exception e) {
			flagConnect =false; 
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("not connected");
			a.showAndWait();}	
	}

	public void disConnect() {
		if(flagConnect==true) {
			try {
				this.fg.closeSo();
				this.fg = null;
				flagConnect = false;
			} catch (Exception e) {flagConnect =true;}
		}
	}

	public void setAnomalyDetector(TimeSeriesAnomalyDetector ad) {
		this.ad=ad;
	}

	public Series algos(String...strings) {
		String i1;
		String i2 = null;
		if (strings[0] != null && strings[1] != null) {
			i1 = te.getId(strings[0]).name;
			i2 = te.getId(strings[1]).name;
		}
		else {
			i1 = te.getId(strings[0]).name;
		}
		
		return this.ad.paint(i1,i2);
	}

	public double getCoraletion(String s3, String s2) {
		float p = StatLib.pearson(StatLib.al(Train.getId(s3).sam),
					StatLib.al(Train.getId(s2).sam));
		return Math.abs(p);
	}
}
