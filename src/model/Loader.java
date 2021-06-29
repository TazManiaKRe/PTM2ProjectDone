package model;

import java.util.List;

import Algos.AnomalyReport;
import javafx.scene.chart.XYChart.Series;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Loader implements TimeSeriesAnomalyDetector{
    private TimeSeriesAnomalyDetector alg;

    public Loader(String p,String classname) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String path =  "file://" + p;
        URL[] urls = new URL[1];
        urls[0] = new URL(path);
        URLClassLoader loader = new URLClassLoader(urls);
        Class<?> classInstance = loader.loadClass(classname);
        alg = (TimeSeriesAnomalyDetector)classInstance.newInstance();
    }

    public TimeSeriesAnomalyDetector getAlg() {return alg;}

    public void setAlg(TimeSeriesAnomalyDetector alg) {this.alg = alg;}

    @Override
    public void learnNormal(TimeSeries ts) { alg.learnNormal(ts);}

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) { return alg.detect(ts);}

    @Override
    public Series paint(String... strings) { return alg.paint(strings);}

    @Override
    public String getName() {return alg.getName();}

}
