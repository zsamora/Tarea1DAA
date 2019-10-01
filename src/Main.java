import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static int DISK_ACCESSES;
    
    // alphabet
    private static final String ALPHABET = "ABCDFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // sizes
    private static final int[] n_size = {(int)Math.pow(2, 10), (int)Math.pow(2, 11), (int)Math.pow(2, 12), (int)Math.pow(2, 13)};
    private static final int[] n_pow = {10, 11, 12, 13};
    private static final int B = (int) Math.pow(2, 10);
    private static final int[] m_size = {B*20, B*40, B*80};
    
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
        
        // Medir con todas las combinaciones de tamaños.
        for(int n=0; n<n_size.length; n++){

            // Set disk accesses to 0
            DISK_ACCESSES = 0;

            // Output Stream para escribir strings
            FileOutputStream fosX = new FileOutputStream(X);
            FileOutputStream fosY = new FileOutputStream(Y);
            ObjectOutputStream oosX = new ObjectOutputStream(fosX);
            ObjectOutputStream oosY = new ObjectOutputStream(fosY);
            
            // Output Stream para escribir enteros iniciales
            FileOutputStream fosI = new FileOutputStream(IntegerList);
            ObjectOutputStream oosI = new ObjectOutputStream(fosI);
            
            // Crear N caracteres y agregarlos al string builder
            for (int i = 0; i < n_size[n]; i++) {
                strBuilderX.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
                strBuilderY.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
                intBuilder.add(i+1);
                if ((i + 1) % (B / 4) == 0 || i == n_size[n] - 1) {
                    oosI.writeObject(intBuilder);
                    DISK_ACCESSES ++;
                    intBuilder = new ArrayList<Integer>();
                }
                if ((i + 1) % B == 0 || i == n_size[n] - 1) {
                    oosX.writeObject(strBuilderX.toString());
                    DISK_ACCESSES ++;
                    oosY.writeObject(strBuilderY.toString());
                    DISK_ACCESSES ++;
                    strBuilderX = new StringBuilder();
                    strBuilderY = new StringBuilder();
                }
            }
            oosX.close();
            oosY.close();
            fosX.close();
            fosY.close();

            int DISK_ACCESSES_TEMP = DISK_ACCESSES; 

            for(int m=0; m<m_size.length; m++){

                // Printing test settings
                System.out.print("Prueba con N = 2^");
                System.out.print(n_pow[n]);
                System.out.print(" y M = B*");
                System.out.println(m_size[m]/B);


                // Set disk accesses
                DISK_ACCESSES = DISK_ACCESSES_TEMP;

                // Set algorithms
                firstAlgorithm = new FirstAlgorithm(m_size[m], B);
                // ACA INICIALIZAR EL SEGUNDO ALGORITMO
                
                if(m==0){
                    // Run first algorithm measuring the time
                    startTime = System.currentTimeMillis();
                    int distance = firstAlgorithm.calculateDistance(X, Y, IntegerList, n_size[n]);
                    endTime = System.currentTimeMillis();

                    // Print the results
                    System.out.println("");
                    System.out.println("Primer algoritmo:");
                    System.out.print("La distancia es: ");
                    System.out.println(distance);
                    System.out.println("Tiempo total: " + (endTime - startTime) + " milisegundos");
                    System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
                    System.out.println("");
                }

                // Set disk accesses again
                DISK_ACCESSES = DISK_ACCESSES_TEMP;

                // Run seoncd algorithm measuring the time
                startTime = System.currentTimeMillis();
                // ACA CORRER EL SEGUNDO ALGORITMO!!
                // int distance2 = secondAlgorithm.calculateDistance(X, Y, IntegerList, n_size[n]);
                endTime = System.currentTimeMillis();

                // Print the results
                System.out.println("");
                System.out.println("Segundo algoritmo:");
                System.out.print("La distancia es: ");
                // AL AGREGAR EL SEGUNDO ALGORITMO DESCOMENTAR LA LINEA SIGUIENTE!!
                // System.out.println(distance2);
                System.out.println("Tiempo total: " + (endTime - startTime) + " milisegundos");
                System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
                System.out.println("");

                System.out.println("");

            }
        }
        // // Output Stream para escribir fronteras horizontales
        // FileOutputStream fosH = new FileOutputStream(HorizontalList);
        // ObjectOutputStream oosH = new ObjectOutputStream(fosH);
        // // Output Stream para escribir fronteras verticales
        // FileOutputStream fosV = new FileOutputStream(VerticalList);
        // ObjectOutputStream oosV = new ObjectOutputStream(fosV);
        // Crear N caracteres y agregarlos al string builder
        // for (int i = 0; i < N; i++) {
        //     strBuilderX.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        //     strBuilderY.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        //     intBuilder.add(i+1);
        //     if ((i + 1) % (B / 4) == 0 || i == N - 1) {
        //         oosI.writeObject(intBuilder);
        //         oosH.writeObject(intBuilder);
        //         oosV.writeObject(intBuilder);
        //         intBuilder = new ArrayList<Integer>();
        //         DISK_ACCESSES +=3;

        // oosI.close();
        // oosV.close();
        // fosI.close();
        // // Leer X por bloques
        // FileInputStream fisX = new FileInputStream(X);
        // ObjectInputStream oisX = new ObjectInputStream(fisX);
        // System.out.println("----- X -----");
        // for(int j = 0; j < (int) Math.ceil((1.0 * N)/B); j++) {
        //     System.out.print(oisX.readObject());
        // }
        // System.out.println();
        // oisX.close();
        // fisX.close();
        // // Leer Y por bloques
        // FileInputStream fisY = new FileInputStream(Y);
        // ObjectInputStream oisY = new ObjectInputStream(fisY);
        // System.out.println("----- Y -----");
        // for(int j = 0; j < (int) Math.ceil((1.0 * N)/B); j++) {
        //     System.out.print(oisY.readObject());
        // }
        // System.out.println();
        // oisY.close();
        // fisY.close();
        // // Warm up
        // FirstAlgorithm firstAlgorithm = new FirstAlgorithm(M, B);
        // SecondAlgorithm secondAlgorithm = new SecondAlgorithm(M, B, oosH);
        // //firstAlgorithm.calculateDistance(X, Y, IntegerList, N);
        // // Prueba real
        // startTime = System.currentTimeMillis();
        // int distance = firstAlgorithm.calculateDistance(X, Y, IntegerList, N);
        // //int distance2 = secondAlgorithm.calculateDistance(X, Y, HorizontalList, VerticalList, N);
        // endTime = System.currentTimeMillis();
        // System.out.println();
        // System.out.print("La distancia es: ");
        // System.out.println(distance);
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
