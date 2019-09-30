import java.io.*;
import java.util.ArrayList;

public class FirstAlgorithm {
    int M;
    int B;
    int upLeft;
    int downLeft;
    int upLeftTemp;
    int downLeftTemp;
    String X;
    String Y;
    String IntegerList;
    String subX;
    String subY;
    ArrayList<Integer> arrayInts;
    ArrayList<Integer> arrayIntsTemp;

    public FirstAlgorithm(int M, int B) {
        this.M = M;
        this.B = B;
        upLeft = 0;
        downLeft = 1;
    }

    public int calculateDistance(String X, String Y, String IntegerList, int N) throws IOException, ClassNotFoundException {
        this.X = X;
        this.Y = Y;
        this.IntegerList = IntegerList;
        int row = 0;
        int col = 0;
        int result = -1;
        FileInputStream fisY = new FileInputStream(Y);
        ObjectInputStream oisY = new ObjectInputStream(fisY);
        while (row < N) {
            // Leemos un bloque del segundo string (vertical) si pasamos B filas (caracteres)
            if (row % this.B == 0)
                readRows(oisY);
            // Abrimos los Stream para lectura de archivos del primer string y la fila de enteros (previa y temporal)
            FileInputStream fisX = new FileInputStream(X);
            ObjectInputStream oisX = new ObjectInputStream(fisX);
            FileInputStream fisInt = new FileInputStream(IntegerList);
            ObjectInputStream oisInt = new ObjectInputStream(fisInt);
            // Array para guardar todos los enteros calculados en la fila
            this.arrayIntsTemp = new ArrayList<Integer>();
            // Seteamos los valores upLeftTemp y downLeftTemp como los de la primera columna
            this.upLeftTemp = this.upLeft;
            this.downLeftTemp = this.downLeft;
            // Leemos el primer string (horizontal) de un bloque por iteración (N/B veces)
            while (col < N / this.B) {
                // Reiniciamos el array del primer string y los valores calculados en el array de enteros
                this.arrayInts = new ArrayList<Integer>();
                // Llenamos la memoria solo con un bloque de String, que tiene 4 bloques de Integer asociado
                this.subX = (String) oisX.readObject();
                Main.DISK_ACCESSES++;
                for (int j = 0; j < 4 ; j++)
                    this.arrayInts.addAll((ArrayList<Integer>) oisInt.readObject());
                    Main.DISK_ACCESSES++;
                //System.out.println("---- Int ----");
                //System.out.println(arrayInts);
                //System.out.println("---- subX ----");
                //System.out.println(subX);
                // Calculamos la fila
                calculateRow(row % this.B);
                //System.out.println("---- ResInt ----");
                //System.out.println(arrayIntsTemp);
                col += 1;
            }
            oisInt.close();
            fisInt.close();
            oisX.close();
            fisX.close();
            // Escribimos todos los enteros
            result = writeRow();
            row += 1;
            this.upLeft += 1;
            this.downLeft += 1;
            col = 0;
        }
        return result;
    }
    public void readRows(ObjectInputStream oisY) throws IOException, ClassNotFoundException {
        this.subY = (String) oisY.readObject();
        Main.DISK_ACCESSES++;
    }

    public void calculateRow(int row) throws IOException {
        int newDownLeft;
        int up;
        //System.out.println("---- charY ----");
        //System.out.println(subY.charAt(row));
        for (int i = 0; i < this.arrayInts.size(); i ++) {
            up = arrayInts.get(i);
            if (subX.charAt(i) == subY.charAt(row))
                newDownLeft = Math.min(this.upLeftTemp, Math.min(this.downLeftTemp + 1, up + 1));
            else
                newDownLeft = Math.min(this.upLeftTemp + 1, Math.min(this.downLeftTemp + 1, up + 1));
            this.arrayIntsTemp.add(newDownLeft);
            this.downLeftTemp = newDownLeft;
            this.upLeftTemp = up;
        }
    }

    public int writeRow() throws IOException {
        // Array List para escribir los enteros
        ArrayList<Integer> intTempBuilder = new ArrayList<Integer>();
        // Output Stream para escribir enteros temporales
        FileOutputStream fosI = new FileOutputStream(IntegerList);
        ObjectOutputStream oosI = new ObjectOutputStream(fosI);
        for (int i = 0; i < arrayIntsTemp.size(); i++) {
            intTempBuilder.add(arrayIntsTemp.get(i));
            if ((i + 1) % (B / 4) == 0) {
                oosI.writeObject(intTempBuilder);
                Main.DISK_ACCESSES++;
                intTempBuilder = new ArrayList<Integer>();
            }
        }
        fosI.close();
        oosI.close();
        return arrayIntsTemp.get(arrayIntsTemp.size()-1);
    }
}
