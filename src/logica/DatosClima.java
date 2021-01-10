package logica;

import java.util.ArrayList;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class DatosClima {

    private final String ruta_archivo;    //ruta del archivo CSV con datos
    private final ArrayList<String> temp; //temperatura
    private final ArrayList<String> hum;  //humedad
    private final ArrayList<String> viento; // velocidad de viento
    private float[] tempN, humN, vientoN;    //Vectores tipo float para manipulación de datos
    private int[] num_horas;                 //horas (timestamps) de los datos a leer

    
    public DatosClima(String path) {
        ruta_archivo = path;
        temp = new ArrayList<>();
        hum = new ArrayList<>();
        viento = new ArrayList<>();
    }

    // Lectura de archivo CSV mediante la librería org.apache.commons.csv
    public void LeerArchivo() throws IOException {

         
        try (Reader reader = Files.newBufferedReader(Paths.get(ruta_archivo))) {
                   
           //
          Iterable<CSVRecord> datos = CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord().parse(reader);
          //Captura de datos contenidos en el archivo CSV
            datos.forEach(dato -> {
                temp.add(dato.get(0));
                hum.add(dato.get(1));
                viento.add(dato.get(2));
                }
            );
            reader.close();             //Fin de lectura del archivo CSV
        }
        
        //
        tempN = new float[temp.size()];
        humN = new float[hum.size()];
        vientoN = new float[viento.size()];
        num_horas = new int[temp.size()];

        for (int i = 0; i < temp.size(); i++) {
            tempN[i] = Float.parseFloat(temp.get(i));
            humN[i] = Float.parseFloat(hum.get(i));
            vientoN[i] = Float.parseFloat(viento.get(i));
            num_horas[i] = i + 1; //Timestamps o registros de tiempo de temperatura
        }

        temp.clear();
        hum.clear();
        viento.clear();
    }

    
    //Getters y Setters
    public float[] getTempN() {
        return tempN;
    }

    public void setTempN(float[] tempN) {
        this.tempN = tempN;
    }

    public float[] getHumN() {
        return humN;
    }

    public void setHumN(float[] humN) {
        this.humN = humN;
    }

    public float[] getVientoN() {
        return vientoN;
    }

    public void setVientoN(float[] vientoN) {
        this.vientoN = vientoN;
    }

    public int[] getNum_horas() {
        return num_horas;
    }

    public void setNum_horas(int[] num_horas) {
        this.num_horas = num_horas;
    }

}
