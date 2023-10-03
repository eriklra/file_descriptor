/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package descriptor;

import java.util.ArrayList;


/**
 *
 * @author prayt
 */
public class Tabla {
    String Nombre;
    int N; //filas
    int M; //Columnas
    String[][] tabla;
    
    public Tabla (){
        this.Nombre = "";
        /*this.N = N;
        this.M = M;*/
       // tabla = new String[N][M];
    }

    Tabla(String b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setColumnas(ArrayList<String> titulos){
       // System.out.println("Valor de M="+N);
        for (int i = 0; i < M; i++){
            tabla[0][i] = titulos.get(i+1);
          //  System.out.println("i="+i);
            //System.out.println("titulos"+titulos.get(i+1));
            //System.out.println("["+tabla[0][i]+"]");
        }
        
      /*  for (int i = 0; i < M; i++){
                System.out.println("TITULOS:"+tabla[0][i]);
            
        }*/
        
         /*for (int i = 0; i < M; i++){
          System.out.println("["+tabla[0][i]+"]");
        }*/
        
    }
    
    public void setFilas(int i, ArrayList<String> tupla){
        for (int j = 0; j < tupla.size(); j++){
            tabla[i + 1][j] = tupla.get(j);
        }
    }
    
    public void cantCol(int columnas){
        M=columnas;
      //  System.out.println("M : "+M);
    }
    
      public void cantFil(int filas){
        N=filas;
       // System.out.println("N : "+N);
    }
      
    public void dimensionTabla(){
        tabla=new String [N][M];
        //System.out.println("TAM DE ARREGLO= "+N+"X"+M);
    }
      
    public void setNombre(String nombre){
        this.Nombre=nombre;
    }
    
    public void agregarCont(int indice,String contenido,int cont){
       //for (int i = 0; i < M; i++){
    /*  for (int i = 0; i < M; i++){
                System.out.println("TITULOS A LA HORA DE AGREGAR CONTENIDO:"+tabla[0][i]);
            
        }
                System.out.println("INDICE:"+indice+"Cont:"+(cont-1));*/
            
      
            tabla[cont-1][indice] = contenido;
        //}
    }
     public String muestraCont(){
        String f="";
        for (int i = 0; i < N; i++){
            for(int j=0;j<M;j++){
                //System.out.print("["+tabla[i][j]+"]");
                f=f+"["+tabla[i][j]+"]";
            }
            //System.out.println();
            f=f+"\n";
        }
        return f;
    }
     
      public void muestraTitulos(){
        for (int i = 0; i < M; i++){
                System.out.println("TITULOS    :"+tabla[0][i]);
            
        }
    }
    /*public void setColumnas(int n){
        columnas=new ArrayList[n];
        for(int i=0;i<n;i++){
            columnas[i]=new ArrayList<>();
        }
        cantColumnas=n;
    }*/
        
}
