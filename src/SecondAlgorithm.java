import java.io.*;
import java.lang.Math;
import java.util.Arrays;

public class SecondAlgorithm {
    String X;
    String Y;
    int N;
    int M;
    int B;
    String filesDir="C:\\Users\\Agustín\\Desktop\\Material Universidad\\2019-2\\Diseño y Análisis de Algoritmos\\Tarea 1\\files";


    public SecondAlgorithm(int M, int B) {
        this.M = M;
        this.B = B;
    }

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

    public int calculateDistance(String X, String Y) throws IOException, ClassNotFoundException {
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

        //Elementos a almacenar en memoria principal
        int[][] table=new int[blocks*128+1][blocks*128+1];
        char[] partialX = new char[blocks*128];
        char[] partialY = new char[blocks*128];

        //cantidad de iteraciones (horizontal) (total es it al cuadrado)
        int it = (N/128)/blocks;

        //creamos los streams de lectura y escritura
        //Para las columnas
        FileOutputStream fos_c=new FileOutputStream(filesDir + "\\" + "frontier_column.bin");
        ObjectOutputStream oos_c=new ObjectOutputStream(fos_c);
        FileInputStream fis_c=new FileInputStream(filesDir + "\\" + "frontier_column.bin");
        ObjectInputStream ois_c=new ObjectInputStream(fis_c);

        //Para las filas
        FileOutputStream fos_r=new FileOutputStream(filesDir + "\\" + "frontier_row.bin");
        ObjectOutputStream oos_r=new ObjectOutputStream(fos_r);
        FileInputStream fis_r=new FileInputStream(filesDir + "\\" + "frontier_row.bin");
        ObjectInputStream ois_r=new ObjectInputStream(fis_r);

        //Para los input X e Y
        FileInputStream fisX = new FileInputStream(X);
        ObjectInputStream oisX = new ObjectInputStream(fisX);
        FileInputStream fisY = new FileInputStream(Y);
        ObjectInputStream oisY = new ObjectInputStream(fisY);



        //llenamos los archivos de primera columna y primera fila
        int k=0;
        //el arreglo auxiliar toma la posicion de la primera fila de la matriz
        int[] auxArr=table[1];
        for(int i=0;i<N/blocks;i++){
            for(int j=0;j<blocks*128+1;j++){
                auxArr[j]=k;
                if(j<blocks*128){
                    k++;
                }
            }
            oos_r.writeObject(auxArr);
            oos_c.writeObject(auxArr);
        }




        //



        for(int row=0;row<it;row++){
            for(int col=0;col<it;col++){

                //leemos las porciones de X e Y que corresponden
                partialX = (char[]) oisX.readObject();
                partialY = (char[]) oisY.readObject();

                //recuperamos la frontera columna
                //en caso de encontrarnos al inicio de una fila, leemos del archivo columna
                if(col==0){
                     auxArr = (int[]) ois_c.readObject();
                 //en caso contrario, copiamos la frontera columna de la tabla llenada en la iteracion anterior
                 }else{
                     for(int i=0; i<auxArr.length;i++){
                         auxArr[i]=table[i][auxArr.length-1];
                     }
                 }
                 for(int i=0;i<auxArr.length;i++){
                     table[i][0]=auxArr[i];
                 }

                 //ahora traspasamos la fila frontera, que en cualquier caso debemos leer de memoria externa
                auxArr = (int[]) ois_r.readObject();

                //ahora podemos rellenar la matriz con los nuevos valores
                fillTable(partialY,partialX,table);

                //luego escribimos la fila frontera resultante
                oos_r.writeObject(table[blocks*128]);



            }
        }



        return table[128*blocks][128*blocks];
    }






    public static void main(String[] args) {
        int [] arr={1,2,3};
        int [][] mat ={{4,5,6},{7,8,9},{10,11,12}};
        int [][] arr2= new int[2][3];

        try {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\Agustín\\Desktop\\Material Universidad\\2019-2\\Diseño y Análisis de Algoritmos\\Tarea 1\\files" + "\\" + "test.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arr);
            oos.close();
            fos.close();

        }catch (IOException e) {
            e.printStackTrace();
        }


        int res =
    }


}
