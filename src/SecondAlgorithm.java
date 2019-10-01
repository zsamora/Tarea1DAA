import java.io.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

public class SecondAlgorithm {
    String X;
    String Y;
    int N;
    int M;
    int B;
    String filesDir = "C:\\Users\\Agustín\\Desktop\\Material Universidad\\2019-2\\Diseño y Análisis de Algoritmos\\Tarea 1\\files";
    ObjectOutputStream oosH;
    String HorizontalList;
    String VerticalList;


    public SecondAlgorithm(int M, int B, ObjectOutputStream oosH) {
        this.M = M;
        this.B = B;
        this.oosH = oosH;
    }

    public int calculateDistance(String X, String Y, String HorizontalList, String VerticalList, int N) throws IOException, ClassNotFoundException {
        this.X = X;
        this.Y = Y;
        this.HorizontalList = HorizontalList;
        this.VerticalList = VerticalList;
        //Para las fronteras horizontales
        FileInputStream fisH = new FileInputStream(HorizontalList);
        ObjectInputStream oisH = new ObjectInputStream(fisH);
        //Para las fronteras horizontales
        FileInputStream fisV = new FileInputStream(VerticalList);
        ObjectInputStream oisV = new ObjectInputStream(fisV);
        //Para los input X e Y
        FileInputStream fisY = new FileInputStream(Y);
        ObjectInputStream oisY = new ObjectInputStream(fisY);

        // Cantidad de bloques del string que se leen en cada iteración
        int blocks = 1;
        // (bloques X + bloques Y) + (frontera X + frontera Y) * size(int) + (cuadrícula interna) * size(int)
        int totalUsed = 2 * blocks + 4 * 2 * blocks + 4 * blocks * blocks;
        // Calcular cantidad de bloques
        while (totalUsed < (M / this.B) && blocks < (N / this.B)) {
            blocks++;
            totalUsed = 2 * blocks + 4 * 2 * blocks + 4 * blocks * blocks;
            if (totalUsed > M) {
                blocks--;
                break;
            }
        }
        // Cantidad de iteraciones (horizontal) (total es it al cuadrado)
        int it = N / (B * blocks);
        // Variables en las esquinas izquierdas superior
        int rowvar = 0;
        int auxvar = 0;
        int i = 0; // fila de submatriz
        int j = 0; // columna de submatriz
        ArrayList<Integer> row;
        ArrayList<Integer> rowaux = new ArrayList<Integer>();
        ArrayList<Integer> col = new ArrayList<Integer>();
        ArrayList<Integer> colaux = new ArrayList<Integer>();
        String subY = "";
        while (i < it) {
            // Aumentar la variable auxiliar de la fila
            if (i > 0)
                rowvar += blocks*B;
            auxvar += rowvar;
            row = rowaux;
            FileInputStream fisX = new FileInputStream(X);
            ObjectInputStream oisX = new ObjectInputStream(fisX);
            while (j < it) {
                // Llenamos la memoria con k bloques de strings, que tiene 4 bloques de enteros asociado
                String subX = "";
                for (int l = 0; l < blocks; l++) {
                    subX += (String) oisX.readObject();
                    for (int k = 0; k < 4; k++)
                        row.addAll((ArrayList<Integer>) oisH.readObject());
                    Main.DISK_ACCESSES += 5;
                }
                System.out.println("---- Row ----");
                System.out.println(row);
                System.out.println("---- subX ----");
                System.out.println(subX);
                // Primera columna de submatrices
                if (j == 0) {
                    // Llenamos la memoria con k bloques de strings, que tiene 4 bloques de enteros asociado
                    for (int l = 0; l < blocks; l++) {
                        subY += (String) oisY.readObject();
                        for (int k = 0; k < 4; k++)
                            col.addAll((ArrayList<Integer>) oisV.readObject());
                        Main.DISK_ACCESSES += 5;
                    }
                    System.out.println("---- Col ----");
                    System.out.println(col);
                    System.out.println("---- subY ----");
                    System.out.println(subY);
                }
                else {
                    // Cambiamos el col con el colaux, frontera vertical calculada
                    col = colaux;
                    System.out.println("---- Col ----");
                    System.out.println(col);
                }
                ArrayList<ArrayList<Integer>> newrowandcol = calculateMatrix(auxvar, row, col, subX, subY);
                rowaux = newrowandcol.get(0);
                colaux = newrowandcol.get(1);
                auxvar += blocks*B;
                j++;
            }
            i++;
            fisX.close();
            oisX.close();
        }
        fisY.close();
        oisY.close();
        return rowaux.get(rowaux.size()-1);
    }
    public static ArrayList<ArrayList<Integer>> calculateMatrix (int upLeft, ArrayList<Integer> row, ArrayList<Integer> col,
                                                                 String subX, String subY) {
        ArrayList<Integer> rowaux;
        int up;
        int downLeft;
        int newDownLeft;
        for (int i = 0; i < col.size(); i++) {
            downLeft = col.get(i);
            rowaux = new ArrayList<Integer>();
            for (int j = 0; j < row.size(); j++) {
                up = row.get(j);
                if (subX.charAt(j) == subY.charAt(i))
                    newDownLeft = Math.min(upLeft, Math.min(downLeft + 1, up + 1));
                else
                    newDownLeft = Math.min(upLeft + 1, Math.min(downLeft + 1, up + 1));
                rowaux.add(newDownLeft);
                downLeft = newDownLeft;
                upLeft = up;
                if (j == row.size() - 1) {
                    col.set(i,newDownLeft);
                }
            }
            row = rowaux;
        }
        ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
        res.add(row);
        res.add(col);
        return res;
    }
}
