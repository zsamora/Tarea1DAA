import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FirstAlgorithm {
    int M;
    int B;
    String X;
    String Y;
    String IntegerList = "Integers.txt";
    ArrayList<Character> arrayX;
    ArrayList<Character> arrayY;
    ArrayList<Integer> arrayInts;
    ArrayList<Integer> arrayIntsRes;
    int upLeft = 0;
    int downLeft = 1;

    public FirstAlgorithm(int M, int B) {
        this.M = M;
        this.B = B;
    }

    public int calculateDistance (String X, String Y, int N) throws IOException {
        this.X = X;
        this.Y = Y;
        int row = 0;
        readBlocks();
        while (row < N) {
            if (row % this.B == 0)
                readRows(row);
            calculateRow(upLeft, downLeft, row);
            row += 1;
            upLeft += 1;
            downLeft += 1;
        }
    }
    public void readBlocks() throws IOException {
        BufferedReader readerX = new BufferedReader(new FileReader(X));
        BufferedReader readerInt = new BufferedReader(new FileReader(IntegerList));
        char[] bufferX = new char[this.B];
        char[] bufferInt = new char[this.B];
        // M / 5 pues son 1 byte char + 4 byte int resultado = 5 bytes
        for (int i = 0; i < this.M / 5; i += this.B) {
            readerX.read(bufferX, i, this.B);
            for (char x : bufferX) {
                arrayX.add(x);
            }
            readerInt.read(bufferInt, i, this.B);
            for (char j : bufferInt) {
                arrayInts.add(Character.getNumericValue(j));
            }
        }
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
            if (arrayX.get(i) != arrayY.get(row)) {
                upLeft += 1;
            }
            arrayInts.set(i, Math.min(upLeft, Math.min(downLeft + 1, arrayInts.get(i) + 1)));
        }
    }
}
