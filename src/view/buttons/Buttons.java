package view.buttons;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

public class Buttons extends AnchorPane
{
	public final ButtonsController coll;
	public Slider time;
	public Label video;
	public ChoiceBox<Number> speed;
	
	public Buttons() {
		AnchorPane ap = null;
		FXMLLoader fxl = new FXMLLoader();
		try {
			ap = fxl.load(getClass().getResource("Buttons.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}
		
		if(ap!=null){
			coll =fxl.getController();
			time = coll.videoSlider;
			speed = coll.videoSpeed;
			speed.setItems(FXCollections.observableArrayList(0.25,0.5,0.75,1.0,1.25,1.5,1.75,2.0));
			speed.setValue(1.0);
			video = coll.VideoTime;
			this.getChildren().add(ap);
		}
		else 
			coll =null;
	}
}
