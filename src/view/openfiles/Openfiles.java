package view.openfiles;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Openfiles extends AnchorPane
{
	public final OpenfilesController coll;
	
	public Openfiles()
	{
		FXMLLoader fxmlLoader = new FXMLLoader();
		AnchorPane anchorPane = null;
		
		try {
			anchorPane = fxmlLoader.load(getClass().getResource("Openfiles.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}
		if(anchorPane!=null) {
			coll =fxmlLoader.getController();
			this.getChildren().add(anchorPane);
		}
		else
			coll =null;
	}

}
