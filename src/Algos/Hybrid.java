package Algos;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import model.*;

public class Hybrid implements TimeSeriesAnomalyDetector
{
    protected HashMap<String, Circle> hybrid = new HashMap<>();
    protected HashMap<String, Linear> lin = new HashMap<>();
    protected HashMap<String, ZScore> zScore=new HashMap<>();
    private Random rand = new Random();
    protected TimeSeries ts;


    @Override
    public void learnNormal(TimeSeries ts) {
        this.ts = ts;
        ArrayList<Point> point = new ArrayList<>();

        ArrayList<MatchFeature> hf = StatLib.FindMatch(ts,0).m1;

        for (MatchFeature maf : hf) {
            Feature f1 = ts.getName2(maf.f1);
            Feature f2 = ts.getName2(maf.f2);
            String name=f1.getName()+"-"+f2.getName();

            if(Math.abs(maf.corr)>=0.95)
            {
                TimeSeries t = new TimeSeries(f1,f2);
                Linear linear = new Linear();
                linear.learnNormal(t);
                lin.put(name,linear);
            }
            else if(Math.abs(maf.corr)<0.5) {
                TimeSeries t = new TimeSeries(f1,f2);
                ZScore z = new ZScore();
                z.learnNormal(t);
                zScore.put(name, z);
            }
            else {
                TimeSeries t = new TimeSeries(f1,f2);
                for(int i=0;i<f1.size;i++) {
                    Point p=new Point(f1.sam.get(i),f2.sam.get(i));
                    point.add(p);
                }
                hybrid.put(name,findMinimumCircle(point));
                point=new ArrayList<>();
            }
        }
        for (Feature f : StatLib.FindMatch(ts,0).m2) {
            TimeSeries t = new TimeSeries(f);
            ZScore z = new ZScore();
            z.learnNormal(t);
            zScore.put(f.name, z);
        }
    }

    @Override
    public Series paint(String... strings) {

        Series s= new Series();
        String key = strings[0] + "-" + strings[1];

        if (hybrid.containsKey(key)) {
            float xcen = hybrid.get(key).cen.x;
            float ycen = hybrid.get(key).cen.y;
            float radius = hybrid.get(key).rad;
            ArrayList<Point> points=new ArrayList<Point>();
            for(double angle=0;angle<360;angle+=0.5)
            {
                float x=(float) (radius*Math.cos(angle)+xcen);
                float y=(float) (radius*Math.sin(angle)+ycen);
                points.add(new Point(x, y));
            }
            for (Point point : points)
                s.getData().add(new XYChart.Data(point.x, point.y));
            return s;
        }
        else if (lin.containsKey(key)) {
            s = lin.get(key).paint(strings);
            return s;
        }
        else if (zScore.containsKey(key)) {
            s = zScore.get(key).paint(strings);
            return s;
        }
        return null;
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        ArrayList<AnomalyReport> resultList = new ArrayList<AnomalyReport>();

        for (int i = 0; i < ts.table.size(); i++) {
            for (int j = 0; j < ts.table.size(); j++) {
                Feature f1 = ts.getTable().get(i);
                Feature f2 = ts.getTable().get(j);
                TimeSeries t = new TimeSeries(f1,f2);
                String name= f1.getName()+"-"+f2.getName();

                if(hybrid.containsKey(name)) {
                    ArrayList<Point> points = new ArrayList<>();
                    for(int k=0;k<f1.size;k++) {
                        Point p=new Point(f1.sam.get(k),f2.sam.get(k));
                        points.add(p);
                    }
                    int index=1;
                    for (Point point : points) {
                        if(!this.hybrid.get(name).isContainsPoint(point)) {
                            String discription = f1.id + "-" + f2.id;
                            String discription2 = f2.id + "-" + f1.id;
                            AnomalyReport report = new AnomalyReport(discription,index);
                            AnomalyReport report2 = new AnomalyReport(discription2,index);
                            if(!StatLib.isgood(resultList,report) && !StatLib.isgood(resultList,report2))
                                resultList.add(report);
                        }
                        index++;
                    }
                }
                else if(lin.containsKey(name))
                {

                    List<AnomalyReport> reports =this.lin.get(name).detect(t);
                    for (AnomalyReport anomalyReport : reports) {
                        AnomalyReport anomalyReport2 = new AnomalyReport(StatLib.ReverseString(anomalyReport.des), anomalyReport.stenp);
                        if(!StatLib.isgood(resultList,anomalyReport) && !StatLib.isgood(resultList,anomalyReport2))
                            resultList.add(anomalyReport);
                    }
                }
                else if(this.zScore.containsKey(name)){
                    List<AnomalyReport> reports =this.zScore.get(name).detect(t);
                    for (AnomalyReport anomalyReport : reports) {
                        AnomalyReport anomalyReport2 = new AnomalyReport(StatLib.ReverseString(anomalyReport.des), anomalyReport.stenp);
                        if(!StatLib.isgood(resultList,anomalyReport) && !StatLib.isgood(resultList,anomalyReport2))
                            resultList.add(anomalyReport);
                    }
                }
            }
        }
        for (Feature f : StatLib.FindMatch(ts,0).m2) {
            if (zScore.containsKey(f.name)) {
                TimeSeries t = new TimeSeries(f);
                List<AnomalyReport> reports =this.zScore.get(f.name).detect(t);
                for (AnomalyReport anomalyReport : resultList) {
                    if(!StatLib.isgood(resultList,anomalyReport))
                        resultList.add(anomalyReport);
                }
            }
        }
        resultList.sort((x,y)->{
            return (int) (x.stenp -y.stenp);
        });
        return resultList;
    }


    public Circle findMinimumCircle(final List<Point> points) {
        return WelezAlgo(points, new ArrayList<Point>());
    }

    private Circle WelezAlgo(final List<Point> points, final List<Point> R) {
        Circle minimumCircle = null;

        if (R.size() == 3) {
            minimumCircle = new Circle(R.get(0), R.get(1), R.get(2));
        }
        else if (points.isEmpty() && R.size() == 2) {
            minimumCircle = new Circle(R.get(0), R.get(1));
        }
        else if (points.size() == 1 && R.isEmpty()) {
            minimumCircle = new Circle(points.get(0).x, points.get(0).y, 0);
        }
        else if (points.size() == 1 && R.size() == 1) {
            minimumCircle = new Circle(points.get(0), R.get(0));
        }
        else {
            Point p = points.remove(rand.nextInt(points.size()));
            minimumCircle = WelezAlgo(points, R);

            if (minimumCircle != null && !minimumCircle.isContainsPoint(p)) {
                R.add(p);
                minimumCircle = WelezAlgo(points, R);
                R.remove(p);
                points.add(p);
            }
        }

        return minimumCircle;
    }
    @Override
    public String getName() { return "Hybrid";}


}
