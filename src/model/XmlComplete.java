package model;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javax.swing.text.View;

public class XmlComplete {
	private ArrayList<String> fd;
	private XmlSettings client;
	protected ViewName loadName;
	protected XmlSettings clientSet;
	protected String path;
	
	public XmlComplete() {
		loadName = new ViewName();
		clientSet = new XmlSettings();
		path = "resources/ViewNamesSettings.txt";
		fd = new ArrayList<String>();
	}
	
	public void loadNames() { this.fd  =  loadName.Load(path);}
	
	public void WriteXmlToUser() throws IOException {
		this.loadNames();
		clientSet.setHost("local");
		clientSet.setPort(5400);
		clientSet.setTO(10);
		ArrayList<FeatureSettings> fs = new ArrayList<FeatureSettings>();
		for (String s : this.fd) {
			FeatureSettings f = new FeatureSettings();
			f.setReal(s);
			f.setName("enter csv");
			f.setMax(1);
			f.setMin(-1);
			fs.add(f);
		}
		clientSet.setHf(fs);
		XMLWrite.WriteToXML(clientSet);
	}
	
	public XmlSettings LoadSettingsFromClient(String path){
		XmlSettings new_setting = new XmlSettings();
		try {
			new_setting = XMLWrite.deserializeFromXML(path);
			this.SettingCheck(new_setting);
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("all good");
			a.setContentText("load");
			a.show();
			this.client = new_setting;
		} catch (Exception e) {
	
			if (this.client != null) {
				Alert a = new Alert(AlertType.WARNING);
				a.setHeaderText("no good");
				a.setContentText("fail");
				a.showAndWait();
				return this.client;
			}
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("no good");
			a.setContentText("fail");
			a.showAndWait();
			new_setting = null;
		}
		return new_setting;
	}
	public void SettingCheck(XmlSettings xs) throws Exception {
		for (FeatureSettings FeatureSetting : xs.getHf()) {
			if (FeatureSetting.getName().equals("") || FeatureSetting.getName().equals("enter name")  ) {
				throw new Exception("Missing name");
			}
			if (FeatureSetting.getMax() <= FeatureSetting.getMin()) {
				throw new Exception("no good vals");
			}
		}
		if (xs.TO == 0.0) {
			throw new Exception("no good vals");
		}
		
	}
	public void SaveXml(XmlSettings xs) {
		
		try {
			XMLWrite.WriteToXML(xs);
		} catch (IOException e) { System.out.println("cant save");}
	}
}
