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

    public FirstAlgorithm(int M, int B, String X, String Y) {
        this.M = M;
        this.B = B;
        this.X = X;
    }
    public void readBlocks(int k) throws IOException {
        BufferedReader readerX = new BufferedReader(new FileReader(X));
        BufferedReader readerY = new BufferedReader(new FileReader(Y));
        BufferedReader readerInt = new BufferedReader(new FileReader(IntegerList));
        char[] bufferX = new char[this.B];
        char[] bufferY = new char[this.B];
        char[] bufferInt = new char[this.B];
        // M / 5 pues son 1 byte char + 4 byte int resultado = 5 bytes
        for (int i = 0; i < this.M / 5; i += this.B) {
            readerX.read(bufferX, i, this.B);
            for (char x: bufferX) {
                arrayX.add(x);
            }
            readerInt.read(bufferInt, i, this.B);
            for (char j: bufferInt) {
                arrayInts.add(Character.getNumericValue(j));
            }

        }
        readerY.read(bufferY, k, this.B);
        for (char y: bufferY) {
            arrayY.add(y);
        }
    }
}
