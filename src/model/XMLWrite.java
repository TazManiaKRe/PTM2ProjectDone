
package model;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLWrite {


    public static void WriteToXML (XmlSettings settings) throws IOException
    {
        FileOutputStream fos = new FileOutputStream("resources/settings.xml");
        XMLEncoder encoder = new XMLEncoder(fos);
        encoder.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception e) {
                System.out.println(e.toString());
            }
        });
        encoder.writeObject(settings);
        encoder.close();
        fos.close();
    }


    public static XmlSettings deserializeFromXML(String path) throws Exception{
        FileInputStream file = new FileInputStream(path);
        XMLDecoder decoder = new XMLDecoder(file);
        XmlSettings decodedSettings = (XmlSettings) decoder.readObject();
        decoder.close();
        file.close();
        return decodedSettings;
    }
}
