package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ViewName {

    public ArrayList<String> Load(String pa){
        Scanner s;
        try {
            s = new Scanner(new BufferedReader(new FileReader(pa)));
            ArrayList<String> n=new ArrayList<String>();
            while (s.hasNext()) {
                String string = (String) s.next();
                n.add(string);
            }
            s.close();
            return n;
        } catch (FileNotFoundException e) {}

        return null;

    }
}
