package view.joystick;

import java.io.IOException;

import eu.hansolo.medusa.Gauge;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class Joystick extends AnchorPane
{
	public final JoystickController coll;
	public Gauge dir,speed,yaw,roll,pitch,altitude;
	public Slider rudder,throttle;
	public Circle canvas, moving;
	public DoubleProperty aileron,elevator;
	
	public Joystick()
	{
		FXMLLoader fxl = new FXMLLoader();
		AnchorPane ap = null;
		try {
			ap = fxl.load(getClass().getResource("Joystick.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}

		if(ap!=null) {
			coll =fxl.getController();
			dir = coll.DirectionValue;
			altitude= coll.AltitudeValue;
			speed= coll.speedValue;
			yaw= coll.yawValue;
			pitch= coll.PitchValue;
			roll= coll.RollValue;
			rudder= coll.rudder;
			throttle= coll.throttle;
			moving = coll.movingCircle;
			canvas = coll.CanvasCircle;
			aileron = new SimpleDoubleProperty();
			elevator = new SimpleDoubleProperty();
			this.getChildren().add(ap);
		}
		else
			coll =null;
	}
	public void SetMaxMinForSliders(double...ds)
	{
		coll.rudder.setMax(ds[0]);
		coll.rudder.setMin(ds[1]);
		coll.throttle.setMax(ds[2]);
		coll.throttle.setMin(ds[3]);
	}
	
	public void setMaxMinForClock(double...ds) {
		coll.DirectionValue.setMaxValue(ds[0]);
		coll.DirectionValue.setMinValue(ds[1]);
		
		coll.AltitudeValue.setMaxValue(ds[2]);
		coll.AltitudeValue.setMinValue(ds[3]);
		
		coll.PitchValue.setMaxValue(ds[4]);
		coll.PitchValue.setMinValue(ds[5]);
		
		coll.RollValue.setMaxValue(ds[6]);
		coll.RollValue.setMinValue(ds[7]);
		
		coll.speedValue.setMaxValue(ds[8]);
		coll.speedValue.setMinValue(ds[9]);
		
		coll.yawValue.setMaxValue(ds[10]);
		coll.yawValue.setMinValue(ds[11]);
	}
	
	public double NormlaizeJoystic(double x ,double max,double min,double a,double b) {
		double res=a+((x-min)*(b-a))/(max-min);
		return res;
	}

}
