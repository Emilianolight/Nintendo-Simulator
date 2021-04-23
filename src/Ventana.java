
import java.util.concurrent.Semaphore;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Ventana extends javax.swing.JFrame {

    Productor productor;
    Productor[] productorBotones;
    Productor[] productorPantallas;
    Productor[] productorJoystick;
    Productor[] productorTarjeta;
    Ensamblador[] Ensambladores;

    Archivo archivo = new Archivo();

   

    volatile int cantidadMaxBoton;
    volatile int cantidadMaxPantalla;
    volatile int cantidadMaxJoystick;
    volatile int cantidadMaxTarjeta;
    volatile int dormir;
    volatile int dias;

    volatile int pBotones = 0;
    volatile int pJoystick = 0;
    volatile int pPantalla = 0;
    volatile int pTarjeta = 0;
    volatile int pEnsamblador = 0;
    volatile String respuesta;

    Almacen almacen = new Almacen();

    Semaphore mutexJefe = new Semaphore(1);
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


    public Ventana() {
        initComponents();
        textBotones.setEditable(false);
        textPantallas.setEditable(false);
        textPantallaTactil.setEditable(false);
        textTarjetas.setEditable(false);
        textConsolas.setEditable(false);
        textJoysticks.setEditable(false);
        textGerente.setEditable(false);
        textJefe.setEditable(false);
        textPBotones.setEditable(false);
        textPJoystick.setEditable(false);
        textPPantalla.setEditable(false);
        textPTarjeta.setEditable(false);
        textDias.setEditable(false);
        textEnsamblador.setEditable(false);

    }

    public void InicioConArchivo() {
    

        archivo.LeerDisco();
        if (archivo.señal) {
            iniciar.setEnabled(false);
            iniciar.setVisible(false);
            semProductorBotones = new Semaphore(archivo.maxBotones);
            mutexBotones = new Semaphore(1);
            semEnsambladorBotones = new Semaphore(0);

            semProductorPantalla = new Semaphore(archivo.maxPantalla / 2);
            mutexPantalla = new Semaphore(1);
            semEnsambladorPantalla = new Semaphore(0);

            semProductorPantallaTactil = new Semaphore(archivo.maxPantalla / 2);
            mutexPantallaTactil = new Semaphore(1);
            semEnsambladorPantallaTactil = new Semaphore(0);

            semProductorJoystick = new Semaphore(archivo.maxJoystick);
            mutexJoystick = new Semaphore(1);
            semEnsambladorJoystick = new Semaphore(0);

            semProductorTarjeta = new Semaphore(archivo.maxTarjeta);
            mutexTarjeta = new Semaphore(1);
            semEnsambladorTarjeta = new Semaphore(0);

            dias = archivo.contadorJefe;

            dormir = archivo.dias * 1000;
            productorBotones = new Productor[archivo.maxPBotones];
            productorPantallas = new Productor[archivo.maxPPantallas];
            productorJoystick = new Productor[archivo.maxPJoystick];
            productorTarjeta = new Productor[archivo.maxPTarjeta];
            Ensambladores = new Ensamblador[archivo.maxEnsambladores];
            
            Jefe jefe = new Jefe(dias, dormir, mutexJefe);

            Gerente gerente = new Gerente(dormir, jefe, mutexJefe, jefe.contador, almacen);

            for (int i = 0; i < archivo.inicialBotones; i++) {
                productorBotones[i] = new Productor(dormir, archivo.maxBotones, almacen, semProductorBotones, mutexBotones, semEnsambladorBotones, "boton");
                productorBotones[i].start();

            }
            almacen.pBotones = archivo.inicialBotones;
            

            for (int i = 0; i < archivo.inicialPantallas; i++) {
                productorPantallas[i] = new Productor(dormir, almacen, archivo.maxPantalla / 2, semProductorPantalla, mutexPantalla, semEnsambladorPantalla, semProductorPantallaTactil, mutexPantallaTactil, semEnsambladorPantallaTactil, "pantalla");
                productorPantallas[i].start();

            }
            almacen.pPantalla = archivo.inicialPantallas;
            for (int i = 0; i < archivo.inicialJoystick; i++) {
                productorJoystick[i] = new Productor(almacen, dormir, archivo.maxJoystick, semProductorJoystick, mutexJoystick, semEnsambladorJoystick, "joystick");
                productorJoystick[i].start();

            }
            almacen.pJoystick = archivo.inicialJoystick;
            for (int i = 0; i < archivo.inicialTarjeta; i++) {
                productorTarjeta[i] = new Productor(dormir, archivo.maxPTarjeta, semProductorTarjeta, mutexTarjeta, semEnsambladorTarjeta, "tarjeta", almacen);
                productorTarjeta[i].start();

            }
            almacen.pTarjeta = archivo.inicialTarjeta;

            for (int i = 0; i < archivo.iniEnsambladores; i++) {
                Ensambladores[i] = new Ensamblador(almacen, semProductorBotones, mutexBotones, semEnsambladorBotones,
                        semProductorPantalla, mutexPantalla, semEnsambladorPantalla, semProductorPantallaTactil,
                        mutexPantallaTactil, semEnsambladorPantallaTactil, semProductorJoystick, mutexJoystick,
                        semEnsambladorJoystick, semProductorTarjeta, mutexTarjeta, semEnsambladorTarjeta, dormir);
                Ensambladores[i].start();
            }
            almacen.pEnsamblador = archivo.iniEnsambladores;

            jefe.start();
            gerente.start();

            Actualizador act = new Actualizador(almacen, textBotones, textPantallas, textJoysticks, textTarjetas, textPantallaTactil, textJefe, textGerente,
                    textDias, textConsolas, textPBotones, textPPantalla, textPTarjeta, textPJoystick, textEnsamblador, jefe, gerente);

            act.start();
        }

    }

    public void InicioPorDefecto() {
        iniciar.setEnabled(false);
        iniciar.setVisible(false);
  

        semProductorBotones = new Semaphore(45);
        mutexBotones = new Semaphore(1);
        semEnsambladorBotones = new Semaphore(0);

        semProductorPantalla = new Semaphore(40 / 2);
        mutexPantalla = new Semaphore(1);
        semEnsambladorPantalla = new Semaphore(0);

        semProductorPantallaTactil = new Semaphore(40 / 2);
        mutexPantallaTactil = new Semaphore(1);
        semEnsambladorPantallaTactil = new Semaphore(0);

        semProductorJoystick = new Semaphore(20);
        mutexJoystick = new Semaphore(1);
        semEnsambladorJoystick = new Semaphore(0);

        semProductorTarjeta = new Semaphore(15);
        mutexTarjeta = new Semaphore(1);
        semEnsambladorTarjeta = new Semaphore(0);

        dias = 20;

        dormir = 1000;
        productorBotones = new Productor[3];
        productorPantallas = new Productor[5];
        productorJoystick = new Productor[4];
        productorTarjeta = new Productor[4];
        Ensambladores = new Ensamblador[5];
        Jefe jefe = new Jefe(dias, 1000, mutexJefe);

        Gerente gerente = new Gerente(dormir, jefe, mutexJefe, jefe.contador, almacen);

        productorBotones[0] = new Productor(dormir, 45, almacen, semProductorBotones, mutexBotones, semEnsambladorBotones, "boton");

        cantidadMaxBoton = 45;
        productorPantallas[0] = new Productor(dormir, almacen, 40 / 2, semProductorPantalla, mutexPantalla, semEnsambladorPantalla, semProductorPantallaTactil, mutexPantallaTactil, semEnsambladorPantallaTactil, "pantalla");
        pPantalla++;
        cantidadMaxPantalla = 40 / 2;

        productorJoystick[0] = new Productor(almacen, dormir, 20, semProductorJoystick, mutexJoystick, semEnsambladorJoystick, "joystick");
        pJoystick++;
        cantidadMaxJoystick = 20;
        productorTarjeta[0] = new Productor(dormir, 15, semProductorTarjeta, mutexTarjeta, semEnsambladorTarjeta, "tarjeta", almacen);
        pTarjeta++;
        cantidadMaxTarjeta = 15;
        Ensambladores[0] = new Ensamblador(almacen, semProductorBotones, mutexBotones, semEnsambladorBotones,
                semProductorPantalla, mutexPantalla, semEnsambladorPantalla, semProductorPantallaTactil,
                mutexPantallaTactil, semEnsambladorPantallaTactil, semProductorJoystick, mutexJoystick,
                semEnsambladorJoystick, semProductorTarjeta, mutexTarjeta, semEnsambladorTarjeta, dormir);

        pEnsamblador++;

        productorBotones[0].start();
        almacen.pBotones = 1;

        productorPantallas[0].start();
        almacen.pPantalla = 1;
        productorJoystick[0].start();
        almacen.pJoystick = 1;
        productorTarjeta[0].start();
        almacen.pTarjeta = 1;
        Ensambladores[0].start();
        almacen.pEnsamblador = 1;

        jefe.start();
        gerente.start();

        Actualizador act = new Actualizador(almacen, textBotones, textPantallas, textJoysticks, textTarjetas, textPantallaTactil, textJefe, textGerente,
                textDias, textConsolas, textPBotones, textPPantalla, textPTarjeta, textPJoystick, textEnsamblador, jefe, gerente);
        System.out.println("El objeto vale");

        act.start();

    }

    public void crearProductor(Productor[] productores, String tipo, int dormir, int cantidadMax, Almacen almacen) {

        boolean creado = true;
        int i = 0;
        switch (tipo) {

            case "boton":
                while (creado) {

                    if (productores[i] == null) {
                        productores[i] = new Productor(dormir, cantidadMax, almacen, semProductorBotones, mutexBotones, semEnsambladorBotones, "boton");
                        productores[i].start();
                        almacen.pBotones++;
                      
                        creado = false;
                    } else {
                        i++;
                        if (i >= productores.length) {
                            creado = false;
                         
                            almacen.pBotones = productores.length;

                        }

                    }

                }

                break;
            case "pantalla":

                while (creado) {

                    if (productores[i] == null) {
                        productores[i] = new Productor(dormir, almacen, cantidadMax, semProductorPantalla, mutexPantalla, semEnsambladorPantalla, semProductorPantallaTactil, mutexPantallaTactil, semEnsambladorPantallaTactil, "pantalla");
                        productores[i].start();
                        almacen.pPantalla++;
                        creado = false;
                     

                    } else {
                        i++;
                        if (i >= productores.length) {
                            creado = false;
                           
                            almacen.pPantalla = productores.length;

                        }

                    }

                }

                break;
            case "joystick":

                while (creado) {

                    if (productores[i] == null) {
                        productores[i] = new Productor(almacen, dormir, cantidadMax, semProductorJoystick, mutexJoystick, semEnsambladorJoystick, "joystick");
                        productores[i].start();
                        almacen.pJoystick++;
                        creado = false;
                     

                    } else {
                        i++;
                        if (i >= productores.length) {
                            creado = false;
                           
                            almacen.pJoystick = productores.length;
                        }

                    }

                }

                break;
            case "tarjeta":

                while (creado) {

                    if (productores[i] == null) {

                        productores[i] = new Productor(dormir, cantidadMax, semProductorTarjeta, mutexTarjeta, semEnsambladorTarjeta, "tarjeta", almacen);
                        productores[i].start();
                        almacen.pTarjeta++;
                        creado = false;
                       

                    } else {
                        i++;
                        if (i >= productores.length) {
                            creado = false;
                           
                            almacen.pTarjeta = productores.length;
                        }

                    }

                }

                break;

            default:
                System.out.println("No entre al switchi del creador");

                break;

        }

    }

    public void eliminarProductor(Productor[] productores, String tipo) {
        int i = productores.length - 1;
        boolean eliminado = true;
        while (eliminado) {

            if (productores[i] != null) {
                productores[i].seguir = false;
                productores[i] = null;

              
                eliminado = false;
                switch (tipo) {

                    case "Boton":
                        almacen.pBotones--;
                        break;
                    case "Joystick":
                        almacen.pJoystick--;
                        break;
                    case "Pantalla":
                        almacen.pPantalla--;
                        break;
                    case "Tarjeta":
                        almacen.pTarjeta--;
                        break;
                    default:
                        System.out.println("No entre al switchi de las variables elimiandas");
                        break;

                }
            } else {
                i--;
                if (i < 0) {
               
                    eliminado = false;
                    switch (tipo) {

                        case "Botone":
                            almacen.pBotones = 0;
                            break;
                        case "Joystick":
                            almacen.pJoystick = 0;
                            break;
                        case "Pantalla":
                            almacen.pPantalla = 0;
                            break;
                        case "Tarjeta":
                            almacen.pTarjeta = 0;
                            break;
                        default:
                            System.out.println("No entre al switchi de las variables elimiandas");
                            break;
                    }
                }

            }
        }
    }

    public void crearEnsamblador(Ensamblador[] ensambladores) {

        boolean creado = true;
        int i = 0;

        while (creado) {

            if (ensambladores[i] == null) {

                ensambladores[i] = new Ensamblador(almacen, semProductorBotones, mutexBotones, semEnsambladorBotones,
                        semProductorPantalla, mutexPantalla, semEnsambladorPantalla, semProductorPantallaTactil,
                        mutexPantallaTactil, semEnsambladorPantallaTactil, semProductorJoystick, mutexJoystick,
                        semEnsambladorJoystick, semProductorTarjeta, mutexTarjeta, semEnsambladorTarjeta, dormir);
                ensambladores[i].start();
                almacen.pEnsamblador++;
            
                creado = false;
            } else {
                i++;
                if (i >= ensambladores.length) {
                    creado = false;
                    almacen.pEnsamblador = ensambladores.length;
                   

                }

            }

        }

    }

    public void eliminarEnsamblador(Ensamblador[] ensamblador) {
        int i = ensamblador.length - 1;
        boolean eliminado = true;
        while (eliminado) {

            if (ensamblador[i] != null) {
                ensamblador[i].consumir = false;
                ensamblador[i] = null;
                almacen.pEnsamblador--;
                System.out.println("Se ha eliminado un ensamblador");
                eliminado = false;
            } else {
                i--;
                if (i < 0) {
                    almacen.pEnsamblador = 0;
                    System.out.println("Todos los ensambladores  fueron eliminados");
                    eliminado = false;
                }
            }

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        iniciar = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        textBotones = new javax.swing.JTextField();
        textPantallas = new javax.swing.JTextField();
        textPantallaTactil = new javax.swing.JTextField();
        textJoysticks = new javax.swing.JTextField();
        textTarjetas = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        textConsolas = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        textGerente = new javax.swing.JTextField();
        textJefe = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        textPBotones = new javax.swing.JTextField();
        textPPantalla = new javax.swing.JTextField();
        textPJoystick = new javax.swing.JTextField();
        textPTarjeta = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        textDias = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        textEnsamblador = new javax.swing.JTextField();
        box = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Nintendo");

        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Quitar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText("Productores");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("Ensambladores");

        iniciar.setText("Iniciar");
        iniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciarActionPerformed(evt);
            }
        });

        jButton7.setText("Salir");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel4.setText("Botones:");

        jLabel5.setText("Pantalla:");

        jLabel6.setText("Pantalla táctil:");

        jLabel7.setText("Joysticks:");

        jLabel8.setText("Tarjetas SD:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel9.setText("Productos en almacén:");

        textBotones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textBotonesActionPerformed(evt);
            }
        });

        textPantallas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPantallasActionPerformed(evt);
            }
        });

        textTarjetas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textTarjetasActionPerformed(evt);
            }
        });

        jLabel10.setText("Consolas:");

        textConsolas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textConsolasActionPerformed(evt);
            }
        });

        jLabel11.setText("Jefe:");

        jLabel12.setText("Gerente:");

        textJefe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textJefeActionPerformed(evt);
            }
        });

        jLabel13.setText("Botones:");

        jLabel14.setText("Pantalla:");

        jLabel15.setText("Joystick:");

        jLabel16.setText("Tarjetas SD:");

        textPBotones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPBotonesActionPerformed(evt);
            }
        });

        textPPantalla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPPantallaActionPerformed(evt);
            }
        });

        jLabel17.setText("Días para el despliegue:");

        textDias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textDiasActionPerformed(evt);
            }
        });

        jLabel18.setText("Ensambladores:");

        textEnsamblador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textEnsambladorActionPerformed(evt);
            }
        });

        box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "Botones", "Pantallas", "Joysticks", "Tarjetas" }));
        box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxActionPerformed(evt);
            }
        });

        jButton4.setText("Quitar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton8.setText("Agregar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel15)
                                            .addComponent(jLabel14)))
                                    .addComponent(jLabel16))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(textPJoystick, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                            .addComponent(textPTarjeta))
                                        .addGap(61, 61, 61)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(box, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(textPPantalla)
                                        .addGap(180, 180, 180))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel13)
                                .addGap(17, 17, 17)
                                .addComponent(textPBotones, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel10)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5)
                                                .addComponent(jLabel4))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addGap(1, 1, 1))
                                            .addComponent(jLabel8))
                                        .addGap(39, 39, 39)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(textPantallas, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(textBotones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(textPantallaTactil, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(textJoysticks, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(textTarjetas, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(textConsolas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(169, 169, 169)
                                        .addComponent(iniciar)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textGerente, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(129, 129, 129))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton2)
                                    .addComponent(jLabel18))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addComponent(textEnsamblador, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton1)))
                                .addGap(79, 79, 79))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addContainerGap())
                            .addComponent(textDias, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(7, 7, 7)
                                .addComponent(textJefe, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(692, 692, 692)
                        .addComponent(jButton7)
                        .addGap(0, 15, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(296, 296, 296))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(textEnsamblador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addGap(85, 85, 85))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(textBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(textPantallas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(textJefe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(textPantallaTactil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)
                                    .addComponent(textGerente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textJoysticks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel8))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(textTarjetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(iniciar))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textConsolas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(textPBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(textPPantalla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addComponent(jButton8)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addGap(18, 18, 18)))
                .addComponent(jButton7)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(textDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(311, 311, 311)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(textPJoystick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(textPTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        crearEnsamblador(Ensambladores);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void iniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciarActionPerformed

        int resp = JOptionPane.showConfirmDialog(null, "¿Desea iniciar la simulación con la configuración por defecto? \n Presionar NO lee los datos del archivo.", "Inicio de la simulación", JOptionPane.YES_NO_OPTION);
        if (resp == 0) {
            InicioPorDefecto();

        } else if (resp == 1) {

            InicioConArchivo();

        }


    }//GEN-LAST:event_iniciarActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        System.exit(WIDTH);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void textPantallasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPantallasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textPantallasActionPerformed

    private void textTarjetasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textTarjetasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textTarjetasActionPerformed

    private void textBotonesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textBotonesActionPerformed


    }//GEN-LAST:event_textBotonesActionPerformed

    private void textConsolasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textConsolasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textConsolasActionPerformed

    private void textPBotonesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPBotonesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textPBotonesActionPerformed

    private void boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxActionPerformed
        respuesta = box.getSelectedItem().toString();
        System.out.println(respuesta);

    }//GEN-LAST:event_boxActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            switch (respuesta) {

                case "Botones":
                    crearProductor(productorBotones, "boton", dormir, cantidadMaxBoton, almacen);
                    break;
                case "Pantallas":

                    crearProductor(productorPantallas, "pantalla", dormir, cantidadMaxPantalla, almacen);
                    break;
                case "Joysticks":
                    crearProductor(productorJoystick, "joystick", dormir, cantidadMaxJoystick, almacen);
                    break;
                case "Tarjetas":
                    crearProductor(productorTarjeta, "tarjeta", dormir, cantidadMaxTarjeta, almacen);
                    break;

                default:
                    System.out.println("no entre al switch del box");
                    break;

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            switch (respuesta) {

                case "Botones":
                    eliminarProductor(productorBotones, "Boton");

                    break;
                case "Pantallas":
                    eliminarProductor(productorPantallas, "Pantalla");
                    break;
                case "Joysticks":
                    eliminarProductor(productorJoystick, "Joystick");
                    break;
                case "Tarjetas":
                    eliminarProductor(productorTarjeta, "Tarjeta");
                    break;

                default:
                    System.out.println("no entre al switch del box");
                    break;

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        eliminarEnsamblador(Ensambladores);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void textDiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textDiasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textDiasActionPerformed

    private void textPPantallaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPPantallaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textPPantallaActionPerformed

    private void textEnsambladorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textEnsambladorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textEnsambladorActionPerformed

    private void textJefeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textJefeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textJefeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> box;
    private javax.swing.JButton iniciar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField textBotones;
    private javax.swing.JTextField textConsolas;
    private javax.swing.JTextField textDias;
    private javax.swing.JTextField textEnsamblador;
    private javax.swing.JTextField textGerente;
    private javax.swing.JTextField textJefe;
    private javax.swing.JTextField textJoysticks;
    private javax.swing.JTextField textPBotones;
    private javax.swing.JTextField textPJoystick;
    private javax.swing.JTextField textPPantalla;
    private javax.swing.JTextField textPTarjeta;
    private javax.swing.JTextField textPantallaTactil;
    private javax.swing.JTextField textPantallas;
    private javax.swing.JTextField textTarjetas;
    // End of variables declaration//GEN-END:variables
}
