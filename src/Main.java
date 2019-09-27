import java.io.File;

public class Main {

    // Parámetros
    public final static int M = 160;
    private static final int N = (int) Math.pow(2, 14);
    public static int DISK_ACCESSES = 0;
    // Strings en memoria externa
    public static final String DIR_1 = "data" + File.separator;
    public static final String DIR_2 = "data" + File.separator;
    public static final File FILE_1 = new File(DIR_1 + "id");
    public static final File FILE_2 = new File(DIR_2 + "id");

    public static void main(String[] args) {
        long startTime;
        long endTime;
        // Crear strings progresivamente e ir guardandolos en memoria
        char x = 'x';
        System.out.println(x);
        //
        startTime = System.currentTimeMillis();
        // Funcion aqui
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo promedio: " + ((endTime - startTime) / (N / 10)) + " milisegundos");
        System.out.println("N° total de accesos a disco: " + DISK_ACCESSES);
        System.out.println("Tiempo total de búsquedas" + (endTime - startTime) + " milisegundos");
    }
}
