package gui;

import base.enums.Fabricantes;
import base.enums.Paises;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Vista extends JFrame{
    private JPanel panel1;
    private final static String TITULO_FRAME="Aplicacion tiendaVideojuegos";

    //Desarrolladores
    JPanel JPanelDesarrolladores;
    JTextField txtIdDesarrolladores;
    JTextField txtNombreDesarrolladores;
    JComboBox comBoxPais;
    DatePicker fechaDesarrolladores;
    JTextField txtWebDesarrolladores;
    JTextField txtEmpleados;
    JButton btnDesarrolladoresEliminar;
    JButton btnDesarrolladoresModificar;
    JButton btnDesarrolladoresAñadir;
    JTable desarrolladoresTabla;

    //Plataformas
    JPanel JPanelPlataformas;
    JTextField txtIdPlataformas;
    JTextField txtNombrePlataformas;
    JComboBox comBoxFabricPlatform;
    JTextField txtGeneracionPlataformas;
    DatePicker fechaPlataform;
    JTextField txtTipoPlatform;
    JButton btnPlataformasAñadir;
    JButton btnPlataformasModificar;
    JButton btnPlataformasEliminar;
    JTable plataformasTabla;

    //Videojuegos
    JPanel JPanelVideojuegos;
    JTextField txtIdVideojuegos;
    JTextField txtTitulovideojuegos;
    JTextField txtCodigoVideojuegos;
    JComboBox comBoxDesaVideojuegos;
    JComboBox comBoxPlatformVideojuegos;
    JTextField txtGeneroVideojuegos;
    JTextField txtPrecioVideojuegos;
    DatePicker fechaVideojuegos;
    JTextField txtClasificacionVideojuegos;
    JTextField txtStockVideojuegos;
    JTextField txtBusqueda;
    JButton btnBuscar;
    JButton btnLimpiarBusqueda;
    JButton btnVideojuegosAñadir;
    JButton btnVideojuegosModificar;
    JButton btnVideojuegosEliminar;
    JTable videojuegosTabla;

    //Ventas
    JPanel JPanelVentas;
    JTextField txtIdVenta;
    JComboBox comBoxVideojuegoVenta;
    JTextField txtCantidadVenta;
    JTextField txtPrecioVenta;
    DatePicker fechaVenta;
    JTextField txtClienteVenta;
    JButton btnRegistrarVenta;
    JButton btnEliminarVenta;
    JTable ventasTabla;
    JLabel lblTotalVentas;
    JLabel lblIngresoTotal;

    //busqueda
    JLabel etiquetaEstado;

    //default table model
    DefaultTableModel dtmVideojuegos;
    DefaultTableModel dtmPlataformas;
    DefaultTableModel dtmDesarrolladores;
    DefaultTableModel dtmVentas;

    //menubar
    JMenuItem itemOpciones;
    JMenuItem itemDesconectar;
    JMenuItem itemSalir;

    //cuadro dialogo
    OptionDialog optionDialog;
    JDialog adminPasswordDialog;
    JButton btnValidate;
    JPasswordField adminPassword;
    JTabbedPane tabbedPane1;


    public Vista() {
        super(TITULO_FRAME);
        initFrame();
    }

    public void initFrame() {
        this.setContentPane(panel1);
        //al clickar en cerrar no hace nada
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.pack();
        //doy dimension
        this.setSize(new Dimension(this.getWidth()+100,this.getHeight()));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        //creo cuadro dialogo
        optionDialog=new OptionDialog(this);
        //llamo menu
        setMenu();
        //llamo cuadro dialogo admin
        setAdminDialog();
        //cargo enumerados
        setEnumComboBox();
        //cargo table models
        setTableModels();
    }
    private void setMenu() {
        JMenuBar mbBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        itemOpciones = new JMenuItem("Opciones");
        itemOpciones.setActionCommand("Opciones");
        itemDesconectar = new JMenuItem("Desconectar");
        itemDesconectar.setActionCommand("Desconectar");
        itemSalir=new JMenuItem("Salir");
        itemSalir.setActionCommand("Salir");
        menu.add(itemOpciones);
        menu.add(itemDesconectar);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }
    private void setAdminDialog() {
        btnValidate = new JButton("Validar");
        btnValidate.setActionCommand("abrirOpciones");
        adminPassword = new JPasswordField();
        //dimension al cuadro de texto
        adminPassword.setPreferredSize(new Dimension(100,26));
        Object[] options=new Object[] {adminPassword,btnValidate};
        JOptionPane jop = new JOptionPane("Introduce la contraseña",JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION,null,options);
        adminPasswordDialog = new JDialog(this,"Opciones",true);
        adminPasswordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        adminPasswordDialog.setContentPane(jop);
        adminPasswordDialog.pack();
        adminPasswordDialog.setLocationRelativeTo(this);
    }
    private void setEnumComboBox() {
        //recorrer los enumerados y los cargo en el comboBox correspondiente
        //.values cogemos valores del enumerado
        //.getValor los añadimos al combo
        for (Paises constant: Paises.values()) {
            comBoxPais.addItem(constant.getValor());
        }
        //lo coloco en una posicion que no tenga valor
        comBoxPais.setSelectedIndex(-1);
        for (Fabricantes constant: Fabricantes.values()) {
            comBoxFabricPlatform.addItem(constant.getValor());
        }
        comBoxFabricPlatform.setSelectedIndex(-1);
    }
    private void setTableModels() {
        if (desarrolladoresTabla != null) {
            this.dtmDesarrolladores = new DefaultTableModel();
            this.desarrolladoresTabla.setModel(dtmDesarrolladores);
        }

        if (plataformasTabla != null) {
            this.dtmPlataformas = new DefaultTableModel();
            this.plataformasTabla.setModel(dtmPlataformas);
        }

        if (videojuegosTabla != null) {
            this.dtmVideojuegos = new DefaultTableModel();
            this.videojuegosTabla.setModel(dtmVideojuegos);
        }

        if (ventasTabla != null) {
            this.dtmVentas = new DefaultTableModel();
            this.ventasTabla.setModel(dtmVentas);
        }
    }
}