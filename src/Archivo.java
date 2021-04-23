
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Emiliano
 */
public class Archivo {

    int[] nintendo = new int[10];
    int dias;
    int contadorJefe;

    int maxBotones;
    int maxJoystick;
    int maxPantalla;
    int maxPantallaTactil;
    int maxTarjeta;

    int inicialBotones;
    int inicialJoystick;
    int inicialPantallas;
    int inicialTarjeta;

    int maxPBotones;
    int maxPJoystick;
    int maxPPantallas;
    int maxPTarjeta;

    int iniEnsambladores;

    int maxEnsambladores;

    boolean señal;

    public void Archivo() {

    }

    public void LeerDisco() {

        señal = true;
        String aux;
        int nlinea = 1;
        String line;
        String Nintendotxt = "";
        String path = "test\\Nintendo.txt";
        File file = new File(path);

        try {
            if (!file.exists()) {
                file.createNewFile();
                JOptionPane.showMessageDialog(null, "El Archivo no existe, acaba de ser creado. Ingrese el formato y vuelva a correr el programa");
            } else {

               
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {

                        Nintendotxt += line + "\n";

                    }
                }
                if (!"".equals(Nintendotxt)) {
                    String[] Nintendosplit = Nintendotxt.split("\n");
                    for (int i = 0; i < Nintendosplit.length; i++) {

                    }

                    for (int i = 0; i < Nintendosplit.length; i++) {

                        switch (i) {

                            case 0:
                                String[] ayuda = Nintendosplit[0].split(":");
                                try {
                                    ayuda = Nintendosplit[0].split(":");

                                    dias = Integer.parseInt(ayuda[1].replace(" ", ""));
                                    if(dias <=0){
                                        JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea uno-> TIEMPO EN SEGUNDOS: No puede ser 0 o un valor negativo. Resolver y reiniciar el programa");
                                        señal=false;
                                    }
                                    
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea uno-> TIEMPO EN SEGUNDOS: Cantidad en segundos. Resolver y reiniciar el programa");
                                    señal = false;
                                }
                                break;

                            case 1:
                                
                                   try {
                                ayuda = Nintendosplit[1].replace(" ", "").split(":");

                                contadorJefe = Integer.parseInt(ayuda[1]);
                                 if(contadorJefe <=0){
                                        JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea dos-> JEFE CONTADOR: No puede ser 0 o un valor negativo. Resolver y reiniciar el programa");
                                        señal=false;
                                 }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea dos-> JEFE CONTADOR: Cantidad de días. Resolver y reiniciar el programa");
                                señal = false;
                            }
    

                            break;
                            case 2:
                                 try {
                                ayuda = Nintendosplit[2].split(":");
                                ayuda = ayuda[1].replace(" ", "").split(",");
                               
                                if(Integer.parseInt(ayuda[0])<0 || Integer.parseInt(ayuda[1])<0 || Integer.parseInt(ayuda[2])<0 || Integer.parseInt(ayuda[3])<0 ){
                                    JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea tres-> CAPACIDADES MAXIMAS PARA TIPO DE ALMACEN: No pueden ser 0 o un valor negativo. Resolver y reiniciar el programa");
                                        señal=false;
                                }
                                maxBotones = Integer.parseInt(ayuda[0]);
                                maxPantalla = Integer.parseInt(ayuda[1]);
                                maxJoystick = Integer.parseInt(ayuda[2]);
                                maxTarjeta = Integer.parseInt(ayuda[3]);

                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea tres-> CAPACIDADES MAXIMAS PARA TIPO DE ALMACEN: B50,P20,PT25,J30,T15. Resolver y reiniciar el programa");
                                señal = false;
                            }

                            break;
                            case 3:
                                
                                
                                try {
                                ayuda = Nintendosplit[3].split(":");
                                ayuda = ayuda[1].replace(" ", "").split(",");
                                
                                
                                 if(Integer.parseInt(ayuda[0])<0 || Integer.parseInt(ayuda[1])<0 || Integer.parseInt(ayuda[2])<0 || Integer.parseInt(ayuda[3])<0  ){
                                    JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea cuatro-> CANTIDAD INICIAL DE PRODUCTORES DE CADA TIPO: No pueden ser 0 o un valor negativo. Resolver y reiniciar el programa");
                                        señal=false;
                                }

                                inicialBotones = Integer.parseInt(ayuda[0]);

                                inicialPantallas = Integer.parseInt(ayuda[1]);
                                inicialJoystick = Integer.parseInt(ayuda[2]);
                                inicialTarjeta = Integer.parseInt(ayuda[3]);

                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea cuatro-> CANTIDAD INICIAL DE PRODUCTORES DE CADA TIPO: B1,P2,J1,T2. Resolver y reiniciar el programa");
                                señal = false;
                            }
                            break;
                            case 4:
                                
                                try {
                                ayuda = Nintendosplit[4].split(":");
                                ayuda = ayuda[1].replace(" ", "").split(",");
                                
                                
                                
                                
                                 if(Integer.parseInt(ayuda[0])<inicialBotones || Integer.parseInt(ayuda[1])<inicialPantallas || Integer.parseInt(ayuda[2])<inicialJoystick || Integer.parseInt(ayuda[3])<inicialTarjeta  ){
                                    JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea cinco-> CANTIDAD MAXIMA DE PRODUCTORES: Las cantidades iniciales no pueden ser mayor que las cantidades máximas. Resolver y reiniciar el programa");
                                        señal=false;
                                }
                                 
                                  if(Integer.parseInt(ayuda[0])<=0 || Integer.parseInt(ayuda[1])<=0 || Integer.parseInt(ayuda[2])<=0 || Integer.parseInt(ayuda[3])<=0  ){
                                    JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea cinco-> CANTIDAD MAXIMA DE PRODUCTORES: No pueden ser 0 o un valor negativo. Resolver y reiniciar el programa");
                                        señal=false;
                                }

                                maxPBotones = Integer.parseInt(ayuda[0]);
                                maxPPantallas = Integer.parseInt(ayuda[1]);
                                maxPJoystick = Integer.parseInt(ayuda[2]);
                                maxPTarjeta = Integer.parseInt(ayuda[3]);

                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea cinco-> CANTIDAD MAXIMA DE PRODUCTORES: B4,P3,J3,T4. Resolver y reiniciar el programa");
                                señal = false;
                            }
                            break;
                            case 5:
                                    try {
                                ayuda = Nintendosplit[5].replace(" ", "").split(":");
                                
                                if(Integer.parseInt(ayuda[1])<0){
                                   JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea seis-> CANTIDAD  INICIAL DE ENSAMBLADORES: No pueden ser un valor negativo. Resolver y reiniciar el programa");
                                señal = false; 
                                }
                                
                                iniEnsambladores = Integer.parseInt(ayuda[1]);
                                    
                              
                                
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea seis-> CANTIDAD INICIAL DE ENSAMBLADORES: 1. Resolver y reiniciar el programa");
                                señal = false;
                            }
                            break;

                            case 6:
                                try {
                                ayuda = Nintendosplit[6].replace(" ", "").split(":");
                                    System.out.println(ayuda[1]);   
                                    
                                      if(Integer.parseInt(ayuda[1])<iniEnsambladores ){
                                    JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea siete-> CANTIDAD MAXIMA DE ENSAMBLADORES: Las cantidades iniciales no pueden ser mayor que las cantidades máximas. Resolver y reiniciar el programa");
                                        señal=false;
                                }
                                    
                                if(Integer.parseInt(ayuda[1])<=0){
                                   JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea siete-> CANTIDAD MAXIMA DE ENSAMBLADORES: No pueden ser 0 un valor negativo. Resolver y reiniciar el programa");
                                señal = false; 
                                }
                                
                              
                                
                                
                                maxEnsambladores = Integer.parseInt(ayuda[1]);
                                

                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Por favor verifique la forma en la que está escrita la línea siete-> CANTIDAD MAXIMA DE ENSAMBLADORES: 5. Resolver y reiniciar el programa");

                                señal = false;
                            }
                            break;

                            default:
                                System.out.println("No entré al switchi");
                                break;

                        }

                    }

                    System.out.println("Sali del while");
                }

                br.close();
            }
        } catch (IOException ex) {
            System.out.println("error al leer el txt");
        }
    }

}
