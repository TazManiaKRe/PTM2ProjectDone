
package Algos;

import java.util.ArrayList;
import java.util.List;

import Algos.AnomalyReport;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import model.Feature;
import model.StatLib;
import model.TimeSeries;
import model.TimeSeriesAnomalyDetector;

public class ZScore implements TimeSeriesAnomalyDetector {
    private ArrayList<Double> hf = new ArrayList<Double>();
    protected TimeSeries ts;

    @Override
    public void learnNormal(TimeSeries ts) {
        this.ts = ts;
        for (Feature f : ts.getTable()) {
            double max = -1;
            for (int i = 2; i < f.getSam().size(); i++) {
                double avgx = StatLib.avg( StatLib.al( f.getSam().subList(0, i)));
                double temp =  Math.sqrt(StatLib.var(StatLib.al(f.getSam().subList(0, i))));
                for (int j = 0; j < i; j++) {
                    double zScore;
                    if (temp != 0 ) {
                        zScore = Math.abs( f.getSam().get(j) - avgx) / temp;
                    }
                    else {
                        zScore = 0;
                    }
                    if (zScore > max) {
                        max = zScore;
                    }
                }
            }
            hf.add(max);
            max = -1;
        }
    }



    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        ArrayList<AnomalyReport> hf = new ArrayList<AnomalyReport>();
        for (int i = 0; i < ts.getTable().size(); i++) {
            Feature f = ts.getTable().get(i);
            for (int j = 2; j < f.getSam().size(); j++) {
                double avgx = StatLib.avg( StatLib.al( f.getSam().subList(0, j)));
                double temp =  Math.sqrt(StatLib.var(StatLib.al(f.getSam().subList(0, j))));
                if (temp != 0 ) {
                    for (int k = 0; k < j; k++) {
                        double z_score = Math.abs( f.getSam().get(k) - avgx) / temp;
                        if (z_score > this.hf.get(i)) {
                            AnomalyReport ar = new AnomalyReport(f.getId(), k+1);
                            if (!StatLib.isgood(hf, ar)) {hf.add(ar);}
                        }
                    }
                }
            }
        }

        return hf;
    }

    @Override
    public Series paint(String... str) {
        String f1 = str[0];
        int temp = 0;
        for (int i = 0; i < ts.getTable().size(); i++) {
            if (ts.getTable().get(i).name.equals(f1)) {
                i = i;
            }
        }
        Series s = new Series();
        int x = 0;
        int max = ts.getTable().get(0).size;
        double threshold = hf.get(temp);

        s.getData().add(new XYChart.Data(x,threshold));
        s.getData().add(new XYChart.Data(max,threshold));
        return s;
    }


    @Override
    public String getName() { return "Zscore";}

}
