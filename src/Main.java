import java.io.File;

public class Main {

    public final static int MAX_M = 160;
    public final static int MIN_M = (int) (MAX_M * 0.4);
    public static int DISK_ACCESSES = 0;
    public static final String DIR = "data" + File.separator;
    public static final File FILE = new File(DIR + "id");
    private static final int N = (int) Math.pow(2, 14);

    public static void main(String[] args) {

        long startTime;
        long endTime;
        // Search
        startTime = System.currentTimeMillis();
        // Funcion
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo promedio: " + ((endTime - startTime) / (N / 10)) + " milisegundos");
        System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
        System.out.println("Tiempo total de búsquedas" + (endTime - startTime) + " milisegundos");
    }
}
