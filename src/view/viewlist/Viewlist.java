package view.viewlist;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class Viewlist extends AnchorPane
{
	public final ViewlistController coll;
	public ListView<String> hf;
	
	public Viewlist()
	{
		FXMLLoader fxl = new FXMLLoader();
		AnchorPane ap = null;
		try {
			ap = fxl.load(getClass().getResource("Viewlist.fxml").openStream());
		} catch (IOException e) { e.printStackTrace();}
		
		if(ap!=null) {
			coll =fxl.getController();
			hf = coll.listView;
			this.getChildren().add(ap);
		}
		else
			coll =null;
	}

}
