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
    ArrayList<ArrayList<Integer>> arrayInts;
    int upLeft = 0;
    int downLeft = 1;

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
        /*System.out.println(arrayX);
        System.out.println(arrayInts);*/
        while (row < N) {
            // Reiniciamos los arrays en memoria para el primer string y los valores calculados
            this.arrayX = new ArrayList<String>();
            if (row % this.B == 0)
                readRows(row);
            // Leemos el string de un bloque cada uno
            while (col < N / this.B) {
                this.arrayInts = new ArrayList<ArrayList<Integer>>();
                // Abrimos los Stream para lectura de archivos
                FileInputStream fisX = new FileInputStream(X);
                ObjectInputStream oisX = new ObjectInputStream(fisX);
                FileInputStream fisInt = new FileInputStream(IntegerList);
                ObjectInputStream oisInt = new ObjectInputStream(fisInt);
                // Llenamos la memoria con (M / (B * 5)) bloques, pues un bloque de String=8 char=8 bytes necesita
                // 4 bloques de Integer = 2 int = 8 bytes cada uno, con lo que obtenemos M / (B * 5)
                this.arrayX.add((String) oisX.readObject());
                for (int j = 0; j < 4 ; j++)
                    this.arrayInts.add((ArrayList<Integer>) oisInt.readObject());
                oisX.close();
                fisX.close();
                oisInt.close();
                fisInt.close();
                calculateRow(upLeft, downLeft, row);
                writeRow();
            }
            row += 1;
            upLeft += 1;
            downLeft += 1;
        }
    }
    public void readBlocks() throws IOException, ClassNotFoundException {  }
    public void readRows(int row) throws IOException, ClassNotFoundException {
        this.arrayY = new ArrayList<String>();
        FileInputStream fisY = new FileInputStream(Y);
        ObjectInputStream oisY = new ObjectInputStream(fisY);
        this.arrayY.add((String) oisY.readObject());
    }

    public void calculateRow(int upLeft, int downLeft, int row) {
        for (int i = 0; i < this.arrayInts.size(); i ++) {
            /*if (arrayX.get(i) != arrayY.get(row)) {
                upLeft += 1;
            }*/
            //arrayInts.set(i, Math.min(upLeft, Math.min(downLeft + 1, arrayInts.get(i) + 1)));
        }
    }
}
