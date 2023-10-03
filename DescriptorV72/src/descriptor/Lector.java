/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package descriptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
/**
 *
 * @author prayt
 */
public class Lector {
    static List <Tabla> tablas=new ArrayList<>();
    static int contador;
    static String aux="";
    static ArrayList<String> atributos = new ArrayList<>();
    static ArrayList<Integer> posiciones=new ArrayList<>();
    static FileReader fr;
    static BufferedReader bw;
    
    public void cargarTablas(){
        String [] tablasName={"countries.txt","departments.txt","jobs.txt","locations.txt","employees2.txt","regions.txt","jobs_history.txt","pacientes.txt"};
        for(int i=0;i<tablasName.length;i++){
            Tabla tabla=new Tabla();
            File f = new File(tablasName[i]);
            leerArchivo(f,tabla);
            tablas.add(tabla);
            
        }
        
       // System.out.println("TABLAS LEIDAS DEL ARRAY LIST\n\n\n\n\n");
        for(int i=0;i<tablasName.length;i++){
            tablas.get(i).muestraCont();
        }
       /* Tabla tabla=new Tabla();
        File f = new File("employees2.txt");
            leerArchivo(f,tabla);
            tablas.add(tabla);*/
    }

    public static void leerArchivo(File archivo, Tabla tabla){
      //  int count=0;
                try {
    fr=new FileReader(archivo);
    bw=new BufferedReader(fr);
    Object[] linea=bw.lines().toArray();
    tabla.cantFil(linea.length);
 //   System.out.println("Cant filas: "+linea.length);
            Scanner myReader = new Scanner(archivo);
            contador=1;
            
            while (myReader.hasNextLine()) {
              //  System.out.println("CONTADOR="+contador);
                String data = myReader.nextLine();
         //       System.out.println("Data: "+data);
                if(contador==1){
                    boolean check=false;
                    int i=0;
                    int j=0;
                   // char a;
                    do{
                       // System.out.println("aux: "+aux);
                        if((data.charAt(i)!=',')&&(i!=(data.length()-1))){
                            aux=aux+data.charAt(i);
                        }else{
                            j+=1;
                            if(check==false){
                                tabla.setNombre(aux);
                                atributos.add(aux);
                                aux="";
                                check=true;
                                j=2;
                            }else{
                                if(j==3){
                                    atributos.add(aux);
                                    aux="";
                                    j=0;
                                }
                                else{
                                    if(i==data.length()-1){
                                       posiciones.add(Integer.valueOf(aux+data.charAt(i)));
                                        aux="";
                                    }
                                    else{
                                        //posiciones=Integer.valueOf(aux);
                                    posiciones.add(Integer.valueOf(aux));
                                    aux="";
                                    }

                                }
                            }
                        }
                        i+=1;
                    }while(i<data.length());

                    ///Declaración de la cantidad de columnas de la tabla
                    tabla.cantCol(atributos.size()-1);
                   // System.out.println("tam atri: "+atributos.size());
                    tabla.dimensionTabla();
                   
                   /* for(int k=1;k<atributos.size();k++){
                       
                    }*/
                    tabla.setColumnas(atributos);
                  //  System.out.println("SALE DE TITULOS");
                }
                if(contador>1){
                //    System.out.println("ENTRO A ELSE");
                    int j;
                    j=0;
                    for(int i=1;i<atributos.size();i++){
                       // int aFinal;
                        int aux1,aux2,K=0;
                        String contenido="";
                     //   System.out.println("contenido inicio= "+contenido);
                        aux1=posiciones.get(j)-1;
                        aux2=posiciones.get(j+1);
                        if(aux2>data.length()){
                               contenido=data.substring(aux1,data.length());
                     //   System.out.println("contenido despues1= "+contenido);
                        }else{
                           contenido=data.substring(aux1,aux2);
                        //    System.out.println("contenido despues2= "+contenido); 
                        }
                        
                         
             //   System.out.println("Contador:"+contador);
            
          
                        tabla.agregarCont(i-1, contenido,contador);
                        j+=2;
                    }
                }
                contador++;
            }
            atributos.clear();
            posiciones.clear();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No pude encontrar el archivo men.");
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            System.out.println("Elige un archivo plz.");
        }
                //tabla.muestraTitulos();
                //tabla.muestraCont();
                
}

    
    
}

