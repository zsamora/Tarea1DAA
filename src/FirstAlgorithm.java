import java.io.*;
import java.util.ArrayList;

public class FirstAlgorithm {
    int M; // Memoria a usar
    int B; // Tamaño del bloque
    int upLeft; // Variable que guarda el valor de la casilla de la diagonal izquierda superior
    int downLeft; // Variable que guarda el valor de la casilla de la diagonal izquierda inferior
    int upLeftTemp; // Variables temporales, que se modifican cuando se itera dentro de una misma fila
    int downLeftTemp;
    String X; // Archivo donde está guardado el string X (horizontal)
    String Y; // Archivo donde está guardado el string Y (horizontal)
    String IntegerList; // Archivo donde está guardado el arreglo de enteros iniciales (del 1 a N por bloques)
    String subX; // Substring del string X utilizado para comparar
    String subY; // Substring del string Y utilizado para comparar
    ArrayList<Integer> arrayInts; // Arreglo de enteros leídos del archivo de enteros
    ArrayList<Integer> arrayIntsTemp; // Arreglo de enteros temporales resultantes del calculo

    public FirstAlgorithm(int M, int B) {
        this.M = M;
        this.B = B;
        upLeft = 0;
        downLeft = 1;
    }

    public int calculateDistance(String X, String Y, String IntegerList, int N) throws IOException, ClassNotFoundException {
        // Asignacion de nombres de archivo
        this.X = X;
        this.Y = Y;
        this.IntegerList = IntegerList;
        // Columnas y filas inciales
        int row = 0;
        int col = 0;
        int result = -1; // Resultado de la última casilla
        // Inicialización del stream de lectura del string Y (vertical)
        FileInputStream fisY = new FileInputStream(Y);
        ObjectInputStream oisY = new ObjectInputStream(fisY);
        // Iteración sobre todas las filas del string
        while (row < N) {
            // Leemos un bloque del string Y (vertical), si pasamos B filas (caracteres)
            if (row % this.B == 0)
                this.subY = (String) oisY.readObject();
            Main.DISK_ACCESSES++;
            // Abrimos los Stream para lectura de archivos del string X (horizontal) y la fila de enteros en memoria
            FileInputStream fisX = new FileInputStream(X);
            ObjectInputStream oisX = new ObjectInputStream(fisX);
            FileInputStream fisInt = new FileInputStream(IntegerList);
            ObjectInputStream oisInt = new ObjectInputStream(fisInt);
            // Array para guardar todos los enteros calculados en la fila
            this.arrayIntsTemp = new ArrayList<Integer>();
            // Seteamos los valores upLeftTemp y downLeftTemp como los de la columna inicial
            this.upLeftTemp = this.upLeft;
            this.downLeftTemp = this.downLeft;
            // Leemos el string X (horizontal), de a un bloque por iteración (N/B veces) TODO
            while (col < N / this.B) {
                // Reiniciamos el array del primer string y los valores calculados en el array de enteros
                this.arrayInts = new ArrayList<Integer>();
                // Llenamos la memoria solo con un bloque de caracteres, que tiene 4 bloques de enteros asociado TODO
                this.subX = (String) oisX.readObject();
                for (int j = 0; j < 4 ; j++)
                    this.arrayInts.addAll((ArrayList<Integer>) oisInt.readObject());
                Main.DISK_ACCESSES+=5;
                //System.out.println("---- Int ----");
                //System.out.println(arrayInts);
                //System.out.println("---- subX ----");
                //System.out.println(subX);
                // Calculamos la fila (% B pues tenemos un bloque de string de tamaño B)
                calculateRow(row % this.B);
                //System.out.println("---- ResInt ----");
                //System.out.println(arrayIntsTemp);
                col += 1;
            }
            oisInt.close();
            fisInt.close();
            oisX.close();
            fisX.close();
            // Escribimos todos los enteros resueltantes en el archivo de enteros
            result = writeRow();
            row += 1;
            this.upLeft += 1;
            this.downLeft += 1;
            col = 0;
        }
        return result;
    }

    public void calculateRow(int row) throws IOException {
        int newDownLeft; // Variable temporal que guarda la casilla resultante
        int up; // Variable temporal que guarda el entero del array correspondiente
        //System.out.println("---- charY ----");
        //System.out.println(subY.charAt(row));
        for (int i = 0; i < this.arrayInts.size(); i ++) {
            up = arrayInts.get(i);
            if (subX.charAt(i) == subY.charAt(row))
                newDownLeft = Math.min(this.upLeftTemp, Math.min(this.downLeftTemp + 1, up + 1));
            else
                newDownLeft = Math.min(this.upLeftTemp + 1, Math.min(this.downLeftTemp + 1, up + 1));
            // Agrega el nuevo resultado al arreglo de resultados
            this.arrayIntsTemp.add(newDownLeft);
            // Actualiza los valores de las casillas superior/inferior izquierda
            this.downLeftTemp = newDownLeft;
            this.upLeftTemp = up;
        }
    }

    public int writeRow() throws IOException {
        // Array List para escribir los enteros
        ArrayList<Integer> intTempBuilder = new ArrayList<Integer>();
        // Output Stream para escribir enteros temporales en el archivo de enteros
        FileOutputStream fosI = new FileOutputStream(IntegerList);
        ObjectOutputStream oosI = new ObjectOutputStream(fosI);
        for (int i = 0; i < arrayIntsTemp.size(); i++) {
            intTempBuilder.add(arrayIntsTemp.get(i));
            if ((i + 1) % (B / 4) == 0) {
                Main.DISK_ACCESSES++;
                oosI.writeObject(intTempBuilder);
                intTempBuilder = new ArrayList<Integer>();
            }
        }
        fosI.close();
        oosI.close();
        return arrayIntsTemp.get(arrayIntsTemp.size()-1);
    }
}
