package gui;

import com.github.lgooddatepicker.components.DatePicker;
import gui.base.enums.GenerosVideojuegos;
import gui.base.enums.TiposCompanniaPlataformas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Vista extends JFrame{
    private JPanel panel1;
    private final static String TITULO_FRAME="Aplicacion tiendaVideojuegos";

    //libros
    JPanel JPanelVideojuegos;
    JTextField txtTituloVideojuego;
    JComboBox comboCompanniaVideojugo;
    JComboBox comboPlataformaVideojuego;
    JComboBox comboGenero;
    DatePicker fechaSalida;
    JTextField txtIdVideojuego;
    JTextField txtPrecioVideojuego;
    JButton btnVideojuegosEliminar;
    JButton btnVideojuegosModificar;
    JButton btnVideojuegosAnadir;
    JTable videojuegosTabla;

    //autores
    JPanel JPanelFiguras;
    JTextField txtNombre;
    JTextField txtIdFiguras;
    JTextField txtPrecioFiguras;
    DatePicker fechaCreacion;
    JButton btnFigurasAnadir;
    JButton btnFigurasModificar;
    JButton btnFigurasEliminar;
    JTable figurasTabla;

    //editoriales
    JPanel JPanelCompannia;
    JTextField txtIdCompannia;
    JTextField txtNombreCompannia;
    JTextField txtEmail;
    JTextField txtTelefono;
    JComboBox comboTipoCompannia;
    JTextField txtWeb;
    JButton btnCompanniaAnadir;
    JButton btnCompanniaModificar;
    JButton btnCompanniaEliminar;
    JTable companniaTabla;

    //busqueda
    private JLabel etiquetaEstado;

    //default table model
    DefaultTableModel dtmCompannia;
    DefaultTableModel dtmFiguras;
    DefaultTableModel dtmVideojuegos;

    //menubar
    JMenuItem itemOpciones;
    JMenuItem itemDesconectar;
    JMenuItem itemSalir;

    //cuadro dialogo
    OptionDialog optionDialog;
    JDialog adminPasswordDialog;
    JButton btnValidate;
    JPasswordField adminPassword;
    private JTabbedPane tabbedPane1;
    private JComboBox comboBox1;

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
        for (TiposCompanniaPlataformas constant: TiposCompanniaPlataformas.values()) {
            comboTipoCompannia.addItem(constant.getValor());
        }
        //lo coloco en una posicion que no tenga valor
        comboTipoCompannia.setSelectedIndex(-1);
        for (GenerosVideojuegos constant: GenerosVideojuegos.values()) {
            comboGenero.addItem(constant.getValor());
        }
        comboGenero.setSelectedIndex(-1);
    }
    private void setTableModels() {
        //librosTabla, autoresTabla, editorialesTabla
        this.dtmVideojuegos =new DefaultTableModel();
        this.videojuegosTabla.setModel(dtmVideojuegos);

        this.dtmFiguras =new DefaultTableModel();
        this.figurasTabla.setModel(dtmFiguras);

        this.dtmCompannia =new DefaultTableModel();
        this.companniaTabla.setModel(dtmCompannia);
    }
}
