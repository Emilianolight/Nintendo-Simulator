
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
public class Jefe extends Thread {

    Semaphore mutexJefe;
    String actividad;
    volatile int contador;
    int tiempo;

    public Jefe(int contador, int tiempo, Semaphore mutexJefe) {

        this.contador = contador;
        this.tiempo = tiempo;
        this.mutexJefe = mutexJefe;
    }

    public void run() {

        while (true) {

            try {
                mutexJefe.acquire();
                actividad = "actualizar Contador";
                Thread.sleep(this.tiempo);
                Thread.sleep(seisHoras(this.tiempo));

                this.contador--;
                mutexJefe.release();
                actividad = "Dormido";
                Thread.sleep(this.tiempo);

            } catch (InterruptedException ex) {
                Logger.getLogger(Jefe.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public int seisHoras(int dia) {
        dia = (6 * dia) / 24;
        return dia;

    }
}
