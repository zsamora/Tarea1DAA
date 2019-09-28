import java.io.*;
import java.util.Random;

public class Main {

    // Parámetros
    public final static int M = 160;
    private static final int N = (int) Math.pow(2, 1);
    public static int DISK_ACCESSES = 0;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    // Strings en memoria externa
    public static final String X = "StringX.txt";
    public static final String Y = "StringY.txt";
    // Librerias
    private static Random random = new Random();

    public static void main(String[] args) {
        // Medición de tiempo
        long startTime;
        long endTime;
        // String Builder para escribir los strings en memoria
        StringBuilder strBuilderX = new StringBuilder();
        StringBuilder strBuilderY = new StringBuilder();
        // Crear N caracteres y agregarlos al string builder
        for (int i = 0; i < N; i++) {
            strBuilderX.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
            strBuilderY.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        try {
            BufferedWriter writerX = new BufferedWriter(new FileWriter(X));
            BufferedWriter writerY = new BufferedWriter(new FileWriter(Y));
            writerX.write(strBuilderX.toString());
            writerY.write(strBuilderY.toString());
            writerX.close();
            writerY.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startTime = System.currentTimeMillis();
        // Funcion aqui
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo promedio: " + (endTime - startTime) + " milisegundos");
        System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
        System.out.println("Tiempo total de búsquedas: " + (endTime - startTime) + " milisegundos");
    }
}
