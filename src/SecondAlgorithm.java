import java.io.*;
import java.lang.Math;
import java.util.Arrays;

public class SecondAlgorithm {
    String X;
    String Y;
    int N;
    int M;
    String filesDir;

    //leer la frontera de entrada y almacenarla en un arreglo que contenga espacio para los nuevos elementos (inFrontier)

    public void fillTable(char[] verticalC, char[] horizontalC, int[][] inFrontier ){

        int weight;

        for(int row=1;row<N;row++){
            for(int column=1;column<N;column++){
                if(verticalC[row-1]==horizontalC[column-1]) {
                    weight = 0;
                }else{
                    weight=1;
                }
                inFrontier[row][column]=Math.min(Math.min(inFrontier[row][column-1]+1,
                        inFrontier[row-1][column]+1),inFrontier[row-1][column-1]+weight);


            }
        }


    }

    public void calculateDistance(String X, String Y) throws IOException, ClassNotFoundException {
        this.X = X;
        this.Y = Y;
        //cantidad de bloques del string que se leen en cada iteración
        int blocks=1;
        int totalUsed=2*blocks+4*2*blocks+4*blocks*blocks;
        while(totalUsed<M){
            blocks++;
        }
        if(totalUsed>M){
            blocks--;
        }
        int[][] table=new int[blocks*128+1][blocks*128+1];
        //cantidad de iteraciones (horizontal)
        int it = (N/128)/blocks;
        for(int i=0;i<it;i++){
            for(int j=0;j<it;j++){





            }
        }



    }

    public void readToTable(int[][] table,String row_name,String column_name,int blockNum){

        int[] destArray=new int[N+1];
        String name=column_name;

        for(int j=0;j<2;j++) {
            try {
                FileInputStream fis = new FileInputStream(filesDir + "\\" + name);
                ObjectInputStream ois = new ObjectInputStream(fis);
                destArray = (int[]) ois.readObject();
                ois.close();
                fis.close();
                //System.out.println(object2.toString()); // Despues de leerlo imprimimos sus valores
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(j==0){
                //se inserta la primera columna en la tabla
                for (int i = 0; i < destArray.length; i++) {
                    table[i][1] = destArray[i];
                }
            }else{
                //se inserta la primera fila en la tabla
                table[1]=destArray;
           }
            name=row_name;
        }



    };
    public void writeFiles(int[][] table,String row_name,String column_name){

        String name=row_name;

        for(int j=0;j<2;j++) {

            try {
                FileOutputStream fos = new FileOutputStream(filesDir + "\\" + name);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(table[N + 1]);
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(j==0){
                //traspasamos los elementos de la ultima columna a la ultima fila para poder leerlos como array.
                for(int i=0;i<N+1;i++){
                    table[N+1][i]=table[i][N+1];
                }
                name=column_name;
            }

        }
    };





    public static void main(String[] args) {
        int [] arr={1,2,3};
        int [][] mat ={{4,5,6},{7,8,9},{10,11,12}};
        int [][] arr2= new int[2][3];
        System.out.println(Arrays.toString(arr));

        mat[1]=arr;

        System.out.println(Arrays.toString(mat[1]));

        mat[1][1]=200;

        System.out.println(Arrays.toString(arr));


    }


}
