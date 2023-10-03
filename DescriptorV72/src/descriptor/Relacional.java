/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package descriptor;

import java.util.LinkedList;
import java.util.StringTokenizer;
/**
 *
 * @author prayt
 */
public class Relacional {
    Query CONSULTA;
    LinkedList<Tabla> tablas = new LinkedList<>();
    //Tabla TablaDISTINCT = new Tabla();
    Tabla ProductoCartesiano = new Tabla();
    Tabla SELECCION = new Tabla();
    String resProd;
    String Sel;
    
    public Relacional(){

    }
    
    public void AddTabla(Tabla nueva){
        tablas.add(nueva);
    }
    
    public Tabla AsignaTabla(Query CONSULTA, int t){
        Tabla resultado;
        
        for (int i = 0; i < tablas.size(); i++){
            Tabla AUX;
             AUX = tablas.get(i);
          // System.out.println("NOMBRE TABLA:"+AUX.Nombre);
           
            if (this.CONSULTA.FROM.get(t).equals(AUX.Nombre)){
                resultado = tablas.get(i);
                return resultado;
            }
        }
        
        return null;
    }
    
    public Tabla Opera(Query consulta){
        this.CONSULTA = consulta;
        Tabla COPIA_ORIGINAL = AsignaTabla(CONSULTA,0);
        Tabla AUX;
        Tabla OpRelacional=new Tabla();
        
        if (CONSULTA.activos[0] != -1 && CONSULTA.activos[3] == -1 && CONSULTA.activos[1] == -1){
            if (CONSULTA.SELECT.get(0).equals("*")){
                OpRelacional = COPIA_ORIGINAL;
            }else{
                OpRelacional = seleccion(COPIA_ORIGINAL);
            }
        }
        
        if (CONSULTA.activos[1] != -1 && CONSULTA.activos[3] == -1){
            if (CONSULTA.SELECT.get(0).equals("*")){
                OpRelacional = distinctALL(COPIA_ORIGINAL);
            }else{
                OpRelacional = seleccionDistinct(COPIA_ORIGINAL);
            }
        }
        
        if (CONSULTA.activos[0] != -1 && CONSULTA.activos[1] == -1 && CONSULTA.activos[3] != -1){
            if (CONSULTA.SELECT.get(0).equals("*")){
                AUX = COPIA_ORIGINAL;
                OpRelacional = proyeccion(AUX);
            }else{
                AUX = seleccion(COPIA_ORIGINAL);
                OpRelacional = proyeccion(AUX);
            }
        }
        
        if (CONSULTA.activos[1] != -1 && CONSULTA.activos[3] != -1){
            if (CONSULTA.SELECT.get(0).equals("*")){
                AUX = distinctALL(COPIA_ORIGINAL);
                OpRelacional = proyeccion(AUX);
            }else{
                AUX = seleccionDistinct(COPIA_ORIGINAL);
                OpRelacional = proyeccion(AUX);
            }
        }
        
        if (CONSULTA.activos[0] != -1 && CONSULTA.activos[1] == -1 && CONSULTA.activos[2] > 1){
            OpRelacional = productoCartesiano();
        }
        return OpRelacional;
    }
    
    public Tabla productoCartesiano(){
        Tabla AUX  = new Tabla();
        Tabla uno = new Tabla();
        Tabla dos = new Tabla();
        
        for (int i = 0; i < tablas.size(); i++){
             AUX = tablas.get(i);
           // System.out.println("NOMBRE TABLA:"+AUX.Nombre);
           
            if (this.CONSULTA.FROM.get(0).equals(AUX.Nombre)){
                uno = tablas.get(i);
            }
            if (this.CONSULTA.FROM.get(1).equals(AUX.Nombre)){
                dos = tablas.get(i);
            }
        }
        
        ProductoCartesiano.setNombre("xCartesiano");
        ProductoCartesiano.cantFil((uno.N - 1) * (dos.N - 1) + 1);
        ProductoCartesiano.cantCol(uno.M + dos.M);
        ProductoCartesiano.dimensionTabla();
        
        for (int i = 0; i < uno.M; i++){
            ProductoCartesiano.tabla[0][i] = uno.tabla[0][i];
        }
        int y = 0;
        for (int i = uno.M; i < uno.M + dos.M; i++){
            ProductoCartesiano.tabla[0][i] = dos.tabla[0][y];
            y++;
        }
        //SELECT * FROM EMPLOYEES, DEPARTMENTS
        /*for (int i = 1; i < ProductoCartesiano.N; i++){
            for (int j = 0; j < ProductoCartesiano.M; j++){
                
            }
        }*/
        int a = 1;
        int b = 1;
        ProductoCartesiano.muestraCont();
        
        /*for (int i = 1; i < ProductoCartesiano.N; i++){
            if (a == dos.N - 1){
                a = 1;
                b++;
            }
            //System.out.println("B: " + b);
            ProductoCartesiano.tabla[i] = uno.tabla[i];
            a++;
            
        }*/
       // System.out.println("-------------------------------------------------");
        int CONTADOR = 1;
        int aux = 0;
        for(int i = 1; i < uno.N; i++){
            for(int x = CONTADOR; x < CONTADOR + dos.N - 1; x++){
                for(int j = 0; j < uno.M; j++){
                    ProductoCartesiano.tabla[x][j] = uno.tabla[i][j];
                }
                aux = x;
            }
            CONTADOR = aux + 1;
        }
        
        CONTADOR = 1;
        while (CONTADOR < ProductoCartesiano.N){
            for(int i = 1; i < dos.N; i++){
                for(int j = 0; j < dos.M; j++){
                    ProductoCartesiano.tabla[CONTADOR][j + uno.M] = dos.tabla[i][j];
                }
                CONTADOR++;
            }
        }
         
        
        //System.out.println("------------------------AQUI SE MUESTRA LA TABLA FINAL DEL PRODUCTO CARTESIANO----------------------");
        resProd=ProductoCartesiano.muestraCont();
        
        SELECCION = seleccionPC(uno, dos);
        
        SELECCION.muestraCont();
        
        AUX = proyeccionPC(uno, dos);
        
        
        return AUX;
    }
    
    public Tabla proyeccionPC(Tabla uno, Tabla dos){
        Tabla AUX = new Tabla();
        String[][] seleccionado = new String[CONSULTA.SELECT.size()][2];
        LinkedList<Integer> POS = new LinkedList<>();
        StringTokenizer particion;
        int x = 0;
        //SELECT EMPLOYEES.EMPLOYEE_ID,EMPLOYEES.FIRST_NAME,EMPLOYEES.DEPARTMENT_ID,DEPARTMENTS.DEPARTMENT_ID,DEPARTMENTS.LOCATION_ID FROM EMPLOYEES, DEPARTMENTS WHERE EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID
        for(int i = 0; i < CONSULTA.SELECT.size(); i++){
            x = 0;
            particion = new StringTokenizer(CONSULTA.SELECT.get(i), ".");
            while(particion.hasMoreElements()){
                seleccionado[i][x] = "" + particion.nextElement();
                x++;
            }
        }
        
        AUX.setNombre("PROYECCION");
        AUX.cantCol(CONSULTA.SELECT.size());
        AUX.cantFil(SELECCION.N);
        AUX.dimensionTabla();
        
        for(int i = 0; i < AUX.M; i++){
            AUX.tabla[0][i] = seleccionado[i][1];
        }
        
        for(int i = 0; i < CONSULTA.SELECT.size(); i++){
            if (seleccionado[i][0].equals(uno.Nombre)){
                for(int j = 0; j < uno.M; j++){
                    if(seleccionado[i][1].equals(SELECCION.tabla[0][j])){
                        POS.add(j);
                    }
                }
            }
            if (seleccionado[i][0].equals(dos.Nombre)){
                for(int j = uno.M; j < uno.M + dos.M; j++){
                    if(seleccionado[i][1].equals(SELECCION.tabla[0][j])){
                        POS.add(j);
                    }
                }
            }
        }
        
        for(int i = 1; i < AUX.N; i++){
            for(int j = 0; j < POS.size(); j++){
                AUX.tabla[i][j] = SELECCION.tabla[i][POS.get(j)];
            }
        }
        //AUX.muestraCont();
        return AUX;
    }
    
    public Tabla seleccionPC(Tabla uno, Tabla dos){
        Tabla AUX = new Tabla();
        LinkedList<Integer> POS = new LinkedList<>();
        int i = 0;
        int posUno = 0;
        int posDos = 0;
        StringTokenizer particion1 = new StringTokenizer(CONSULTA.WHERE.get(0), ".");
        StringTokenizer particion2= new StringTokenizer(CONSULTA.WHERE.get(2), ".");
        
        String[] condicion1 = new String[2];
        String[] condicion2 = new String[2];
        
        while(particion1.hasMoreElements()){
            condicion1[i] = "" + particion1.nextElement();
            condicion2[i] = "" + particion2.nextElement();
            i++;
        }
        //SELECT * FROM EMPLOYEES, DEPARTMENTS WHERE EMPLOYEES.DEPARTMENT_ID = DEPARTMENTS.DEPARTMENT_ID
      //  System.out.println("CONDICION 1: " + condicion1[0] + " uno: " + uno.Nombre);
      //  System.out.println("CONDICION 2: " + condicion2[0] + " dos: " + dos.Nombre);
        
        if (condicion1[0].equals(uno.Nombre) && condicion2[0].equals(dos.Nombre)){
       //     System.out.println("CONDICION 2: " + condicion2[0] + " dos: " + dos.Nombre);
            for(i = 0; i < uno.M; i++){
                if(ProductoCartesiano.tabla[0][i].equals(condicion1[1])){
                    posUno = i;
                }
            }
            for(i = uno.M; i < uno.M + dos.M; i++){
                if(ProductoCartesiano.tabla[0][i].equals(condicion2[1])){
                    posDos = i;
                }
            }
            
            for(i = 1; i < ProductoCartesiano.N; i++){
                //System.out.println("A: " + ProductoCartesiano.tabla[i][posUno] + " B: " + ProductoCartesiano.tabla[i][posDos]);
                /*String a = new String(ProductoCartesiano.tabla[i][posUno]);
                String b = new String(ProductoCartesiano.tabla[i][posDos]);
                System.out.println(a == b);*/
                
                if (ProductoCartesiano.tabla[i][posUno].equals(ProductoCartesiano.tabla[i][posDos].replace(" ",""))){
                   // System.out.println("SE CUMPLE ---- " + "A: " + ProductoCartesiano.tabla[i][posUno] + " B: " + ProductoCartesiano.tabla[i][posDos]);
                    POS.add(i);
                }
            }
            
         //   System.out.println("POS UNO: " + posUno);
         //   System.out.println("POS DOS: " + posDos);
          //  System.out.println("POS TAMAÑO: " + POS.size());
            AUX.cantCol(ProductoCartesiano.M);
            AUX.cantFil(POS.size() + 1);
            AUX.dimensionTabla();
            
            AUX.tabla[0] = ProductoCartesiano.tabla[0];
            
            for(i = 1; i < POS.size() + 1; i++){
                AUX.tabla[i] = ProductoCartesiano.tabla[POS.get(i - 1)];
            }
            
            //AUX.muestraCont();
        }
        return AUX;
    }
    
    public Tabla proyeccion(Tabla EnProceso){
        Tabla AUX;
        int cont = 0;
        
        int x = 0;
        int y = 0;
        
        String titulo = CONSULTA.WHERE.get(0);
        String op = CONSULTA.WHERE.get(1);
        String caracteristica = CONSULTA.WHERE.get(2);
        
        for (int i = 1; i < EnProceso.N; i++){
            for (int j = 1; j < EnProceso.M; j++){
                if (titulo.equals(EnProceso.tabla[0][j]) && caracteristica.equals(EnProceso.tabla[i][j])){
                    cont++;
                }
            }
        }
        
        //AUX = new Tabla("RESULTADO", cont, EnProceso.M);
        AUX = new Tabla(); AUX.setNombre("RESULTADO"); AUX.cantFil(cont+1); AUX.cantCol(EnProceso.M); AUX.dimensionTabla();
        
        
        for (int i = 0; i < AUX.M; i++){
                AUX.tabla[0][i] = EnProceso.tabla[0][i];
        }
        
        /*for (int i = 1; i < AUX.N; i++){
            for (int j = 1; j < EnProceso.M; j++){
                if (titulo.equals(EnProceso.tabla[0][j]) && caracteristica.equals(EnProceso.tabla[i][j])){
                    for (int z = 0; z < EnProceso.M; z++){
                        System.out.println("INFO TABLA "+EnProceso.tabla[i][z]);
                        AUX.tabla[i][z] = EnProceso.tabla[j][z];
                    }
                }
            }
        }*/
        for (int i = 1; i < EnProceso.N; i++){
            for (int j = 1; j < EnProceso.M; j++){
                if (titulo.equals(EnProceso.tabla[0][j]) && caracteristica.equals(EnProceso.tabla[i][j])){
                    for (int z = 0; z < EnProceso.M; z++){
                      //  System.out.println("INFO TABLA "+EnProceso.tabla[i][z]);
                        AUX.tabla[x + 1][z] = EnProceso.tabla[i][z];
                    }
                    x++;
                }
            }
        }
        
        return AUX;
    }
    
    public Tabla seleccion(Tabla COPIA_ORIGINAL){
        //Tabla AUX = new Tabla("RESULTADO", COPIA_ORIGINAL.N, CONSULTA.SELECT.size());
        
        Tabla AUX = new Tabla(); 
        AUX.setNombre("RESULTADO"); 
        AUX.cantFil(COPIA_ORIGINAL.N); 
        AUX.cantCol(CONSULTA.SELECT.size()); 
        AUX.dimensionTabla();
        
            for (int i = 0; i < CONSULTA.SELECT.size(); i++){
                AUX.tabla[0][i] = CONSULTA.SELECT.get(i);
            }
            
            for (int i = 1; i < COPIA_ORIGINAL.N; i++){
                for (int j = 0; j < COPIA_ORIGINAL.M; j++){
                    for (int z = 0; z < CONSULTA.SELECT.size(); z++){
                        if (CONSULTA.SELECT.get(z).equals(COPIA_ORIGINAL.tabla[0][j])){
                            AUX.tabla[i][z] = COPIA_ORIGINAL.tabla[i][j];
                            
                        }
                    }
                }
            }
            
       return AUX;
    }
    
    public Tabla seleccionDistinct(Tabla COPIA_ORIGINAL){
        
        //Tabla AUX = new Tabla("RESULTADO", COPIA_ORIGINAL.N, CONSULTA.DISTINCT.size());
        //Tabla AUX_COPIA = new Tabla("RESULTADO", COPIA_ORIGINAL.N, CONSULTA.DISTINCT.size());
        LinkedList<String> DATA = new LinkedList<>();
        LinkedList<Integer> POS = new LinkedList<>();
        
        Tabla AUX = new Tabla(); 
        AUX.setNombre("RESULTADO"); 
        AUX.cantCol(CONSULTA.DISTINCT.size()); 
        
        
        Tabla AUX_COPIA = new Tabla(); 
        AUX_COPIA.setNombre("RESULTADO"); 
        AUX_COPIA.cantFil(COPIA_ORIGINAL.N); 
        AUX_COPIA.cantCol(CONSULTA.DISTINCT.size()); 
        AUX_COPIA.dimensionTabla();
        
        boolean esta;
        int contador = 0;
        
                for (int i = 0; i < CONSULTA.DISTINCT.size(); i++){
                    AUX_COPIA.tabla[0][i] = CONSULTA.DISTINCT.get(i);
                    //AUX.tabla[0][i] = CONSULTA.DISTINCT.get(i);
                }

                for (int i = 1; i < COPIA_ORIGINAL.N; i++){
                    for (int j = 0; j < COPIA_ORIGINAL.M; j++){
                        for (int z = 0; z < CONSULTA.DISTINCT.size(); z++){
                            if (CONSULTA.DISTINCT.get(z).equals(COPIA_ORIGINAL.tabla[0][j])){
                                AUX_COPIA.tabla[i][z] = COPIA_ORIGINAL.tabla[i][j];
                            }
                        }
                    }
                }
                
                AUX_COPIA.muestraCont();
                
                for(int i = 1; i < COPIA_ORIGINAL.N; i ++){
                    if (i == 1){
                        DATA.add(AUX_COPIA.tabla[i][0]);
                        POS.add(i);
                    }else{
                        esta = false;
                        for(int j = 0; j < DATA.size(); j++){
                            if(AUX_COPIA.tabla[i][0].equals(DATA.get(j))){
                                esta = true;
                            }
                        }
                        if (esta == false){
                            DATA.add(AUX_COPIA.tabla[i][0]);
                            POS.add(i);
                        }
                    }
                }
                
                AUX.cantFil(DATA.size() + 1); 
                AUX.dimensionTabla();
                AUX.tabla[0][0] = AUX_COPIA.tabla[0][0];
                for(int a = 0; a < DATA.size(); a++){
                   // System.out.println("DATA ELEMENTOS: " + DATA.get(a));
                    AUX.tabla[a + 1][0] = DATA.get(a);
                }
                
                SELECCION.setNombre("RESULTADO"); 
                SELECCION.cantFil(DATA.size() + 1); 
                SELECCION.cantCol(COPIA_ORIGINAL.M); 
                SELECCION.dimensionTabla();
                
                SELECCION.tabla[0] = COPIA_ORIGINAL.tabla[0];
                
                for(int i = 1; i < SELECCION.N; i++){
                    for(int j = 0; j < SELECCION.M; j++){
                        SELECCION.tabla[i][j] = COPIA_ORIGINAL.tabla[POS.get(i - 1)][j];
                    }
                }

                AUX.muestraCont();
        return AUX;
    }
    
    public Tabla distinctALL(Tabla COPIA_ORIGINAL){
        
        //Tabla AUX = new Tabla("RESULTADO", COPIA_ORIGINAL.N, COPIA_ORIGINAL.M);
        //Tabla AUX_COPIA = new Tabla("RESULTADO", COPIA_ORIGINAL.N, COPIA_ORIGINAL.M);
        
        Tabla AUX = new Tabla(); 
        AUX.setNombre("RESULTADO"); 
        AUX.cantFil(COPIA_ORIGINAL.N); 
        AUX.cantCol(COPIA_ORIGINAL.M); 
        AUX.dimensionTabla();
        
        Tabla AUX_COPIA = new Tabla(); 
        AUX_COPIA.setNombre("RESULTADO"); 
        AUX_COPIA.cantFil(COPIA_ORIGINAL.N); 
        AUX_COPIA.cantCol(COPIA_ORIGINAL.M); 
        AUX_COPIA.dimensionTabla();
        
        boolean diferente;
        
        for (int i = 0; i < COPIA_ORIGINAL.M; i++){
                    AUX_COPIA.tabla[0][i] = COPIA_ORIGINAL.tabla[0][i];
                    AUX.tabla[0][i] = COPIA_ORIGINAL.tabla[0][i];
                }
                
                for (int i = 1; i < COPIA_ORIGINAL.N; i++){
                    for (int j = 0; j < COPIA_ORIGINAL.M; j++){
                        AUX_COPIA.tabla[i][j] = COPIA_ORIGINAL.tabla[i][j];
                    }
                }
                
                for (int i = 1; i < AUX.N; i++){
                    diferente = false;
                    if (i == 1){
                        AUX.tabla[i] = AUX_COPIA.tabla[i];
                    }
                    for (int j = 1; j < AUX_COPIA.N; j++){
                        for (int z = 0; z < COPIA_ORIGINAL.M; z++){
                            if (!AUX_COPIA.tabla[i][z].equals(AUX_COPIA.tabla[j][z])){
                                diferente = true;
                            }
                        }
                        if (diferente == true){
                            for (int z = 0; z < COPIA_ORIGINAL.M; z++){
                                AUX.tabla[i][z] = AUX_COPIA.tabla[i][z];
                            }
                        }
                    }
                }  
    
                return AUX;
    }
}

