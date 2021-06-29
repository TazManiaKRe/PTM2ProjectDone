package Algos;

import Algos.AnomalyReport;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import model.*;

import java.util.ArrayList;
import java.util.List;



public class Linear implements TimeSeriesAnomalyDetector {

    protected ArrayList<CorrelatedFeatures> cofeatures_ls = new ArrayList<CorrelatedFeatures>();
    protected TimeSeries ts;


    public List<CorrelatedFeatures> getno(){return this.cofeatures_ls;}


    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        ArrayList<AnomalyReport> arl = new ArrayList<AnomalyReport>();
        for (CorrelatedFeatures correlatedFeature : this.cofeatures_ls) {
            ArrayList<Float> t1 = ts.getName(correlatedFeature.f1);
            ArrayList<Float> t2 = ts.getName(correlatedFeature.f2);
            float[] f1 = StatLib.al(t1);
            float[] f2 = StatLib.al(t2);
            Point[] points =StatLib.points_gen(f1, f2);
            int ind = 1;
            for (Point p : points) {
                float dev_temp = StatLib.dev(p, correlatedFeature.reg);
                if ( dev_temp > correlatedFeature.tree) {
                    String d = ts.getName2(correlatedFeature.f1).id + "-" + ts.getName2(correlatedFeature.f2).id;
                    AnomalyReport ar = new AnomalyReport(d, ind);
                    arl.add(ar);
                }
                ind++;
            }
        }
        return arl;
    }

    public void learnNormal(TimeSeries ts) {
        this.ts=ts;
        IfGood mam = StatLib.FindMatch(ts, 0.9);
        for (MatchFeature maf : mam.m1) {
            Feature f1 = ts.getName2(maf.f1);
            Feature f2 = ts.getName2(maf.f2);
            float[] mainf1 = StatLib.al(f1.sam);
            float[] nomainf2 = StatLib.al(f2.sam);
            Point[] points = StatLib.points_gen(mainf1, nomainf2);
            Line lrg = StatLib.linear_reg(points);
            float threshold = 0;
            for (Point point : points) {
                float dev = StatLib.dev(point, lrg);
                if (threshold < dev) {
                    threshold = dev;
                }
            }
            CorrelatedFeatures cof = new CorrelatedFeatures(f1.getName(), f2.getName(), maf.corr, lrg,(float) (threshold+0.025));
            this.cofeatures_ls.add(cof);
        }
    }


    @Override
    public Series paint(String... strings) {
        String a=strings[0];
        String b=strings[1];
        Line line = null;
        for (CorrelatedFeatures CF : cofeatures_ls) {
            if((a.equals(CF.f1) && b.equals(CF.f2)))
                line = CF.reg;

        }
        if (line == null) {
            return null;
        }
        Series s = new Series();
        String F1 = ts.getName2(a).id;
        String F2 = ts.getName2(b).id;
        float xMax = ts.getMax(F1);
        float xMin = ts.getMin(F2);
        float yMax = line.f(xMax);
        float yMin = line.f(xMin);

        s.getData().add(new XYChart.Data(xMin, yMin));
        s.getData().add(new XYChart.Data(xMax,yMax));
        return s;
    }

    @Override
    public String getName() { return "Linear";}

}
