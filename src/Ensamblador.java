
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
public class Ensamblador extends Thread {


    int dormir;

    Almacen almacen;
    boolean consumir = true;

    Semaphore semProductorBotones;
    Semaphore mutexBotones;
    Semaphore semEnsambladorBotones;

    Semaphore semProductor;
    Semaphore mutex;
    Semaphore semEnsamblador;

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
    int piezas;

    public Ensamblador(Almacen almacen, Semaphore semProductorBotones, Semaphore mutexBotones, Semaphore semEnsambladorBotones,
            Semaphore semProductorPantalla, Semaphore mutexPantalla, Semaphore semEnsambladorPantalla, Semaphore semProductorPantallaTactil,
            Semaphore mutexPantallaTactil, Semaphore semEnsambladorPantallaTactil, Semaphore semProductorJoystick, Semaphore mutexJoystick,
            Semaphore semEnsambladorJoystick, Semaphore semProductorTarjeta, Semaphore mutexTarjeta, Semaphore semEnsambladorTarjeta, int dormir) {
        this.almacen = almacen;
        this.semProductorBotones = semProductorBotones;
        this.mutexBotones = mutexBotones;
        this.semEnsambladorBotones = semEnsambladorBotones;
        this.semProductorPantalla = semProductorPantalla;
        this.mutexPantalla = mutexPantalla;
        this.semEnsambladorPantalla = semEnsambladorPantalla;
        this.semProductorPantallaTactil = semProductorPantallaTactil;
        this.mutexPantallaTactil = mutexPantallaTactil;
        this.semEnsambladorPantallaTactil = semEnsambladorPantallaTactil;
        this.semProductorJoystick = semProductorJoystick;
        this.mutexJoystick = mutexJoystick;
        this.semEnsambladorJoystick = semEnsambladorJoystick;
        this.semProductorTarjeta = semProductorTarjeta;
        this.mutexTarjeta = mutexTarjeta;
        this.semEnsambladorTarjeta = semEnsambladorTarjeta;
        this.dormir = dormir;
    }

    public void run() {

        while (consumir) {
            try {

                semEnsambladorBotones.acquire(5);
                mutexBotones.acquire();
          
                semEnsambladorPantalla.acquire();
                mutexPantalla.acquire();
          
                semEnsambladorPantallaTactil.acquire();
                mutexPantallaTactil.acquire();
             

                semEnsambladorJoystick.acquire(2);
                mutexJoystick.acquire();
         
                semEnsambladorTarjeta.acquire();
                mutexTarjeta.acquire();
              

                if (almacen.botones >= 5 && almacen.joystick >= 2 && almacen.pantalla >= 1 && almacen.pantallaTactil >= 1 && almacen.tarjeta >= 1) {
                  
                    almacen.botones -= 5;
                    almacen.joystick -= 2;
                    almacen.pantallaTactil -= 1;
                    almacen.pantalla -= 1;
                    almacen.tarjeta -= 1;
                    almacen.Consola++;
                

                    semProductorTarjeta.release();
                    mutexTarjeta.release();

                    semProductorJoystick.release(2);
                    mutexJoystick.release();

                    semProductorPantallaTactil.release();
                    mutexPantallaTactil.release();

                    semProductorPantalla.release();
                    mutexPantalla.release();

                    semProductorBotones.release(5);
                    mutexBotones.release();
                    Thread.sleep(dormir);
                } else {
            

                    semEnsambladorJoystick.release(2);
                    semProductorJoystick.release(0);
                    mutexJoystick.release();

                    semEnsambladorPantallaTactil.release();
                    semProductorPantallaTactil.release();
                    mutexPantallaTactil.release();

                    semEnsambladorPantalla.release();
                    semProductorPantalla.release();
                    mutexPantalla.release();

                    semEnsambladorBotones.release(5);
                    semProductorBotones.release();
                    mutexBotones.release();

                    semEnsambladorTarjeta.release();
                    semProductorTarjeta.release();
                    mutexTarjeta.release();

                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Ensamblador.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }


}