
package model;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;



public class Connact{

    protected XmlSettings xml;
    protected Socket cli;
    protected PrintWriter ot;

    public Connact(XmlSettings set) throws UnknownHostException, IOException {
        this.xml = set;
        cli = new Socket(xml.host, xml.port);
        ot = new PrintWriter(cli.getOutputStream());
    }
    public void send(ArrayList<Float> data) {
        ot.println(fos(data));
        ot.flush();
    }
    public String fos(ArrayList<Float>line) {
        String out = "";
        for (Float float1 : line) {
            out += float1 + ",";
        }
        String re = out.substring(0, out.length()-1);
        return re;
    }



    public void closeSo() {
        try {
            cli.close();
            ot.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
