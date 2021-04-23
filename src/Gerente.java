
import javax.swing.JOptionPane;
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
public class Gerente extends Thread {

    String actividad;
    int dormir;
    Jefe jefe;
    Almacen almacen;
    Semaphore mutexJefe;

    int valor;

    public Gerente(int dormir, Jefe jefe, Semaphore mutexJefe, int valor, Almacen almacen) {

        this.dormir = dormir;
        this.jefe = jefe;
        this.mutexJefe = mutexJefe;

        this.valor = valor;

        this.almacen = almacen;

    }

    public void run() {

        while (true) {

            try {
                mutexJefe.acquire();

                actividad = "Verificando";

                if (jefe.contador == 0) {
                    int aux = almacen.Consola;
                    almacen.Consola = 0;
                    JOptionPane.showMessageDialog(null, "El 15SD XL es Desplegado con: " + aux + " Unidades");

                    jefe.contador = valor;
                    mutexJefe.release();

                } else {
                    actividad = "Verificando";
                    mutexJefe.release();
                    actividad = "Dormido";
                    Thread.sleep(dosHoras(dormir));

                }

            } catch (InterruptedException ex) {
                Logger.getLogger(Gerente.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public int dosHoras(int dia) {
        dia = (2 * dia) / 24;

        return dia;
    }
}
