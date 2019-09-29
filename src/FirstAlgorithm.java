import java.io.*;
import java.util.ArrayList;

public class FirstAlgorithm {
    int M;
    int B;
    String X;
    String Y;
    String IntegerList;
    ArrayList<String> arrayX;
    ArrayList<Character> arrayY;
    ArrayList<ArrayList<Integer>> arrayInts;
    int upLeft = 0;
    int downLeft = 1;

    public FirstAlgorithm(int M, int B) {
        this.M = M;
        this.B = B;
        this.arrayX = new ArrayList<String>();
        this.arrayY = new ArrayList<Character>();
        this.arrayInts = new ArrayList<ArrayList<Integer>>();
    }

    public void calculateDistance(String X, String Y, String IntegerList, int N) throws IOException, ClassNotFoundException {
        this.X = X;
        this.Y = Y;
        this.IntegerList = IntegerList;
        int row = 0;
        readBlocks();
        System.out.println(arrayX);
        System.out.println(arrayInts);
        /*while (row < N) {
            if (row % this.B == 0)
                readRows(row);
            calculateRow(upLeft, downLeft, row);
            row += 1;
            upLeft += 1;
            downLeft += 1;
        }*/
    }
    public void readBlocks() throws IOException, ClassNotFoundException {
        FileInputStream fisX = new FileInputStream(X);
        ObjectInputStream oisX = new ObjectInputStream(fisX);
        FileInputStream fisInt = new FileInputStream(IntegerList);
        ObjectInputStream oisInt = new ObjectInputStream(fisInt);
        // M / 5 pues son 1 byte char + 4 byte int resultado = 5 bytes
        for (int i = 0; i < this.M / this.B; i++) {
            if ((i + 1) % 4 == 0) {
                String x = (String) oisX.readObject();
                arrayX.add(x);
            }
            arrayInts.add((ArrayList<Integer>) oisInt.readObject());
        }
        oisX.close();
        fisX.close();
        oisInt.close();
        fisInt.close();
    }
    public void readRows(int row) throws IOException {
        BufferedReader readerY = new BufferedReader(new FileReader(Y));
        char[] bufferY = new char[this.B];
        readerY.read(bufferY, row, this.B);
        for (char y : bufferY) {
            arrayY.add(y);
        }
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
