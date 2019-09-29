import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    // Parámetros
    // -Xmx 1GB (maximo uso de RAM) -Xms 64MB (minimo uso de RAM)
    private static final int N = (int) Math.pow(2, 3);
    private static final int B = (int) Math.pow(2, 2);
    public final static int M = 20 * B;
    public static int DISK_ACCESSES = 0;
    private static final String ALPHABET = "AB";//CDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    // Strings en memoria externa
    public static final String X = "StringX.bin";
    public static final String Y = "StringY.bin";
    public static final String IntegerList = "Integers.bin";
    // Librerias
    private static Random random = new Random();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Medición de tiempo
        long startTime;
        long endTime;
        // String Builder para escribir los strings en memoria
        StringBuilder strBuilderX = new StringBuilder();
        StringBuilder strBuilderY = new StringBuilder();
        // Array List para escribir los enteros
        ArrayList<Integer> intBuilder = new ArrayList<Integer>();
        // Output Stream para escribir strings
        FileOutputStream fosX = new FileOutputStream(X);
        ObjectOutputStream oosX = new ObjectOutputStream(fosX);
        FileOutputStream fosY = new FileOutputStream(Y);
        ObjectOutputStream oosY = new ObjectOutputStream(fosY);
        // Output Stream para escribir enteros iniciales
        FileOutputStream fosI = new FileOutputStream(IntegerList);
        ObjectOutputStream oosI = new ObjectOutputStream(fosI);
        // Crear N caracteres y agregarlos al string builder
        for (int i = 0; i < N; i++) {
            strBuilderX.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
            strBuilderY.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
            intBuilder.add(i+1);
            if ((i + 1) % (B / 4) == 0 || i == N - 1) {
                oosI.writeObject(intBuilder);
                intBuilder = new ArrayList<Integer>();
            }
            if ((i + 1) % B == 0 || i == N - 1) {
                oosX.writeObject(strBuilderX.toString());
                oosY.writeObject(strBuilderY.toString());
                strBuilderX = new StringBuilder();
                strBuilderY = new StringBuilder();
            }
        }
        oosX.close();
        oosY.close();
        fosX.close();
        fosY.close();
        // Leer X por bloques
        FileInputStream fisX = new FileInputStream(X);
        ObjectInputStream oisX = new ObjectInputStream(fisX);
        System.out.println("----- X -----");
        for(int j = 0; j < (int) Math.ceil((1.0 * N)/B); j++) {
            System.out.print(oisX.readObject());
        }
        System.out.println();
        oisX.close();
        fisX.close();
        // Leer Y por bloques
        FileInputStream fisY = new FileInputStream(Y);
        ObjectInputStream oisY = new ObjectInputStream(fisY);
        System.out.println("----- Y -----");
        for(int j = 0; j < (int) Math.ceil((1.0 * N)/B); j++) {
            System.out.print(oisY.readObject());
        }
        System.out.println();
        oisY.close();
        fisY.close();
        // Warm up
        FirstAlgorithm firstAlgorithm = new FirstAlgorithm(M, B);
        //firstAlgorithm.calculateDistance(X, Y, IntegerList, N);
        // Prueba real
        startTime = System.currentTimeMillis();
        int distance = firstAlgorithm.calculateDistance(X, Y, IntegerList, N);
        endTime = System.currentTimeMillis();
        System.out.println();
        System.out.print("La distancia es: ");
        System.out.println(distance);
        // Leer la lista de enteros por bloque (menos enteros pues son mas pesados)
        /*FileInputStream fisInt = new FileInputStream(IntegerList);
        ObjectInputStream oisInt = new ObjectInputStream(fisInt);
        System.out.println("---- Ints ----");
        for(int j = 0; j < (int) Math.ceil((1.0 * N) / (B / 4)); j++) {
            System.out.println(oisInt.readObject());
        }
        oisInt.close();
        fisInt.close();*/
        System.out.println("Tiempo total: " + (endTime - startTime) + " milisegundos");
        System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
    }
}
