package gui;

import util.Util;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

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
        actualizarEstadisticas();
    }

    private void refrescarTodo() {
        refrescarDesarrolladores();
        refrescarPlataformas();
        refrescarVideojuegos();
        refrescarVentas();
        refrescar = false;
    }

    private void addActionListeners(ActionListener listener) {
        vista.btnDesarrolladoresAñadir.addActionListener(listener);
        vista.btnDesarrolladoresAñadir.setActionCommand("anadirDesarrollador");
        vista.btnPlataformasAñadir.addActionListener(listener);
        vista.btnPlataformasAñadir.setActionCommand("anadirPlataforma");
        vista.btnVideojuegosAñadir.addActionListener(listener);
        vista.btnVideojuegosAñadir.setActionCommand("anadirVideojuego");
        vista.btnDesarrolladoresEliminar.addActionListener(listener);
        vista.btnDesarrolladoresEliminar.setActionCommand("eliminarDesarrollador");
        vista.btnPlataformasEliminar.addActionListener(listener);
        vista.btnPlataformasEliminar.setActionCommand("eliminarPlataforma");
        vista.btnVideojuegosEliminar.addActionListener(listener);
        vista.btnVideojuegosEliminar.setActionCommand("eliminarVideojuego");
        vista.btnDesarrolladoresModificar.addActionListener(listener);
        vista.btnDesarrolladoresModificar.setActionCommand("modificarDesarrollador");
        vista.btnPlataformasModificar.addActionListener(listener);
        vista.btnPlataformasModificar.setActionCommand("modificarPlataforma");
        vista.btnVideojuegosModificar.addActionListener(listener);
        vista.btnVideojuegosModificar.setActionCommand("modificarVideojuego");
        vista.optionDialog.btnOpcionesGuardar.addActionListener(listener);
        vista.optionDialog.btnOpcionesGuardar.setActionCommand("guardarOpciones");
        vista.itemOpciones.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
        vista.itemDesconectar.addActionListener(listener);
        vista.btnValidate.addActionListener(listener);
        vista.btnBuscar.addActionListener(listener);
        vista.btnBuscar.setActionCommand("buscarVideojuego");
        vista.btnLimpiarBusqueda.addActionListener(listener);
        vista.btnLimpiarBusqueda.setActionCommand("limpiarBusqueda");

        // VENTAS
        vista.btnRegistrarVenta.addActionListener(listener);
        vista.btnRegistrarVenta.setActionCommand("registrarVenta");
        vista.btnEliminarVenta.addActionListener(listener);
        vista.btnEliminarVenta.setActionCommand("eliminarVenta");
    }

    private void addWindowListeners(WindowListener listener) {
        vista.addWindowListener(listener);
    }

    void iniciar() {
        // DESARROLLADORES
        vista.desarrolladoresTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = vista.desarrolladoresTabla.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.desarrolladoresTabla.getSelectionModel())) {
                        int row = vista.desarrolladoresTabla.getSelectedRow();
                        vista.txtNombreDesarrolladores.setText(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 1)));
                        vista.comBoxPais.setSelectedItem(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 2)));
                        vista.fechaDesarrolladores.setDate((Date.valueOf(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 3)))).toLocalDate());
                        vista.txtWebDesarrolladores.setText(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 4)));
                        vista.txtEmpleados.setText(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 5)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.desarrolladoresTabla.getSelectionModel())) {
                            borrarCamposDesarrolladores();
                        } else if (e.getSource().equals(vista.plataformasTabla.getSelectionModel())) {
                            borrarCamposPlataformas();
                        } else if (e.getSource().equals(vista.videojuegosTabla.getSelectionModel())) {
                            borrarCamposVideojuegos();
                        }
                    }
                }
            }
        });

        // PLATAFORMAS
        vista.plataformasTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel2 = vista.plataformasTabla.getSelectionModel();
        cellSelectionModel2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel2.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.plataformasTabla.getSelectionModel())) {
                        int row = vista.plataformasTabla.getSelectedRow();
                        vista.txtNombrePlataformas.setText(String.valueOf(vista.plataformasTabla.getValueAt(row, 1)));
                        vista.comBoxFabricPlatform.setSelectedItem(String.valueOf(vista.plataformasTabla.getValueAt(row, 2)));
                        vista.txtGeneracionPlataformas.setText(String.valueOf(vista.plataformasTabla.getValueAt(row, 3)));
                        vista.fechaPlataform.setDate((Date.valueOf(String.valueOf(vista.plataformasTabla.getValueAt(row, 4)))).toLocalDate());
                        vista.txtTipoPlatform.setText(String.valueOf(vista.plataformasTabla.getValueAt(row, 5)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.desarrolladoresTabla.getSelectionModel())) {
                            borrarCamposDesarrolladores();
                        } else if (e.getSource().equals(vista.plataformasTabla.getSelectionModel())) {
                            borrarCamposPlataformas();
                        } else if (e.getSource().equals(vista.videojuegosTabla.getSelectionModel())) {
                            borrarCamposVideojuegos();
                        }
                    }
                }
            }
        });

        // VIDEOJUEGOS
        vista.videojuegosTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel3 = vista.videojuegosTabla.getSelectionModel();
        cellSelectionModel3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel3.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.videojuegosTabla.getSelectionModel())) {
                        int row = vista.videojuegosTabla.getSelectedRow();
                        vista.txtTitulovideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 1)));
                        vista.txtCodigoVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 2)));
                        vista.comBoxDesaVideojuegos.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 3)));
                        vista.comBoxPlatformVideojuegos.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 4)));
                        vista.txtGeneroVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 5)));
                        vista.txtPrecioVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 6)));
                        vista.fechaVideojuegos.setDate((Date.valueOf(String.valueOf(vista.videojuegosTabla.getValueAt(row, 7)))).toLocalDate());
                        vista.txtClasificacionVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 8)));
                        vista.txtStockVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 9)));
                    } else if (e.getValueIsAdjusting()
                            && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                        if (e.getSource().equals(vista.desarrolladoresTabla.getSelectionModel())) {
                            borrarCamposDesarrolladores();
                        } else if (e.getSource().equals(vista.plataformasTabla.getSelectionModel())) {
                            borrarCamposPlataformas();
                        } else if (e.getSource().equals(vista.videojuegosTabla.getSelectionModel())) {
                            borrarCamposVideojuegos();
                        }
                    }
                }
            }
        });

        // VENTAS
        vista.ventasTabla.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel4 = vista.ventasTabla.getSelectionModel();
        cellSelectionModel4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel4.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()
                        && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
                    if (e.getSource().equals(vista.ventasTabla.getSelectionModel())) {
                        int row = vista.ventasTabla.getSelectedRow();
                        vista.comBoxVideojuegoVenta.setSelectedItem(String.valueOf(vista.ventasTabla.getValueAt(row, 1)));
                        vista.txtCantidadVenta.setText(String.valueOf(vista.ventasTabla.getValueAt(row, 2)));
                        vista.txtPrecioVenta.setText(String.valueOf(vista.ventasTabla.getValueAt(row, 3)));
                        vista.fechaVenta.setDate((Date.valueOf(String.valueOf(vista.ventasTabla.getValueAt(row, 5)))).toLocalDate());
                        vista.txtClienteVenta.setText(String.valueOf(vista.ventasTabla.getValueAt(row, 6)));
                    }
                }
            }
        });

        // Listener para auto-completar precio al seleccionar videojuego:
        vista.comBoxVideojuegoVenta.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && vista.comBoxVideojuegoVenta.getSelectedIndex() != -1) {
                    String seleccion = String.valueOf(vista.comBoxVideojuegoVenta.getSelectedItem());
                    int idVideojuego = Integer.parseInt(seleccion.split(" ")[0]);
                    float precio = modelo.obtenerPrecioVideojuego(idVideojuego);
                    vista.txtPrecioVenta.setText(String.valueOf(precio));
                }
            }
        });
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && !((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
            if (e.getSource().equals(vista.desarrolladoresTabla.getSelectionModel())) {
                int row = vista.desarrolladoresTabla.getSelectedRow();
                vista.txtNombreDesarrolladores.setText(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 1)));
                vista.comBoxPais.setSelectedItem(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 2)));
                vista.fechaDesarrolladores.setDate((Date.valueOf(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 3)))).toLocalDate());
                vista.txtWebDesarrolladores.setText(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 4)));
                vista.txtEmpleados.setText(String.valueOf(vista.desarrolladoresTabla.getValueAt(row, 5)));
            } else if (e.getSource().equals(vista.plataformasTabla.getSelectionModel())) {
                int row = vista.plataformasTabla.getSelectedRow();
                vista.txtNombrePlataformas.setText(String.valueOf(vista.plataformasTabla.getValueAt(row, 1)));
                vista.comBoxFabricPlatform.setSelectedItem(String.valueOf(vista.plataformasTabla.getValueAt(row, 2)));
                vista.txtGeneracionPlataformas.setText(String.valueOf(vista.plataformasTabla.getValueAt(row, 3)));
                vista.fechaPlataform.setDate((Date.valueOf(String.valueOf(vista.plataformasTabla.getValueAt(row, 4)))).toLocalDate());
                vista.txtTipoPlatform.setText(String.valueOf(vista.plataformasTabla.getValueAt(row, 5)));
            } else if (e.getSource().equals(vista.videojuegosTabla.getSelectionModel())) {
                int row = vista.videojuegosTabla.getSelectedRow();
                vista.txtTitulovideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 1)));
                vista.txtCodigoVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 2)));
                vista.comBoxDesaVideojuegos.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 3)));
                vista.comBoxPlatformVideojuegos.setSelectedItem(String.valueOf(vista.videojuegosTabla.getValueAt(row, 4)));
                vista.txtGeneroVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 5)));
                vista.txtPrecioVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 6)));
                vista.fechaVideojuegos.setDate((Date.valueOf(String.valueOf(vista.videojuegosTabla.getValueAt(row, 7)))).toLocalDate());
                vista.txtClasificacionVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 8)));
                vista.txtStockVideojuegos.setText(String.valueOf(vista.videojuegosTabla.getValueAt(row, 9)));
            } else if (e.getValueIsAdjusting()
                    && ((ListSelectionModel) e.getSource()).isSelectionEmpty() && !refrescar) {
                if (e.getSource().equals(vista.desarrolladoresTabla.getSelectionModel())) {
                    borrarCamposDesarrolladores();
                } else if (e.getSource().equals(vista.plataformasTabla.getSelectionModel())) {
                    borrarCamposPlataformas();
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
                if (String.valueOf(vista.adminPassword.getPassword()).equals(modelo.getAdminPassword())) {
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

            // DESARROLLADORES
            case "anadirDesarrollador": {
                try {
                    if (comprobarDesarrolladorVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.desarrolladoresTabla.clearSelection();
                    } else if (modelo.desarrolladorNombreYaExiste(vista.txtNombreDesarrolladores.getText())) {
                        Util.showErrorAlert("Ese nombre ya existe.\nIntroduce un desarrollador diferente");
                        vista.desarrolladoresTabla.clearSelection();
                    } else {
                        modelo.insertarDesarrollador(
                                vista.txtNombreDesarrolladores.getText(),
                                String.valueOf(vista.comBoxPais.getSelectedItem()),
                                vista.fechaDesarrolladores.getDate(),
                                vista.txtWebDesarrolladores.getText(),
                                Integer.parseInt(vista.txtEmpleados.getText()));
                        refrescarDesarrolladores();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.desarrolladoresTabla.clearSelection();
                }
                borrarCamposDesarrolladores();
            }
            break;
            case "modificarDesarrollador": {
                try {
                    if (comprobarDesarrolladorVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.desarrolladoresTabla.clearSelection();
                    } else {
                        modelo.modificarDesarrollador(
                                vista.txtNombreDesarrolladores.getText(),
                                String.valueOf(vista.comBoxPais.getSelectedItem()),
                                vista.fechaDesarrolladores.getDate(),
                                vista.txtWebDesarrolladores.getText(),
                                Integer.parseInt(vista.txtEmpleados.getText()),
                                (Integer) vista.desarrolladoresTabla.getValueAt(vista.desarrolladoresTabla.getSelectedRow(), 0));
                        refrescarDesarrolladores();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.desarrolladoresTabla.clearSelection();
                }
                borrarCamposDesarrolladores();
            }
            break;
            case "eliminarDesarrollador":
                modelo.eliminarDesarrollador((Integer) vista.desarrolladoresTabla.getValueAt(vista.desarrolladoresTabla.getSelectedRow(), 0));
                borrarCamposDesarrolladores();
                refrescarDesarrolladores();
                break;

            // PLATAFORMAS
            case "anadirPlataforma": {
                try {
                    if (comprobarPlataformaVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.plataformasTabla.clearSelection();
                    } else if (modelo.plataformaNombreYaExiste(vista.txtNombrePlataformas.getText())) {
                        Util.showErrorAlert("Ese nombre ya existe.\nIntroduce una plataforma diferente");
                        vista.plataformasTabla.clearSelection();
                    } else {
                        modelo.insertarPlataforma(
                                vista.txtNombrePlataformas.getText(),
                                String.valueOf(vista.comBoxFabricPlatform.getSelectedItem()),
                                Integer.parseInt(vista.txtGeneracionPlataformas.getText()),
                                vista.fechaPlataform.getDate(),
                                vista.txtTipoPlatform.getText());
                        refrescarPlataformas();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.plataformasTabla.clearSelection();
                }
                borrarCamposPlataformas();
            }
            break;
            case "modificarPlataforma": {
                try {
                    if (comprobarPlataformaVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.plataformasTabla.clearSelection();
                    } else {
                        modelo.modificarPlataforma(
                                vista.txtNombrePlataformas.getText(),
                                String.valueOf(vista.comBoxFabricPlatform.getSelectedItem()),
                                Integer.parseInt(vista.txtGeneracionPlataformas.getText()),
                                vista.fechaPlataform.getDate(),
                                vista.txtTipoPlatform.getText(),
                                (Integer) vista.plataformasTabla.getValueAt(vista.plataformasTabla.getSelectedRow(), 0));
                        refrescarPlataformas();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números en los campos que lo requieren");
                    vista.plataformasTabla.clearSelection();
                }
                borrarCamposPlataformas();
            }
            break;
            case "eliminarPlataforma":
                modelo.eliminarPlataforma((Integer) vista.plataformasTabla.getValueAt(vista.plataformasTabla.getSelectedRow(), 0));
                borrarCamposPlataformas();
                refrescarPlataformas();
                break;

            // VIDEOJUEGOS
            case "anadirVideojuego": {
                try {
                    if (comprobarVideojuegoVacio()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.videojuegosTabla.clearSelection();
                    } else if (modelo.videojuegoCodigoYaExiste(vista.txtCodigoVideojuegos.getText())) {
                        Util.showErrorAlert("Ese código ya existe.\nIntroduce un videojuego diferente");
                        vista.videojuegosTabla.clearSelection();
                    } else {
                        modelo.insertarVideojuego(
                                vista.txtTitulovideojuegos.getText(),
                                vista.txtCodigoVideojuegos.getText(),
                                String.valueOf(vista.comBoxDesaVideojuegos.getSelectedItem()),
                                String.valueOf(vista.comBoxPlatformVideojuegos.getSelectedItem()),
                                vista.txtGeneroVideojuegos.getText(),
                                Float.parseFloat(vista.txtPrecioVideojuegos.getText()),
                                vista.fechaVideojuegos.getDate(),
                                vista.txtClasificacionVideojuegos.getText(),
                                Integer.parseInt(vista.txtStockVideojuegos.getText()));
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
                                vista.txtTitulovideojuegos.getText(),
                                vista.txtCodigoVideojuegos.getText(),
                                String.valueOf(vista.comBoxDesaVideojuegos.getSelectedItem()),
                                String.valueOf(vista.comBoxPlatformVideojuegos.getSelectedItem()),
                                vista.txtGeneroVideojuegos.getText(),
                                Float.parseFloat(vista.txtPrecioVideojuegos.getText()),
                                vista.fechaVideojuegos.getDate(),
                                vista.txtClasificacionVideojuegos.getText(),
                                Integer.parseInt(vista.txtStockVideojuegos.getText()),
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

            // BÚSQUEDA
            case "buscarVideojuego":
                buscarVideojuegos();
                break;

            case "limpiarBusqueda":
                vista.txtBusqueda.setText("");
                refrescarVideojuegos();
                break;

            // VENTAS
            case "registrarVenta": {
                try {
                    if (comprobarVentaVacia()) {
                        Util.showErrorAlert("Rellena todos los campos");
                        vista.ventasTabla.clearSelection();
                    } else {
                        String videojuegoSeleccionado = String.valueOf(vista.comBoxVideojuegoVenta.getSelectedItem());
                        int idVideojuego = Integer.parseInt(videojuegoSeleccionado.split(" ")[0]);
                        int cantidad = Integer.parseInt(vista.txtCantidadVenta.getText());

                        int stockDisponible = modelo.obtenerStockVideojuego(idVideojuego);
                        if (cantidad > stockDisponible) {
                            Util.showErrorAlert("Stock insuficiente. Disponible: " + stockDisponible + " unidades");
                            return;
                        }

                        modelo.registrarVenta(
                                videojuegoSeleccionado,
                                cantidad,
                                Float.parseFloat(vista.txtPrecioVenta.getText()),
                                vista.fechaVenta.getDate(),
                                vista.txtClienteVenta.getText());

                        Util.showInfoAlert("Venta registrada correctamente");
                        refrescarVentas();
                        refrescarVideojuegos();
                        actualizarEstadisticas();
                    }
                } catch (NumberFormatException nfe) {
                    Util.showErrorAlert("Introduce números válidos en cantidad y precio");
                    vista.ventasTabla.clearSelection();
                } catch (Exception ex) {
                    Util.showErrorAlert("Error al registrar venta: " + ex.getMessage());
                }
                borrarCamposVentas();
            }
            break;

            case "eliminarVenta":
                if (vista.ventasTabla.getSelectedRow() == -1) {
                    Util.showErrorAlert("Selecciona una venta para eliminar");
                } else {
                    int confirmacion = JOptionPane.showConfirmDialog(vista,
                            "¿Estás seguro de eliminar esta venta?\nSe restaurará el stock del videojuego.",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        modelo.eliminarVenta((Integer) vista.ventasTabla.getValueAt(vista.ventasTabla.getSelectedRow(), 0));
                        borrarCamposVentas();
                        refrescarVentas();
                        refrescarVideojuegos();
                        actualizarEstadisticas();
                    }
                }
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    // MÉTODOS DE REFRESCAR
    private void refrescarDesarrolladores() {
        try {
            vista.desarrolladoresTabla.setModel(construirTableModelDesarrolladores(modelo.consultarDesarrolladores()));
            vista.comBoxDesaVideojuegos.removeAllItems();
            for (int i = 0; i < vista.dtmDesarrolladores.getRowCount(); i++) {
                vista.comBoxDesaVideojuegos.addItem(vista.dtmDesarrolladores.getValueAt(i, 0) + " - " +
                        vista.dtmDesarrolladores.getValueAt(i, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModelDesarrolladores(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Obtener nombres de columnas
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Obtener datos usando ArrayList
        ArrayList<Object[]> dataList = new ArrayList<>();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            dataList.add(row);
        }

        // Convertir ArrayList a array bidimensional
        Object[][] data = dataList.toArray(new Object[0][]);

        vista.dtmDesarrolladores.setDataVector(data, columnNames);
        return vista.dtmDesarrolladores;
    }

    private void refrescarPlataformas() {
        try {
            vista.plataformasTabla.setModel(construirTableModelPlataformas(modelo.consultarPlataformas()));
            vista.comBoxPlatformVideojuegos.removeAllItems();
            for (int i = 0; i < vista.dtmPlataformas.getRowCount(); i++) {
                vista.comBoxPlatformVideojuegos.addItem(vista.dtmPlataformas.getValueAt(i, 0) + " - " +
                        vista.dtmPlataformas.getValueAt(i, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel construirTableModelPlataformas(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Obtener nombres de columnas
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Obtener datos usando ArrayList
        ArrayList<Object[]> dataList = new ArrayList<>();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            dataList.add(row);
        }

        // Convertir ArrayList a array bidimensional
        Object[][] data = dataList.toArray(new Object[0][]);

        vista.dtmPlataformas.setDataVector(data, columnNames);
        return vista.dtmPlataformas;
    }

    private void refrescarVideojuegos() {
        try {
            vista.videojuegosTabla.setModel(construirTableModelVideojuegos(modelo.consultarVideojuegos()));

            // Actualizar ComboBox de ventas con videojuegos
            vista.comBoxVideojuegoVenta.removeAllItems();
            for (int i = 0; i < vista.dtmVideojuegos.getRowCount(); i++) {
                vista.comBoxVideojuegoVenta.addItem(vista.dtmVideojuegos.getValueAt(i, 0) + " - " +
                        vista.dtmVideojuegos.getValueAt(i, 1));
            }
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

        // Obtener datos usando ArrayList
        ArrayList<Object[]> dataList = new ArrayList<>();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            dataList.add(row);
        }

        // Convertir ArrayList a array bidimensional
        Object[][] data = dataList.toArray(new Object[0][]);

        vista.dtmVideojuegos.setDataVector(data, columnNames);
        return vista.dtmVideojuegos;
    }

    private void refrescarVentas() {
        try {
            ResultSet rs = modelo.consultarVentas();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Obtener nombres de columnas
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Obtener datos usando ArrayList
            ArrayList<Object[]> dataList = new ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                dataList.add(row);
            }

            // Convertir ArrayList a array bidimensional
            Object[][] data = dataList.toArray(new Object[0][]);

            // Crear y asignar el modelo
            vista.dtmVentas = new DefaultTableModel(data, columnNames);
            vista.ventasTabla.setModel(vista.dtmVentas);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setOptions() {
        vista.optionDialog.txtIP.setText(modelo.getIp());
        vista.optionDialog.txtUsuario.setText(modelo.getUser());
        vista.optionDialog.pfPass.setText(modelo.getPassword());
        vista.optionDialog.pfAdmin.setText(modelo.getAdminPassword());
    }

    // MÉTODOS DE BORRAR CAMPOS
    private void borrarCamposDesarrolladores() {
        vista.txtNombreDesarrolladores.setText("");
        vista.comBoxPais.setSelectedIndex(-1);
        vista.fechaDesarrolladores.setText("");
        vista.txtWebDesarrolladores.setText("");
        vista.txtEmpleados.setText("");
    }

    private void borrarCamposPlataformas() {
        vista.txtNombrePlataformas.setText("");
        vista.comBoxFabricPlatform.setSelectedIndex(-1);
        vista.txtGeneracionPlataformas.setText("");
        vista.fechaPlataform.setText("");
        vista.txtTipoPlatform.setText("");
    }

    private void borrarCamposVideojuegos() {
        vista.txtTitulovideojuegos.setText("");
        vista.txtCodigoVideojuegos.setText("");
        vista.comBoxDesaVideojuegos.setSelectedIndex(-1);
        vista.comBoxPlatformVideojuegos.setSelectedIndex(-1);
        vista.txtGeneroVideojuegos.setText("");
        vista.txtPrecioVideojuegos.setText("");
        vista.fechaVideojuegos.setText("");
        vista.txtClasificacionVideojuegos.setText("");
        vista.txtStockVideojuegos.setText("");
    }

    private void borrarCamposVentas() {
        vista.comBoxVideojuegoVenta.setSelectedIndex(-1);
        vista.txtCantidadVenta.setText("");
        vista.txtPrecioVenta.setText("");
        vista.fechaVenta.setText("");
        vista.txtClienteVenta.setText("");
    }

    // MÉTODOS DE VALIDACIÓN
    private boolean comprobarDesarrolladorVacio() {
        return vista.txtNombreDesarrolladores.getText().isEmpty() ||
                vista.comBoxPais.getSelectedIndex() == -1 ||
                vista.fechaDesarrolladores.getText().isEmpty() ||
                vista.txtWebDesarrolladores.getText().isEmpty() ||
                vista.txtEmpleados.getText().isEmpty();
    }

    private boolean comprobarPlataformaVacia() {
        return vista.txtNombrePlataformas.getText().isEmpty() ||
                vista.comBoxFabricPlatform.getSelectedIndex() == -1 ||
                vista.txtGeneracionPlataformas.getText().isEmpty() ||
                vista.fechaPlataform.getText().isEmpty() ||
                vista.txtTipoPlatform.getText().isEmpty();
    }

    private boolean comprobarVideojuegoVacio() {
        return vista.txtTitulovideojuegos.getText().isEmpty() ||
                vista.txtCodigoVideojuegos.getText().isEmpty() ||
                vista.comBoxDesaVideojuegos.getSelectedIndex() == -1 ||
                vista.comBoxPlatformVideojuegos.getSelectedIndex() == -1 ||
                vista.txtGeneroVideojuegos.getText().isEmpty() ||
                vista.txtPrecioVideojuegos.getText().isEmpty() ||
                vista.fechaVideojuegos.getText().isEmpty() ||
                vista.txtClasificacionVideojuegos.getText().isEmpty() ||
                vista.txtStockVideojuegos.getText().isEmpty();
    }

    private boolean comprobarVentaVacia() {
        return vista.comBoxVideojuegoVenta.getSelectedIndex() == -1 ||
                vista.txtCantidadVenta.getText().isEmpty() ||
                vista.txtPrecioVenta.getText().isEmpty() ||
                vista.fechaVenta.getText().isEmpty();
    }

    // MÉTODO DE BÚSQUEDA
    private void buscarVideojuegos() {
        String textoBusqueda = vista.txtBusqueda.getText().trim();

        if (textoBusqueda.isEmpty()) {
            Util.showErrorAlert("Por favor, introduce un nombre para buscar");
            return;
        }

        try {
            ResultSet rs = modelo.buscarVideojuegosPorNombre(textoBusqueda);
            vista.videojuegosTabla.setModel(construirTableModelVideojuegos(rs));

            if (vista.dtmVideojuegos.getRowCount() == 0) {
                Util.showErrorAlert("No se encontraron videojuegos con ese nombre");
                refrescarVideojuegos();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Util.showErrorAlert("Error al buscar videojuegos");
        }
    }

    private void actualizarEstadisticas() {
        try {
            LocalDate hoy = LocalDate.now();
            LocalDate inicioMes = hoy.withDayOfMonth(1);

            Object[] stats = modelo.obtenerEstadisticasVentas(inicioMes, hoy);

            vista.lblTotalVentas.setText("Ventas este mes: " + stats[0] + " (" + stats[1] + " unidades)");
            vista.lblIngresoTotal.setText("Ingresos este mes: " + String.format("%.2f", stats[2]) + " €");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*LISTENERS IMPLEMENTOS NO UTILIZADOS*/
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