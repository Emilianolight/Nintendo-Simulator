
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Emiliano
 */
public class Productor extends Thread {

    Ventana ventana;

    int dormir;
    int cantidadMax;
    String tipo;
    Almacen almacen;
    boolean seguir = true;

    Semaphore semProductor;
    Semaphore mutex;
    Semaphore semEnsamblador;

    Semaphore semProductorBotones;
    Semaphore mutexBotones;
    Semaphore semEnsambladorBotones;

    Semaphore semProductorPantalla;
    Semaphore mutexPantalla;
    Semaphore semEnsambladorPantalla;

    Semaphore semProductorPantallaTactil;
    Semaphore mutexPantallaTactil;
    Semaphore semEnsambladorPantallaTactil;

    Semaphore semProductorJoystick;
    Semaphore mutexJoystick;
    Semaphore semEnsambladorJoystick;

    Semaphore semProductorTarjeta;
    Semaphore mutexTarjeta;
    Semaphore semEnsambladorTarjeta;

    // Botones
    public Productor(int dormir, int cantidadMax, Almacen almacen, Semaphore semProductorBotones, Semaphore mutexBotones, Semaphore semEnsambladorBotones, String tipo) {

        this.dormir = dormir;
        this.cantidadMax = cantidadMax;
        this.almacen = almacen;
        this.semProductorBotones = semProductorBotones;
        this.mutexBotones = mutexBotones;
        this.semEnsambladorBotones = semEnsambladorBotones;
        this.tipo = tipo;

    }

    //Pantalla
    public Productor(int dormir, Almacen almacen, int cantidadMax, Semaphore semProductorPantalla, Semaphore mutexPantalla, Semaphore semEnsambladorPantalla, Semaphore semProductorPantallaTactil, Semaphore mutexPantallaTactil, Semaphore semEnsambladorPantallaTactil, String tipo) {

        this.dormir = dormir;
        this.cantidadMax = cantidadMax;
        this.almacen = almacen;
        this.semProductorPantalla = semProductorPantalla;
        this.mutexPantalla = mutexPantalla;
        this.semEnsambladorPantalla = semEnsambladorPantalla;
        this.semProductorPantallaTactil = semProductorPantallaTactil;
        this.mutexPantallaTactil = mutexPantallaTactil;
        this.semEnsambladorPantallaTactil = semEnsambladorPantallaTactil;
        this.tipo = tipo;
    }

    //Joystick
    public Productor(Almacen almacen, int dormir, int cantidadMax, Semaphore semProductorJoystick, Semaphore mutexJoystick, Semaphore semEnsambladorJoystick, String tipo) {

        this.dormir = dormir;
        this.cantidadMax = cantidadMax;
        this.almacen = almacen;
        this.semProductorJoystick = semProductorJoystick;
        this.mutexJoystick = mutexJoystick;
        this.semEnsambladorJoystick = semEnsambladorJoystick;
        this.tipo = tipo;
    }

    //Tarjeta
    public Productor(int dormir, int cantidadMax, Semaphore semProductorTarjeta, Semaphore mutexTarjeta, Semaphore semEnsambladorTarjeta, String tipo, Almacen almacen) {

        this.dormir = dormir;
        this.cantidadMax = cantidadMax;
        this.almacen = almacen;
        this.semProductorTarjeta = semProductorTarjeta;
        this.mutexTarjeta = mutexTarjeta;
        this.semEnsambladorTarjeta = semEnsambladorTarjeta;
        this.tipo = tipo;
    }

    public void run() {

        switch (tipo) {

            case "boton":

                while (seguir) {

                    try {

                        semProductorBotones.acquire(1);
                        mutexBotones.acquire();

                        almacen.botones += 1;
                        semEnsambladorBotones.release(1);
                        mutexBotones.release();

                        Thread.sleep(this.dormir / 2);
                        semProductorBotones.acquire(1);
                        mutexBotones.acquire();
                        almacen.botones += 1;
                 
                        semEnsambladorBotones.release(1);
                        mutexBotones.release();
                        Thread.sleep(this.dormir / 2);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            case "pantalla":
                while (seguir) {

                    try {
                     
                        semProductorPantalla.acquire();

                        mutexPantalla.acquire();

                        almacen.pantalla++;
              

                        semEnsambladorPantalla.release();
                        mutexPantalla.release();
                        Thread.sleep(this.dormir);

                        semProductorPantallaTactil.acquire();

                        mutexPantallaTactil.acquire();

                        almacen.pantallaTactil++;
                
                        semEnsambladorPantallaTactil.release();
                        mutexPantallaTactil.release();
                        Thread.sleep(this.dormir * 2);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            case "joystick":
                while (seguir) {

                    try {
                        semProductorJoystick.acquire();
                        mutexJoystick.acquire();

                        almacen.joystick++;
                    

                        semEnsambladorJoystick.release();
                        mutexJoystick.release();
                        Thread.sleep(this.dormir);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            case "tarjeta":
                while (seguir) {

                    try {

                        semProductorTarjeta.acquire();

                        mutexTarjeta.acquire();
                      
                        almacen.tarjeta++;
                 
                        semEnsambladorTarjeta.release();
                        mutexTarjeta.release();
                        Thread.sleep(this.dormir * 3);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            default:
                System.out.println("No entre  switchi chaval");
                break;

        }

    }

}
