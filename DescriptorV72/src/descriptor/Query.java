/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package descriptor;
import java.util.StringTokenizer;
import java.util.ArrayList;
/**
 * SELECT ID FROM CARS
 * @author prayt
 */
public final class Query {
    String consulta;
    String[] comandos = {"SELECT","DISTINCT" ,"FROM" ,"WHERE" };
    String[] palabras;
    int[] activos;
    ArrayList<String> SELECT;
    ArrayList<String> DISTINCT;
    ArrayList<String> FROM;
    ArrayList<String> WHERE;
    int N;
    StringTokenizer particion;
    
    public Query(String consulta){
        this.consulta = consulta;
        particion = new StringTokenizer(consulta, " ,;");
        N = elementos();
        palabras = new String[N];
        Particion();
        activos = new int[4];
        for (int i = 0; i < 4; i++){
            activos[i] = -1;
        }
        interprete();
        
    }
    
    public int elementos(){
        int n = 0;
        while(particion.hasMoreElements()){
            particion.nextElement();
            n++;
        }
       // System.out.println("N="+n);
        return n;
    }
    
    public void Particion(){
        int i = 0;
        particion = new StringTokenizer(consulta, " ,;");
        while(particion.hasMoreElements()){
            palabras[i] = "" + particion.nextElement();
            //System.out.println("Palabras["+i+"]="+palabras[i]+"valor de i="+i);
            i++;
        }
    }
    
    public void interprete(){
        for (int i = 0; i < N; i++){
            for (int j = 0; j < 4; j++){
                if (palabras[i].equals(comandos[j]) ){
                    activos[j] = i;
                   // System.out.println(comandos[j] +" activo: " + activos[j] + " = " + palabras[i]);
                }
            }
        }
        
        if (activos[1] != -1){
            DISTINCT = new ArrayList();
            SELECT = new ArrayList();
            for (int i = activos[1] + 1; i < activos[2]; i++){
                DISTINCT.add(palabras[i]);
                SELECT.add(palabras[i]);
            }
            
        }else{
            if(activos[0] != -1){
                SELECT = new ArrayList();
                for (int i = activos[0] + 1; i < activos[2]; i++){
                    SELECT.add(palabras[i]);
                }
            }
        }
        
        if (activos[2] != -1){
            int x = N;
          //  System.out.println("FROM POSICION: " + activos[2]);
           // System.out.println("X = " + x);
            // System.out.println("activos[3]: "+activos[3]);
            FROM = new ArrayList();
            if (activos[3] != -1){
                x = activos[3];
                WHERE = new ArrayList();
                for (int i = activos[3] + 1; i < N; i++){
                    WHERE.add(palabras[i]);
                }
            }
            
            for (int i = activos[2] + 1; i < x; i++){
              //  System.out.println("x dentro del activos"+x);
               // System.out.println("FROM POSICION: " + activos[2] + " PALABRA: " + palabras[i]);
                FROM.add(palabras[i]);
            }
        }
        
        
        
    }
    
    
}
