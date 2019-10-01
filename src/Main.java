import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static int DISK_ACCESSES;

    // alphabet
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // sizes
    private static final int[] n_size = {(int) Math.pow(2, 10), (int) Math.pow(2, 11), (int)Math.pow(2, 12),
                                         (int)Math.pow(2, 13), (int)Math.pow(2, 14), (int)Math.pow(2, 15), (int)Math.pow(2, 16)};
    private static final int[] n_pow = {10, 11, 12, 13, 14, 15, 16};
    private static final int B = (int) Math.pow(2, 10);
    private static final int[] m_size = {B * 20, B * 40, B * 80};

    // Strings en memoria externa
    public static final String X = "StringX.bin";
    public static final String Y = "StringY.bin";
    public static final String IntegerList = "Integers.bin";
    public static final String HorizontalList = "Horizontal.bin";
    public static final String VerticalList = "Vertical.bin";

    // Librerias
    private static Random random = new Random();


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Medición de tiempo
        long startTime;
        long endTime;

        // Array List para escribir los enteros
        ArrayList<Integer> intBuilder = new ArrayList<Integer>();

        // String Builder para escribir los strings en memoria
        StringBuilder strBuilderX = new StringBuilder();
        StringBuilder strBuilderY = new StringBuilder();

        // First algorithm instance
        FirstAlgorithm firstAlgorithm;
        // Second algorithm instance;
        SecondAlgorithm secondAlgorithm;

        // Medir con todas las combinaciones de tamaños.
        for (int n = 0; n < n_size.length; n++) {

            // Set disk accesses to 0
            DISK_ACCESSES = 0;

            // Output Stream para escribir strings
            FileOutputStream fosX = new FileOutputStream(X);
            FileOutputStream fosY = new FileOutputStream(Y);
            ObjectOutputStream oosX = new ObjectOutputStream(fosX);
            ObjectOutputStream oosY = new ObjectOutputStream(fosY);

            // Output Stream para escribir enteros iniciales y fronteras verticales
            FileOutputStream fosI = new FileOutputStream(IntegerList);
            ObjectOutputStream oosI = new ObjectOutputStream(fosI);
            FileOutputStream fosV = new FileOutputStream(VerticalList);
            ObjectOutputStream oosV = new ObjectOutputStream(fosV);

            // Crear N caracteres y agregarlos al string builder, y los enteros correspondientes
            for (int i = 0; i < n_size[n]; i++) {
                strBuilderX.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
                strBuilderY.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
                intBuilder.add(i + 1);
                // Escribir en memoria los enteros
                if ((i + 1) % (B / 4) == 0 || i == n_size[n] - 1) {
                    oosI.writeObject(intBuilder);
                    oosV.writeObject(intBuilder);
                    DISK_ACCESSES++; // 1 para el primer algoritmo o 1 para el segundo
                    intBuilder = new ArrayList<Integer>();
                }
                // Escribir en memoria los strings
                if ((i + 1) % B == 0 || i == n_size[n] - 1) {
                    oosX.writeObject(strBuilderX.toString());
                    oosY.writeObject(strBuilderY.toString());
                    DISK_ACCESSES += 2; // 2 para el primer algoritmo o 2 para el segundo
                    strBuilderX = new StringBuilder();
                    strBuilderY = new StringBuilder();
                }
            }
            fosX.close();
            fosY.close();
            fosI.close();
            fosV.close();
            oosX.close();
            oosY.close();
            oosI.close();
            oosV.close();

            // Leer X para testear
            FileInputStream fisX = new FileInputStream(X);
            ObjectInputStream oisX = new ObjectInputStream(fisX);
            System.out.println("----- X -----");
            for (int j = 0; j < (int) Math.ceil((1.0 * n_size[n]) / B); j++) {
                System.out.print(oisX.readObject());
            }
            System.out.println();
            oisX.close();
            fisX.close();
            // Leer Y para testear
            FileInputStream fisY = new FileInputStream(Y);
            ObjectInputStream oisY = new ObjectInputStream(fisY);
            System.out.println("----- Y -----");
            for (int j = 0; j < (int) Math.ceil((1.0 * n_size[n]) / B); j++) {
                System.out.print(oisY.readObject());
            }
            System.out.println();
            System.out.println();
            oisY.close();
            fisY.close();

            int DISK_ACCESSES_TEMP = DISK_ACCESSES; // Accesos a disco inicial antes de ejecutar los algoritmos


            for (int m = 0; m < m_size.length; m++) {
                // Warmup con 2^10 y M = 20*B
                if (n == 0 && m == 0) {
                    System.out.print("Warm up con N = 2^");
                    System.out.print(n_pow[n]);
                    System.out.print(" y M = B*");
                    System.out.println(m_size[m] / B);
                    System.out.println("");

                    // Primer algoritmo
                    firstAlgorithm = new FirstAlgorithm(m_size[m], B);
                    startTime = System.currentTimeMillis();
                    int distance = firstAlgorithm.calculateDistance(X, Y, IntegerList, n_size[n]);
                    endTime = System.currentTimeMillis();
                    System.out.println("Warmup del primer algoritmo:");
                    System.out.print("La distancia es: ");
                    System.out.println(distance);
                    System.out.println("Tiempo total: " + (endTime - startTime) + " milisegundos");
                    System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
                    System.out.println("");

                    DISK_ACCESSES = DISK_ACCESSES_TEMP;

                    // Segundo algoritmo
                    FileOutputStream fosH = new FileOutputStream(HorizontalList);
                    ObjectOutputStream oosH = new ObjectOutputStream(fosH);
                    for (int i = 0; i < n_size[n]; i++) {
                        intBuilder.add(i + 1);
                        // Escribir en memoria los enteros
                        if ((i + 1) % (B / 4) == 0 || i == n_size[n] - 1) {
                            oosH.writeObject(intBuilder);
                            DISK_ACCESSES++;
                            intBuilder = new ArrayList<Integer>();
                        }
                    }
                    secondAlgorithm = new SecondAlgorithm(m_size[m], B, oosH);
                    startTime = System.currentTimeMillis();
                    int distance2 = secondAlgorithm.calculateDistance(X, Y, HorizontalList, VerticalList, n_size[n]);
                    endTime = System.currentTimeMillis();
                    System.out.println("Warmup del segundo algoritmo:");
                    System.out.print("La distancia es: ");
                    System.out.println(distance2);
                    System.out.println("Tiempo total: " + (endTime - startTime) + " milisegundos");
                    System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
                    System.out.println("");
                    fosH.close();
                    oosH.close();

                    // Set disk accesses again
                    DISK_ACCESSES = DISK_ACCESSES_TEMP;
                }

                // Printing test settings
                System.out.print("Prueba con N = 2^");
                System.out.print(n_pow[n]);
                System.out.print(" y M = B*");
                System.out.println(m_size[m] / B);
                System.out.println("");


                // Set disk accesses
                DISK_ACCESSES = DISK_ACCESSES_TEMP;

                // Set first algorithm
                firstAlgorithm = new FirstAlgorithm(m_size[m], B);

                // Correr solo cuando N <= 2^13
                if (n_pow[n] <= 13) {
                    // Run first algorithm measuring the time
                    startTime = System.currentTimeMillis();
                    int distance = firstAlgorithm.calculateDistance(X, Y, IntegerList, n_size[n]);
                    endTime = System.currentTimeMillis();

                    // Print the results
                    System.out.println("Primer algoritmo:");
                    System.out.print("La distancia es: ");
                    System.out.println(distance);
                    System.out.println("Tiempo total: " + (endTime - startTime) + " milisegundos");
                    System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
                    System.out.println("");
                }

                // Set disk accesses again
                DISK_ACCESSES = DISK_ACCESSES_TEMP;

                // Se deben escribir las fronteras horizontales justo antes de instanciar el segundo algoritmo
                // para dejar el puntero justo al final del archivo
                FileOutputStream fosH = new FileOutputStream(HorizontalList);
                ObjectOutputStream oosH = new ObjectOutputStream(fosH);
                for (int i = 0; i < n_size[n]; i++) {
                    intBuilder.add(i + 1);
                    // Escribir en memoria los enteros
                    if ((i + 1) % (B / 4) == 0 || i == n_size[n] - 1) {
                        oosH.writeObject(intBuilder);
                        DISK_ACCESSES++;
                        intBuilder = new ArrayList<Integer>();
                    }
                }
                secondAlgorithm = new SecondAlgorithm(m_size[m], B, oosH);
                // Run second algorithm measuring the time
                startTime = System.currentTimeMillis();
                int distance2 = secondAlgorithm.calculateDistance(X, Y, HorizontalList, VerticalList, n_size[n]);
                endTime = System.currentTimeMillis();

                // Print the results
                System.out.println("Segundo algoritmo:");
                System.out.print("La distancia es: ");
                System.out.println(distance2);
                System.out.println("Tiempo total: " + (endTime - startTime) + " milisegundos");
                System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
                System.out.println("");
                fosH.close();
                oosH.close();
            }
        }
        // // Leer la lista de enteros por bloque (menos enteros pues son mas pesados)
        // /*FileInputStream fisInt = new FileInputStream(HorizontalList);
        // ObjectInputStream oisInt = new ObjectInputStream(fisInt);
        // System.out.println("---- Ints ----");
        // for(int j = 0; j < (int) Math.ceil((1.0 * N) / (B / 4)); j++) {
        //     System.out.println(oisInt.readObject());
        // }
        // oisInt.close();
        // fisInt.close();*/
        // System.out.println("Tiempo total: " + (endTime - startTime) + " milisegundos");
        // System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
    }
}
