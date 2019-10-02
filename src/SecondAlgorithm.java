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
        //Para las fronteras verticales
        FileInputStream fisV = new FileInputStream(VerticalList);
        ObjectInputStream oisV = new ObjectInputStream(fisV);
        //Para las fronteras horizontales
        FileInputStream fisH = new FileInputStream(HorizontalList);
        ObjectInputStream oisH = new ObjectInputStream(fisH);
        //Para los input X e Y
        FileInputStream fisY = new FileInputStream(Y);
        ObjectInputStream oisY = new ObjectInputStream(fisY);

        // Cantidad de bloques del string que se leen en cada iteración
        int blocks = 1;
        // (bloques X + bloques Y) + (frontera X + frontera Y) * size(int) + (cuadrícula interna) * size(int)
        int totalUsed = 2 * blocks + 4 * 2 * blocks + 4 * blocks * blocks;
        // Calcular cantidad de bloques por iteracion
        while (totalUsed < (M / this.B) && blocks < (N / this.B)) {
            blocks++;
            totalUsed = 2 * blocks + 4 * 2 * blocks + 4 * blocks * blocks;
            if (totalUsed > M) {
                blocks--;
                break;
            }
        }
        // Cantidad de bloques restantes inicial
        int blocksleftrow = (N / this.B);
        int blocksleftcol = blocksleftrow;

        // Cantidad de iteraciones (horizontal) (total es it al cuadrado)
        int it = (int) Math.ceil((N * 1.0) / (B * blocks));
        // Variable auxiliar de la izquierda superior
        int auxvar = 0;
        // Fila (i) y Columna (j) de las submatrices
        int i = 0;
        int j = 0;
        // Arreglos para guardar los resultados de los calculos de las submatrices
        ArrayList<Integer> row;
        ArrayList<Integer> col;
        ArrayList<Integer> rowaux = new ArrayList<Integer>();
        String subY;
        /*if (it > 1) {
            System.out.print("N° de iteraciones: ");
            System.out.println(it);
        }*/

        while (i < it) {
            //System.out.println("--i--");
            //System.out.println(i);
            // Setear la variable auxiliar como el valor de la primera fila de la cuadricula
            auxvar = i* blocks * B;
            // Re abrir archivos para leer X desde el principio
            FileInputStream fisX = new FileInputStream(X);
            ObjectInputStream oisX = new ObjectInputStream(fisX);
            // Setea la cantidad de bloques por leer según en que iteracion estamos
            int nblockscol;
            if (blocksleftcol >= blocks)
                nblockscol = blocks;
            else
                nblockscol = blocksleftcol;
            //System.out.print("N° de bloques restantes en la columna: ");
            //System.out.println(blocksleftcol);
            //System.out.print("Se leerán en esta iteracion: ");
            //System.out.println(nblockscol);
            // Se lee la primera columna de submatrices
            col = new ArrayList<Integer>();
            subY = "";
            // Llenamos la memoria con k bloques de strings, que tiene 4 bloques de enteros asociado
            for (int l = 0; l < nblockscol; l++) {
                subY += (String) oisY.readObject();
                for (int k = 0; k < 4; k++)
                    col.addAll((ArrayList<Integer>) oisV.readObject());
                Main.DISK_ACCESSES += 5;
            }
            //System.out.println("---- Col ----");
            //System.out.println(col);
            //System.out.println("---- subY ----");
            //System.out.println(subY);
            while (j < it) {
                //System.out.println("--j--");
                //System.out.println(j);
                int nblocksrow;
                // Setear cuantos bloques se leeran segun la iteracion en la que estamos
                if (blocksleftrow >= blocks)
                    nblocksrow = blocks;
                else
                    nblocksrow = blocksleftrow;
                //System.out.print("N° de bloques restantes en la fila: ");
                //System.out.println(blocksleftrow);
                //System.out.print("Se leerán en esta iteracion: ");
                //System.out.println(nblocksrow);
                // Se lee la fila de submatrices que viene en el archivo
                row = new ArrayList<Integer>();
                // Llenamos la memoria con k bloques de strings, que tiene 4 bloques de enteros asociado
                String subX = "";
                for (int l = 0; l < nblocksrow; l++) {
                    subX += (String) oisX.readObject();
                    for (int k = 0; k < 4; k++)
                        row.addAll((ArrayList<Integer>) oisH.readObject());
                    Main.DISK_ACCESSES += 5;
                }
                int newauxvar = row.get(row.size()-1);
                //System.out.println("---- Row ----");
                //System.out.println(row);
                //System.out.println("---- subX ----");
                //System.out.println(subX);
                // Calcula la submatriz correspondiente, y retorna la frontera inferior y vertical calculadas
                ArrayList<ArrayList<Integer>> newrowandcol = calculateMatrix(auxvar, row, col, subX, subY);
                // Asignamos la frontera inferior calculada a una fila auxiliar
                rowaux = newrowandcol.get(0);
                // Escribimos la fila resultante en memoria
                // Array List para escribir los enteros
                ArrayList<Integer> intTempBuilder = new ArrayList<Integer>();
                for (int m = 0; m < rowaux.size(); m++) {
                    intTempBuilder.add(rowaux.get(m));
                    if ((m + 1) % (B / 4) == 0) {
                        this.oosH.writeObject(intTempBuilder);
                        Main.DISK_ACCESSES++;
                        intTempBuilder = new ArrayList<Integer>();
                    }
                }
                // Cambiamos el col por la frontera vertical calculada
                col = newrowandcol.get(1);
                auxvar = newauxvar;
                //System.out.println("---- Col(calc) ----");
                //System.out.println(col);
                //System.out.println("---- subY ----");
                //System.out.println(subY);
                j++;
                blocksleftrow -= nblocksrow;
            }
            i++;
            j = 0;
            blocksleftcol -= nblockscol;
            blocksleftrow = (N / this.B);
            fisX.close();
            oisX.close();
        }
        fisH.close();
        oisH.close();
        fisY.close();
        oisY.close();
        return rowaux.get(rowaux.size() - 1);
    }

    public static ArrayList<ArrayList<Integer>> calculateMatrix(int upLeft, ArrayList<Integer> row, ArrayList<Integer> col,
                                                                String subX, String subY) {
        int up;
        int downLeft;
        int newDownLeft;
        int upLeftTemp;
        //System.out.println("upleft");
        //System.out.println(upLeft);
        for (int i = 0; i < col.size(); i++) {
            downLeft = col.get(i);
            upLeftTemp = col.get(i);
            //System.out.print("[");
            for (int j = 0; j < row.size(); j++) {
                up = row.get(j);
                if (subX.charAt(j) == subY.charAt(i))
                    newDownLeft = Math.min(upLeft, Math.min(downLeft + 1, up + 1));
                else
                    newDownLeft = Math.min(upLeft + 1, Math.min(downLeft + 1, up + 1));
                //System.out.print(newDownLeft);
                //System.out.print(",");
                row.set(j, newDownLeft);
                downLeft = newDownLeft;
                upLeft = up;
            }
            //System.out.println("]");
            col.set(i, downLeft);
            upLeft = upLeftTemp;
        }
        //System.out.println("  ----row----  ");
        //System.out.println(row);
        //System.out.println("  ----col----  ");
        //System.out.println(col);
        ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
        res.add(row);
        res.add(col);
        return res;
    }

}
