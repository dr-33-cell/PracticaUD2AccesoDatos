package gui;

import util.Util;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

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
        refrescarDesarrolladores();
        refrescarPlataformas();
        refrescarVideojuegos();
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
                        // Nota: Necesitarías agregar campos para fundacion, web y empleados en la Vista
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
                    } e