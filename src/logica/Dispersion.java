package logica;

import java.awt.Color;
import java.awt.Shape;
import java.util.Vector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import org.jfree.util.ShapeUtilities;

public class Dispersion {

    Vector datos;
    JFreeChart chart;

    public Dispersion(Vector datos) {
        this.datos = datos;
    }

    //Creación de gráfica a mostrar
    public ChartPanel crearGrafica(String tipo1, String tipo2, SimpleKMeans skm) {

        XYDataset dataset = crearDataset(skm);
        chart = ChartFactory.createScatterPlot(tipo1 + " vs " + tipo2, tipo1, tipo2, dataset);
        ChartPanel cp = new ChartPanel(chart);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        configurarRendered(renderer);

        return cp;
    }

    //Configuración de formas y colores a mostrar en la gráfica
    private void configurarRendered(XYLineAndShapeRenderer renderer) {
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesShapesVisible(3, true);

        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(1, Color.GRAY);
        renderer.setSeriesPaint(2, Color.BLUE);
        renderer.setSeriesPaint(3, Color.BLACK);

        Shape cross = ShapeUtilities.createDiagonalCross(3, 2);
        Shape cross1 = ShapeUtilities.createDiamond(4);
        renderer.setSeriesShape(3, cross);
        renderer.setSeriesShape(2, cross1);
    }

    //Creación del dataset
    public XYDataset crearDataset(SimpleKMeans skm) {

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries c1 = new XYSeries("Cluster #1");
        XYSeries c2 = new XYSeries("Cluster #2");
        XYSeries c3 = new XYSeries("Cluster #3");
        XYSeries c4 = new XYSeries("Centroides");

        //Gráficas de Cluster
        Instances instances = skm.getClusterCentroids();
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance inst = instances.instance(i);
            c4.add(inst.value(0), inst.value(1));
        }

        Punto aux;
        for (int i = 0; i < datos.size(); i++) {
            aux = (Punto) datos.get(i);
            switch (aux.getCluster()) {
                case 0:
                    c1.add(aux.getX(), aux.getY());
                    break;
                case 1:
                    c2.add(aux.getX(), aux.getY());
                    break;
                default:
                    c3.add(aux.getX(), aux.getY());
                    break;
            }
        }
        dataset.addSeries(c1);
        dataset.addSeries(c2);
        dataset.addSeries(c3);
        dataset.addSeries(c4);

        return dataset;
    }

}
