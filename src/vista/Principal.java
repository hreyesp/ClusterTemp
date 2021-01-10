package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import logica.DatosClima;
import logica.ProcesoWeka;
import logica.Punto;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.jfree.chart.ChartPanel;
import logica.Dispersion;
import logica.XYChart;
import logica.XYChartFactory;
import weka.clusterers.SimpleKMeans;

public class Principal extends JFrame {

    private XYChart lineChart;
    private Expression regresionTemp;
    private JFrame vEcuacion;
    private JFrame vCentroide;

    //Evaluar Regresión Temperatura
    private float[] evaluateRegretion(Expression evaluator, int[] toPredict) {
        float predicted[] = new float[toPredict.length];
        for (int i = 0; i < toPredict.length; i++) {
            evaluator.setVariable("X", toPredict[i]);
            predicted[i] = (float) evaluator.evaluate();
        }
        return predicted;
    }

    //Evaluar Proyeccion Temperatura
    private void evaluate() {
        try {
            Integer value = Integer.parseInt(testing.getText());
            if (value > 0) {
                Expression evaluator;
                evaluator = regresionTemp;
                evaluator.setVariable("X", value);
                float predicted = (float) evaluator.evaluate();
                prediction.setText("La temperatura proyectada para la hora " + value + " es de " + String.format("%.2f", predicted) + " grados (°C)");
            }
        } catch (NumberFormatException nfe) {
            prediction.setText("");
        }
    }

    public Principal() {
        try {
            initComponents();
            this.setLocationRelativeTo(null); // Mostrar ventana en el centro

            // Evento para mostrar predicción de temperatura
            testing.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char input = e.getKeyChar();
                    if ((input < '0' || input > '9') && input != '\b') {
                        e.consume();
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            evaluate();
                        });
                    }
                }
            });

            // Gráfico de Temperatura y Regresión
            this.lineChart = XYChartFactory.createXYLineChart("Temperatura - Pasto, Nariño (29/12/2020 - 05/01/2021)", "Horas", "Grados (°C)");
            ChartPanel chartPanel = this.lineChart.getPanel();
            chartPanel.setPreferredSize(this.panelTemp.getPreferredSize());
            this.panelTemp.removeAll();
            this.panelTemp.setLayout(new BorderLayout());
            this.panelTemp.add(chartPanel, BorderLayout.CENTER);
            this.panelTemp.validate();

            //Lectura de archivo csv que contiene datos del clima de la ciudad asignada
            DatosClima csvReader = new DatosClima("datos_clima.csv");
            csvReader.LeerArchivo();
            float[] temp = csvReader.getTempN();
            float[] hum = csvReader.getHumN();
            float[] vel = csvReader.getVientoN();

            this.lineChart.prepareSerie("Temperatura(Real)", Color.BLUE, csvReader.getNum_horas(), temp);
            ProcesoWeka dvTemp = new ProcesoWeka(csvReader.getNum_horas(), temp);
            String regTemp = dvTemp.regresionLineal();
            this.regresionTemp = new ExpressionBuilder(regTemp)
                    .variables("X")
                    .build();
            float[] regEvalTemp = evaluateRegretion(regresionTemp, csvReader.getNum_horas());
            this.lineChart.prepareSerie("Temperatura(Regresión)", Color.BLACK, csvReader.getNum_horas(), regEvalTemp);

            // Creación de Cluster - Temperatura vs Humedad
            Vector datos = new Vector();
            Punto p;
            for (int i = 0; i < temp.length; i++) {
                p = new Punto(hum[i], temp[i]);
                datos.add(p);
            }
                ProcesoWeka analisis = new ProcesoWeka(datos);
                SimpleKMeans skm = analisis.clustering("Humedad");

                
                Dispersion disp = new Dispersion(datos);
                ChartPanel chart = disp.crearGrafica("Humedad (%)", "Temperatura (°C)", skm);
                this.cent_graph.removeAll();
                this.cent_graph.setLayout(new BorderLayout());
                this.cent_graph.add(chart, BorderLayout.CENTER);
                this.cent_graph.validate();

            // Creación de Cluster - Temperatura vs Velocidad del Viento
            Vector datos1 = new Vector();
            Punto p1 = null;
            for (int i = 0; i < temp.length; i++) {
                p1 = new Punto(vel[i], temp[i]);
                datos1.add(p1);
            }
                ProcesoWeka analisis1 = new ProcesoWeka(datos1);
                SimpleKMeans skm1 = analisis1.clustering("Velocidad del Viento");

                
                Dispersion disp1 = new Dispersion(datos1);
                ChartPanel chart1 = disp1.crearGrafica("Velocidad del Viento (Km/h)", "Temperatura (°C)", skm1);
                this.cent2.removeAll();
                this.cent2.setLayout(new BorderLayout());
                this.cent2.add(chart1, BorderLayout.CENTER);
                this.cent2.validate();

            // Ventana para mostrar información de centroides
            vCentroide = new VentanaCentroides(skm, skm1);

            // Ventana para mostrar información de ecuación de regresión    
            vEcuacion = new VentanaEcuaciones(regTemp);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabGraficas = new javax.swing.JTabbedPane();
        panelTemp = new javax.swing.JPanel();
        cenpred = new javax.swing.JPanel();
        cent_graph = new javax.swing.JPanel();
        cent2 = new javax.swing.JPanel();
        info_panel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        prediction = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        testing = new javax.swing.JTextField();
        verRegEC = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ClusterTemp");
        setBackground(new java.awt.Color(255, 255, 255));

        tabGraficas.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        panelTemp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelTempLayout = new javax.swing.GroupLayout(panelTemp);
        panelTemp.setLayout(panelTempLayout);
        panelTempLayout.setHorizontalGroup(
            panelTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelTempLayout.setVerticalGroup(
            panelTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        tabGraficas.addTab("Gráfica Temperatura", panelTemp);

        cent_graph.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout cent_graphLayout = new javax.swing.GroupLayout(cent_graph);
        cent_graph.setLayout(cent_graphLayout);
        cent_graphLayout.setHorizontalGroup(
            cent_graphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 898, Short.MAX_VALUE)
        );
        cent_graphLayout.setVerticalGroup(
            cent_graphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 336, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout cenpredLayout = new javax.swing.GroupLayout(cenpred);
        cenpred.setLayout(cenpredLayout);
        cenpredLayout.setHorizontalGroup(
            cenpredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cenpredLayout.createSequentialGroup()
                .addComponent(cent_graph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        cenpredLayout.setVerticalGroup(
            cenpredLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cent_graph, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabGraficas.addTab("Humedad vs Temperatura", cenpred);

        cent2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout cent2Layout = new javax.swing.GroupLayout(cent2);
        cent2.setLayout(cent2Layout);
        cent2Layout.setHorizontalGroup(
            cent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        cent2Layout.setVerticalGroup(
            cent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        tabGraficas.addTab("Velocidad del Viento vs Temperatura", cent2);

        info_panel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), null));
        info_panel.setMaximumSize(new java.awt.Dimension(32767, 100));
        info_panel.setMinimumSize(new java.awt.Dimension(0, 100));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("Hora");

        prediction.setEditable(false);
        prediction.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        prediction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                predictionActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setText("Proyección");

        testing.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        testing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testingActionPerformed(evt);
            }
        });

        verRegEC.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        verRegEC.setText("Ver Ecuaciones Regresión");
        verRegEC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verRegECActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton1.setText("Ver Información Centroides");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout info_panelLayout = new javax.swing.GroupLayout(info_panel);
        info_panel.setLayout(info_panelLayout);
        info_panelLayout.setHorizontalGroup(
            info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(info_panelLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(info_panelLayout.createSequentialGroup()
                        .addComponent(verRegEC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(info_panelLayout.createSequentialGroup()
                        .addComponent(testing, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(21, 21, 21)))
                .addGroup(info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prediction, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(info_panelLayout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        info_panelLayout.setVerticalGroup(
            info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(info_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(testing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(prediction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(verRegEC)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(info_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabGraficas, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabGraficas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(info_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void verRegECActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verRegECActionPerformed
        // TODO add your handling code here:
        vEcuacion.setVisible(true);
    }//GEN-LAST:event_verRegECActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        vCentroide.setVisible(true);
        vCentroide.setLocationRelativeTo(null);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void testingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_testingActionPerformed

    private void predictionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_predictionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_predictionActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
                }
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cenpred;
    private javax.swing.JPanel cent2;
    private javax.swing.JPanel cent_graph;
    private javax.swing.JPanel info_panel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel panelTemp;
    private javax.swing.JTextField prediction;
    private javax.swing.JTabbedPane tabGraficas;
    private javax.swing.JTextField testing;
    private javax.swing.JButton verRegEC;
    // End of variables declaration//GEN-END:variables
}
