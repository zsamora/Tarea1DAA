import java.io.*;
import java.util.ArrayList;

public class FirstAlgorithm {
    int M;
    int B;
    String X;
    String Y;
    String IntegerList;
    ArrayList<String> arrayX;
    ArrayList<String> arrayY;
    ArrayList<Integer> arrayInts;
    ArrayList<Integer> arrayIntsTemp;
    String IntegerListTemp = "IntegersTemp.bin";

    public FirstAlgorithm(int M, int B) {
        this.M = M;
        this.B = B;
    }

    public void calculateDistance(String X, String Y, String IntegerList, int N) throws IOException, ClassNotFoundException {
        this.X = X;
        this.Y = Y;
        this.IntegerList = IntegerList;
        int row = 0;
        int col = 0;
        int upLeft = 0;
        int downLeft = 1;
        while (row < N) {
            // Leemos un bloque del segundo string si pasamos B filas (caracteres)
            if (row % this.B == 0)
                readRows(row);
            System.out.println(arrayY);
            // Abrimos los Stream para lectura de archivos del primer string y la fila de enteros (previa y temporal)
            FileInputStream fisX = new FileInputStream(X);
            ObjectInputStream oisX = new ObjectInputStream(fisX);
            FileInputStream fisInt = new FileInputStream(IntegerList);
            ObjectInputStream oisInt = new ObjectInputStream(fisInt);
            // Array para guardar todos los enteros calculados en la fila
            this.arrayIntsTemp = new ArrayList<Integer>();
            // Leemos el segundo string de un bloque por iteración (N/B veces)
            while (col < N / this.B) {
                // Reiniciamos el array del primer string y los valores calculados en el array de enteros
                this.arrayX = new ArrayList<String>();
                this.arrayInts = new ArrayList<Integer>();
                // Llenamos la memoria solo con un bloque de String, que tiene 4 bloques de Integer asociado
                this.arrayX.add((String) oisX.readObject());
                for (int j = 0; j < 4 ; j++)
                    this.arrayInts.addAll((ArrayList<Integer>) oisInt.readObject());
                calculateRow(upLeft, downLeft, row);
                writeRow();
            }
            row += 1;
            upLeft += 1;
            downLeft += 1;
            oisX.close();
            fisX.close();
            oisInt.close();
            fisInt.close();
        }
    }
    public void readRows(int row) throws IOException, ClassNotFoundException {
        this.arrayY = new ArrayList<String>();
        FileInputStream fisY = new FileInputStream(Y);
        ObjectInputStream oisY = new ObjectInputStream(fisY);
        this.arrayY.add((String) oisY.readObject());
    }

    public void calculateRow(int upLeft, int downLeft, int row) throws IOException {
        int newDownLeft;
        for (int i = 0; i < this.arrayInts.size(); i ++) {
            if (arrayX.get(i % 4) == arrayY.get(row)) {
                newDownLeft = Math.min(upLeft, Math.min(downLeft + 1, arrayInts.get(i) + 1));
                this.arrayIntsTemp.add(newDownLeft);
                downLeft = newDownLeft;
            }
            else {
                newDownLeft = Math.min(upLeft + 1, Math.min(downLeft + 1, arrayInts.get(i) + 1));
                this.arrayIntsTemp.add(newDownLeft);
                downLeft = newDownLeft;
            }
        }
        return newDownleft;
    }

    public void writeRow() throws IOException {
        // Array List para escribir los enteros
        ArrayList<Integer> intTempBuilder = new ArrayList<Integer>();
        // Output Stream para escribir enteros temporales
        FileOutputStream fosI = new FileOutputStream(IntegerListTemp);
        ObjectOutputStream oosI = new ObjectOutputStream(fosI);
        for (int i = 0; i < arrayIntsTemp.size(); i++) {
            if ((i + 1) % (B / 4) == 0) {
                System.out.println(intTempBuilder);
                oosI.writeObject(intTempBuilder);
                intTempBuilder = new ArrayList<Integer>();
            }
        }
        fosI.close();
        oosI.close();
    }
}
