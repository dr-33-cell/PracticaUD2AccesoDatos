package gui;

import util.Util;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class Controlador implements ActionListener, ItemListener, ListSelectionListener, WindowListener {

    private Modelo modelo;
    private Vista vista;
    boolean refrescar;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        modelo.conectar();
        setOptions();
        addActionListeners(this);
        addItemListeners(this);
        addWindowListeners(this);
        refrescarTodo();
        iniciar();
    }

    private void refrescarTodo() {
        refrescarCompannia();
        refrescarFiguras();
        refrescarVideojuegos();
        refrescar = false;
    }

    private void addActionListeners(ActionListener listener) {
        vista.btnVideojuegosAnadir.addActionListener(listener);
        vista.btnVideojuegosAnadir.setActionCommand("anadirVideojuego");
        vista.btnFigurasAnadir.addActionListener(listener);
        vista.btnFigurasAnadir.setActionCommand("anadirFigura");
        vista.btnCompanniaAnadir.addActionListener(listener);
        vista.btnCompanniaAnadir.setActionCommand("anadirCompannia");
        vista.btnVideojuegosEliminar.addActionListener(listener);
        vista.btnVideojuegosEliminar.setActionCommand("eliminarVideojuego");
        vista.btnFigurasEliminar.addActionListener(listener);
        vista.btnFigurasEliminar.setActionCommand("eliminarFigura");
        vista.btnCompanniaEliminar.addActionListener(listener);
        vista.btnCompanniaEliminar.setActionCommand("eliminarCompannia");
        vista.btnVideojuegosModificar.addActionListener(listener);
        vista.btnVideojuegosModificar.setActionCommand("modificarVideojuego");
        vista.btnFigurasModificar.addActionListener(listener);
        vista.btnFigurasModificar.setActionCommand("modificarFigura");
        vista.btnCompanniaModificar.addActionListener(listener);
        vista.btnCompanniaModificar.setActionCommand("modificarCompannia");
        vista.optionDialog.btnOpcionesGuardar.addActionListener(listener);
        vista.optionDialog.btnOpcionesGuardar.setActionCommand("guardarOpciones");
        vista.itemOpciones.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
        vista.itemDesconectar.addActionListener(listener);
        vista.btnValidate.addActionListener(listener);
    }

    private void addWindowListeners(WindowListener listener) {
        vista.addWindowListener(listener);
    }

    void iniciar(){
        // Listener para tabla de Compañías
        vista.companniaTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = vista.companniaTabla.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.companniaTabla.getSelectionModel())) {
                        int row = vista.companniaTabla.getSelectedRow();
                        vista.txtNombreCompannia.setText(String.valueOf(vista.companniaTabla.getValueAt(row, 1)));
                        vista.txtEmail.setText(String.valueOf(vista.companniaTabla.getValueAt(row, 2)));
                        vista.txtTelefono.setText(String.valueOf(vista.companniaTabla.getValueAt(row, 3)));
                        vista.comboTipoCompannia.setSelectedItem(String.valueOf(vista.companniaTabla.getValueAt(row, 4)));
                        vista.txtWeb.setText(String.valueOf(vista.companniaTabla.getValueAt(row, 5)));
                    }
                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.companniaTabla.getSelectionModel())) {
                        borrarCamposCompannia();
                    }
                }
            }
        });

        // Listener para tabla de Figuras
        vista.figurasTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel2 = vista.figurasTabla.getSelectionModel();
        cellSelectionModel2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel2.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.figurasTabla.getSelectionModel())) {
                        int row = vista.figurasTabla.getSelectedRow();
                        vista.txtNombre.setText(String.valueOf(vista.figurasTabla.getValueAt(row, 1)));
                        vista.txtPrecioFiguras.setText(String.valueOf(vista.figurasTabla.getValueAt(row, 3)));
                        vista.fechaCreacion.setDate((Date.valueOf(String.valueOf(vista.figurasTabla.getValueAt(row, 4)))).toLocalDate());
                    }
                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.figurasTabla.getSelectionModel())) {
                        borrarCamposFiguras();
                    }
                }
            }
        });

        // Listener para tabla de Videojuegos
        vista.videojuegosTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel3 = vista.videojuegosTabla.getSelectionModel();
        cellSelectionModel3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel3.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.videojuegosTabla.getSelectionModel())) {
                        int row = vista.videojuegosTabla.getSelectedRow();
                        vista.txtTituloVideojuego.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 1)));
                        vista.comboGenero.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 2)));
                        vista.txtPrecioVideojuego.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 3)));
                        vista.fechaSalida.setDate((Date.valueOf(String.valueOf(vista.videojuegosTabla.getValueAt(row, 4)))).toLocalDate());
                        vista.comboPlataformaVideojuego.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 6)));
                        vista.comboCompanniaVideojugo.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 8)));
                    }
                } else if (e.getValueIsAdjusting()
                        && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                    if (e.getSource().equals(vista.videojuegosTabla.getSelectionModel())) {
                        borrarCamposVideojuegos();
                    }
                }
            }
        });
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
            if (e.getSource().equals(vista.companniaTabla.getSelectionModel())) {
                int row = vista.companniaTabla.getSelectedRow();
                vista.txtNombreCompannia.setText(String.valueOf(vista.companniaTabla.getValueAt(row, 1)));
                vista.txtEmail.setText(String.valueOf(vista.companniaTabla.getValueAt(row, 2)));
                vista.txtTelefono.setText(String.valueOf(vista.companniaTabla.getValueAt(row, 3)));
                vista.comboTipoCompannia.setSelectedItem(String.valueOf(vista.companniaTabla.getValueAt(row, 4)));
                vista.txtWeb.setText(String.valueOf(vista.companniaTabla.getValueAt(row, 5)));
            } else if (e.getSource().equals(vista.figurasTabla.getSelectionModel())) {
                int row = vista.figurasTabla.getSelectedRow();
                vista.txtNombre.setText(String.valueOf(vista.figurasTabla.getValueAt(row, 1)));
                vista.txtPrecioFiguras.setText(String.valueOf(vista.figurasTabla.getValueAt(row, 3)));
                vista.fechaCreacion.setDate((Date.valueOf(String.valueOf(vista.figurasTabla.getValueAt(row, 4)))).toLocalDate());
            } else if (e.getSource().equals(vista.videojuegosTabla.getSelectionModel())) {
                int row = vista.videojuegosTabla.getSelectedRow();
                vista.txtTituloVideojuego.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 1)));
                vista.comboGenero.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 2)));
                vista.txtPrecioVideojuego.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 3)));
                vista.fechaSalida.setDate((Date.valueOf(String.valueOf(vista.videojuegosTabla.getValueAt(row, 4)))).toLocalDate());
                vista.comboPlataformaVideojuego.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 6)));
                vista.comboCompanniaVideojugo.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 8)));
            } else if (e.getValueIsAdjusting()
                    && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                if (e.getSource().equals(vista.companniaTabla.getSelectionModel())) {
                    borrarCamposCompannia();
                } else if (e.getSource().equals(vista.figurasTabla.getSelectionModel())) {
                    borrarCamposFiguras();
                } else if (e.getSource().equals(vista.videojuegosTabla.getSelectionModel())) {
                    borrarCamposVideojuegos();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Opciones":
                vista.adminPasswordDialog.setVisible(true);
                break;
            case "Desconectar":
                modelo.desconectar();
                break;
            case "Salir":
                System.exit(0);
                break;
            case "abrirOpciones":
                if(String.valueOf(vista.adminPassword.getPassword()).equals(modelo.getAdminPassword())) {
                    vista.adminPassword.setText("");
                    vista.adminPasswordDialog.dispose();
                    vista.optionDialog.setVisible(true);
                } else {
                    Util.showErrorAlert("La contraseña introducida no es correcta.");
                }
                break;
            case "guardarOpciones":
                modelo.setPropValues(vista.optionDialog.txtIP.getText(), vista.optionDialog.txtUsuario.getText(),
                        String.valueOf(vista.optionDialog.pfPass.getPassword()), String.valueOf(vista.optionDialog.pfAdmin.getPassword()));
                vista.optionDialog.dispose();
                vista.dispose();
                new Controlador(new Modelo(), new Vista());
                break;

            // ===============================
            // CASOS PARA VIDEOJUEGOS
            // ===============================
            case "anadirVideojuego": {
                try {
                    if (comprobarVideojuegoVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.videojuegosTabla.clearSelection();
                    } else if (modelo.tituloProductoYaExiste(vista.txtTituloVideojuego.getText())) {
                        Util.showErrorAlert("Ese título ya existe.\nIntroduce un videojuego diferente");
                        vista.videojuegosTabla.clearSelection();
                    } else {
                        modelo.insertarVideojuego(
                                vista.txtTituloVideojuego.getText(),
                                String.valueOf(vista.comboCompanniaVideojugo.getSelectedItem()),
                                String.valueOf(vista.comboPlataformaVideojuego.getSelectedItem()),
                                String.valueOf(vista.comboGenero.getSelectedItem()),
                                vista.fechaSalida.getDate(),
                                Integer.parseInt(vista.txtIdVideojuego.getText()),
                                Integer.parseInt(vista.txtPrecioVideojuego.getText()));
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.videojuegosTabla.clearSelection();
                }
                borrarCamposVideojuegos();
                refrescarVideojuegos();
            }
            break;

            case "modificarVideojuego": {
                try {
                    if (comprobarVideojuegoVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.videojuegosTabla.clearSelection();
                    } else {
                        modelo.modificarVideojuego(
                                vista.txtTituloVideojuego.getText(),
                                String.valueOf(vista.comboGenero.getSelectedItem()),
                                Double.parseDouble(vista.txtPrecioVideojuego.getText()),
                                vista.fechaSalida.getDate(),
                                true,
                                String.valueOf(vista.comboPlataformaVideojuego.getSelectedItem()),
                                "Digital",
                                String.valueOf(vista.comboCompanniaVideojugo.getSelectedItem()),
                                (Integer) vista.videojuegosTabla.getValueAt(vista.videojuegosTabla.getSelectedRow(), 0));
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.videojuegosTabla.clearSelection();
                }
                borrarCamposVideojuegos();
                refrescarVideojuegos();
            }
            break;

            case "eliminarVideojuego":
                modelo.eliminarVideojuego((Integer) vista.videojuegosTabla.getValueAt(vista.videojuegosTabla.getSelectedRow(), 0));
                borrarCamposVideojuegos();
                refrescarVideojuegos();
                break;

            // ===============================
            // CASOS PARA FIGURAS
            // ===============================
            case "anadirFigura": {
                try {
                    if (comprobarFiguraVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.figurasTabla.clearSelection();
                    } else if (modelo.tituloProductoYaExiste(vista.txtNombre.getText())) {
                        Util.showErrorAlert("Ese título ya existe.\nIntroduce una figura diferente");
                        vista.figurasTabla.clearSelection();
                    } else {
                        modelo.insertarFigura(
                                vista.txtNombre.getText(),
                                "Figura",
                                Double.parseDouble(vista.txtPrecioFiguras.getText()),
                                vista.fechaCreacion.getDate(),
                                true,
                                0.0,
                                "PVC",
                                "1 - Default");
                        refrescarFiguras();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.figurasTabla.clearSelection();
                }
                borrarCamposFiguras();
            }
            break;

            case "modificarFigura": {
                try {
                    if (comprobarFiguraVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.figurasTabla.clearSelection();
                    } else {
                        modelo.modificarFigura(
                                vista.txtNombre.getText(),
                                "Figura",
                                Double.parseDouble(vista.txtPrecioFiguras.getText()),
                                vista.fechaCreacion.getDate(),
                                true,
                                0.0,
                                "PVC",
                                "1 - Default",
                                (Integer) vista.figurasTabla.getValueAt(vista.figurasTabla.getSelectedRow(), 0));
                        refrescarFiguras();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.figurasTabla.clearSelection();
                }
                borrarCamposFiguras();
            }
            break;

            case "eliminarFigura":
                modelo.eliminarFigura((Integer) vista.figurasTabla.getValueAt(vista.figurasTabla.getSelectedRow(), 0));
                borrarCamposFiguras();
                refrescarFiguras();
                break;

            // ===============================
            // CASOS PARA COMPAÑÍAS
            // ===============================
            case "anadirCompannia": {
                try {
                    if (comprobarCompanniaVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.companniaTabla.clearSelection();
                    } else if (modelo.companniaYaExiste(vista.txtNombreCompannia.getText())) {
                        Util.showErrorAlert("Ese nombre ya existe.\nIntroduce una compañía diferente.");
                        vista.companniaTabla.clearSelection();
                    } else {
                        modelo.insertarCompannia(
                                vista.txtNombreCompannia.getText(),
                                vista.txtEmail.getText(),
                                vista.txtTelefono.getText(),
                                String.valueOf(vista.comboTipoCompannia.getSelectedItem()),
                                vista.txtWeb.getText());
                        refrescarCompannia();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.companniaTabla.clearSelection();
                }
                borrarCamposCompannia();
            }
            break;

            case "modificarCompannia": {
                try {
                    if (comprobarCompanniaVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.companniaTabla.clearSelection();
                    } else {
                        modelo.modificarCompannia(
                                vista.txtNombreCompannia.getText(),
                                vista.txtEmail.getText(),
                                vista.txtTelefono.getText(),
                                String.valueOf(vista.comboTipoCompannia.getSelectedItem()),
                                vista.txtWeb.getText(),
                                (Integer) vista.companniaTabla.getValueAt(vista.companniaTabla.getSelectedRow(), 0));
                        refrescarCompannia();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.companniaTabla.clearSelection();
                }
                borrarCamposCompannia();
            }
            break;

            case "eliminarCompannia":
                modelo.eliminarCompannia((Integer) vista.companniaTabla.getValueAt(vista.companniaTabla.getSelectedRow(), 0));
                borrarCamposCompannia();
                refrescarCompannia();
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }


    private void refrescarCompannia() {
        try {
            ResultSet rs = modelo.consultarCompannia();
            vista.dtmCompannia = construirTableModelCompannia(rs);
            vista.companniaTabla.setModel(vista.dtmCompannia);

            // Refrescar combo de compañías
            vista.comboCompanniaVideojugo.removeAllItems();
            for(int i = 0; i < vista.dtmCompannia.getRowCount(); i++) {
                vista.comboCompanniaVideojugo.addItem(vista.dtmCompannia.getValueAt(i, 0) + " - " +
                        vista.dtmCompannia.getValueAt(i, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModelCompannia(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Obtener nombres de columnas
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Crear modelo con las columnas
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Agregar filas
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        return model;
    }

    private void refrescarFiguras() {
        try {
            ResultSet rs = modelo.consultarFiguras();
            vista.dtmFiguras = construirTableModelFiguras(rs);
            vista.figurasTabla.setModel(vista.dtmFiguras);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModelFiguras(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Obtener nombres de columnas
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Crear modelo con las columnas
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Agregar filas
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        return model;
    }

    private void refrescarVideojuegos() {
        try {
            ResultSet rs = modelo.consultarVideojuegos();
            vista.dtmVideojuegos = construirTableModelVideojuegos(rs);
            vista.videojuegosTabla.setModel(vista.dtmVideojuegos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModelVideojuegos(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Obtener nombres de columnas
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Crear modelo con las columnas
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Agregar filas
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        return model;
    }

    // ===============================
    // MÉTODOS AUXILIARES
    // ===============================

    private void setOptions() {
        vista.optionDialog.txtIP.setText(modelo.getIp());
        vista.optionDialog.txtUsuario.setText(modelo.getUser());
        vista.optionDialog.pfPass.setText(modelo.getPassword());
        vista.optionDialog.pfAdmin.setText(modelo.getAdminPassword());
    }

    private void borrarCamposVideojuegos() {
        vista.txtTituloVideojuego.setText("");
        vista.comboGenero.setSelectedIndex(-1);
        vista.txtPrecioVideojuego.setText("");
        vista.fechaSalida.setText("");
        vista.comboPlataformaVideojuego.setSelectedIndex(-1);
        vista.comboCompanniaVideojugo.setSelectedIndex(-1);
        vista.txtIdVideojuego.setText("");
    }

    private void borrarCamposFiguras() {
        vista.txtNombre.setText("");
        vista.txtPrecioFiguras.setText("");
        vista.fechaCreacion.setText("");
        vista.txtIdFiguras.setText("");
    }

    private void borrarCamposCompannia() {
        vista.txtNombreCompannia.setText("");
        vista.txtEmail.setText("");
        vista.txtTelefono.setText("");
        vista.comboTipoCompannia.setSelectedIndex(-1);
        vista.txtWeb.setText("");
        vista.txtIdCompannia.setText("");
    }

    private boolean comprobarVideojuegoVacio() {
        return vista.txtTituloVideojuego.getText().isEmpty() ||
                vista.txtPrecioVideojuego.getText().isEmpty() ||
                vista.txtIdVideojuego.getText().isEmpty() ||
                vista.comboGenero.getSelectedIndex() == -1 ||
                vista.comboCompanniaVideojugo.getSelectedIndex() == -1 ||
                vista.comboPlataformaVideojuego.getSelectedIndex() == -1 ||
                vista.fechaSalida.getText().isEmpty();
    }

    private boolean comprobarFiguraVacia() {
        return vista.txtNombre.getText().isEmpty() ||
                vista.txtPrecioFiguras.getText().isEmpty() ||
                vista.txtIdFiguras.getText().isEmpty() ||
                vista.fechaCreacion.getText().isEmpty();
    }

    private boolean comprobarCompanniaVacia() {
        return vista.txtNombreCompannia.getText().isEmpty() ||
                vista.txtEmail.getText().isEmpty() ||
                vista.txtTelefono.getText().isEmpty() ||
                vista.comboTipoCompannia.getSelectedIndex() == -1 ||
                vista.txtWeb.getText().isEmpty();
    }


    private void addItemListeners(Controlador controlador) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }
}
