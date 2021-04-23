
import javax.swing.JTextField;
import javax.swing.JLabel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//
/**
 *
 * @author Emiliano
 */
public class Actualizador extends Thread {

    Jefe jefe;
    Gerente gerente;
    Almacen almacen;
    Ventana ventana;
    JTextField textBotones;
    JTextField textPantallas;
    JTextField textJoysticks;
    JTextField textTarjetas;
    JTextField textPantallasTactil;
    JTextField textPBotones;
    JTextField textPPantallas;
    JTextField textPJoystick;
    JTextField textPTarjetas;
    JTextField textJefe;
    JTextField textGerente;
    JTextField textEnsambladores;
    JTextField textDias;
    JTextField textConsolas;

    public Actualizador(Almacen almacen, JTextField textBotones, JTextField textPantallas, JTextField textJoysticks, JTextField textTarjetas, JTextField textPantallasTactil, JTextField textJefe, JTextField textGerente,
            JTextField textDias, JTextField textConsolas, JTextField textPBotones, JTextField textPPantallas, JTextField textPTarjetas, JTextField textPJoystick, JTextField textEnsambladores, Jefe jefe, Gerente gerente) {
        this.almacen = almacen;
        this.textBotones = textBotones;
        this.textJoysticks = textJoysticks;
        this.textPantallas = textPantallas;
        this.textTarjetas = textTarjetas;
        this.textPantallasTactil = textPantallasTactil;
        this.textJefe = textJefe;
        this.textGerente = textGerente;
        this.textDias = textDias;
        this.textConsolas = textConsolas;
        this.textPBotones = textPBotones;
        this.textPPantallas = textPPantallas;
        this.textPTarjetas = textPTarjetas;
        this.textPJoystick = textPJoystick;
        this.textEnsambladores = textEnsambladores;
        this.jefe = jefe;
        this.gerente = gerente;
    }

    public void run() {

        while (true) {

            textBotones.setText(Integer.toString(almacen.botones));
            textJoysticks.setText(Integer.toString(almacen.joystick));
            textPantallas.setText(Integer.toString(almacen.pantalla));
            textPantallasTactil.setText(Integer.toString(almacen.pantallaTactil));
            textTarjetas.setText(Integer.toString(almacen.tarjeta));
            textConsolas.setText(Integer.toString(almacen.Consola));

            try {
                textPBotones.setText(Integer.toString(almacen.pBotones));
                textPPantallas.setText(Integer.toString(almacen.pPantalla));
                textPJoystick.setText(Integer.toString(almacen.pJoystick));
                textPTarjetas.setText(Integer.toString(almacen.pTarjeta));
                textEnsambladores.setText(Integer.toString(almacen.pEnsamblador));
                textJefe.setText(jefe.actividad);
                textDias.setText(Integer.toString(jefe.contador));
                textGerente.setText(gerente.actividad);
            } catch (Exception e) {
                System.out.println(e);
            }

        }

    }

}
