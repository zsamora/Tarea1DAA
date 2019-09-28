import java.io.*;

public class WriteAndRead {

    public int calculateDistancev2(String w1, String w2, int buffer_size) {
        int n = w1.length();
        // int b = 2**10;
        // int j = 0;
        int[][] arr = new int[buffer_size][buffer_size];
        for(int j=0; j<buffer_size; j++){
            for(int i=0; i<buffer_size; i++){
                arr[j][i] = i+j;
            }
        }
        System.out.println("Arreglo antes de escribirse:");
        for(int j=0; j<buffer_size; j++){
            for(int i=0; i<buffer_size; i++){
                System.out.print(arr[j][i]);
                System.out.print(" ");
            }
            System.out.println("");
        }
        
        String dir = "/home/jhon/Escritorio/logaritmos/Tarea1DAA/src"; // Carpeta en donde esta el archivo
        String fileName = "rec.bin"; // Nombre del archivo

        // Se guarda el archivo
        try {
            FileOutputStream fos = new FileOutputStream(dir + "/" + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for(int j=0; j<buffer_size; j++){
                oos.writeObject(arr[j]);
            }
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ahora tratamos de leerlo
        try {
            FileInputStream fis = new FileInputStream(dir + "/" + fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            int[][] arr_read = new int[3][3];
            for(int j=0; j<buffer_size; j++){
                arr_read[j] = (int[]) ois.readObject();
            }
            ois.close();
            fis.close();
            // Despues de leerlo imprimimos sus valores
            System.out.println("");
            System.out.println("Arreglo leído desde el archivo:");
            for(int j=0; j<buffer_size; j++){
                System.out.print("Arreglo n° ");
                System.out.println(j+1);
                for(int i=0; i<buffer_size; i++){
                    System.out.print(arr_read[j][i]);
                    System.out.print(" ");
                }
                System.out.println("");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void main(String[] args) {
        WriteAndRead fa = new WriteAndRead();
        fa.calculateDistancev2("test", "test", 3);
    }
}