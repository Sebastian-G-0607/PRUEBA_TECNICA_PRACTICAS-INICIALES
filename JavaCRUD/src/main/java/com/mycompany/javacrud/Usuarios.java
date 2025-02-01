/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javacrud;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**

 @author sebas
 */
//ESTA CLASE CONTROLA TODO EL CRUD DE LOS USUARIOS
public class Usuarios {
    //ESTE MÉTODO RECIBE LOS DATOS DE LA DB Y LOS MUESTRA EN LA TABLA
    public void mostrarUsuarios(JTable tablaUsuarios){
        //OBJETO QUE CREA LA CONEXION CON LA DB
        conexionSQL conexion = new conexionSQL();
        DefaultTableModel modeloTabla = new DefaultTableModel();
        
        String sql = "";
        
        //SE AGREGAN LAS COLUMNAS AL MODELO DE LA TABLA:
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Edad");
        modeloTabla.addColumn("Direccion");
        modeloTabla.addColumn("Telefono");
        modeloTabla.addColumn("Fotografía");
        modeloTabla.addColumn("ID");
        
        //SE AGREGA EL MODELO A LA TABLA DE USUARIOS:
        tablaUsuarios.setModel(modeloTabla);
        
        //SE CREA EL QUERY
        sql = "select * from usuarios";
        
        //SE CREA UN ARRAY QUE CONTENDRÁ LOS DATOS DE CADA FILA DE LA DB
        String [] data = new String [6];
        Statement st;
        
        //SE RECORRE LA DB
        try {
            st = conexion.setConexion().createStatement();
            
            ResultSet rs = st.executeQuery(sql);
            
            //MIENTRAS QUE HAYAN DATOS:
            while (rs.next()){
                //SE RECORRE LA FILA DE LA TABLA DE LA DB Y SE AGREGA AL VECTOR
                data[0] = rs.getString(1);
                data[1] = rs.getString(2);
                data[2] = rs.getString(3);
                data[3] = rs.getString(4);
                data[4] = rs.getString(5);
                data[5] = rs.getString(6);
                
                //SE AGREGA EL VECTOR AL MODELO DE LA TABLA
                modeloTabla.addRow(data);
            }
            //AGREGAR EL RENDERER Y EDITOR A LA COLUMNA "Fotografía"
            tablaUsuarios.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
            tablaUsuarios.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
            
        } catch (Exception e) { //EN CASO DE QUE OCURRA ALGUN ERROR:
            JOptionPane.showMessageDialog(null, "Ocurrió un error al mostrar los usuarios" + e.toString());
        }
    }
    
        // Clase para mostrar el botón en la celda de la tabla
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Ver Imagen");
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Clase para manejar la acción del botón en la celda
    // Clase para manejar la acción del botón en la celda
    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private String rutaImagen;

        public ButtonEditor(JCheckBox checkBox) {
            button = new JButton("Ver Imagen");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    abrirImagenEnVisor(rutaImagen);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof String) {
                rutaImagen = (String) value; // Obtener la ruta de la imagen de la celda
            }
            return button;
        }

        public Object getCellEditorValue() {
            return rutaImagen; // Devolver la ruta en lugar
        }
    }


    // Método para abrir la imagen con el visor de imágenes predeterminado del sistema
    public static void abrirImagenEnVisor(String rutaImagen) {
        File imagenFile = new File(rutaImagen);
        System.out.println(rutaImagen);
        if (imagenFile.exists()) {
            try {
                Desktop.getDesktop().open(imagenFile);
                System.out.println("Imagen abierta en el visor predeterminado.");
            } catch (Exception e) {
                System.out.println("No se pudo abrir la imagen.");
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "La imagen no existe en la ruta:\n" + rutaImagen, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //ESTE MÉTODO SELECCIONA UNA FILA DE LA TABLA Y TRASLADA LOS DATOS A LOS JTEXTFIELD
    public void seleccionarUsuarios(JTable tablaUsuarios, JTextField label_nombre, JTextField label_edad, JTextField label_direccion, JTextField label_telefono){
        try {
            //SE GUARDA LA FILA QUE SE SELECCIONÓ CON UN CLIC
            int fila = tablaUsuarios.getSelectedRow();
            
            if(fila>=0){
                label_nombre.setText(tablaUsuarios.getValueAt(fila, 0).toString());
                label_edad.setText(tablaUsuarios.getValueAt(fila, 1).toString());
                label_direccion.setText(tablaUsuarios.getValueAt(fila, 2).toString());
                label_telefono.setText(tablaUsuarios.getValueAt(fila, 3).toString());
            }
            else{
                JOptionPane.showMessageDialog(null, "No se seleccionó ningun registro");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al seleccionar usuarios " + e.toString());
        }
    }
    
    //ESTE MÉTODO CREA UN NUEVO USUARIO EN LA DB
    public void crearUsuario(JTable tablaUsuarios, JTextField label_nombre, JTextField label_edad, JTextField label_direccion, JTextField label_telefono, String ruta_imagen){
        //OBJETO QUE CREA LA CONEXION CON LA DB
        conexionSQL conexion = new conexionSQL();
        
        //QUERY:
        String sql_query = "insert into usuarios (nombre, edad, direccion, telefono, fotografia) values (?,?,?,?,?)";
        
        try {
            CallableStatement cs = conexion.setConexion().prepareCall(sql_query);
            //SE GUARDAN LOS DATOS QUE SE VAN A ENVIAR A LA DB
            cs.setString(1, label_nombre.getText());
            cs.setInt(2, Integer.parseInt(label_edad.getText()));
            cs.setString(3, label_direccion.getText());
            cs.setString(4, label_telefono.getText());
            cs.setString(5, ruta_imagen);
            
            //SE EJECUTA EL QUERY
            cs.execute();
            
            //MENSAJE DE CONFIRMACIÓN
            JOptionPane.showMessageDialog(null, "Usuario creado con éxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al crear un usuario " + e.toString());
        }
    }
    
    //ESTE MÉTODO MODIFICA UN USUARIO EXISTENTE EN LA DB
    public void actualizarUsuario (JTable tablaUsuarios, JTextField label_nombre, JTextField label_edad, JTextField label_direccion, JTextField label_telefono, String ruta_imagen) {
        // OBJETO QUE CREA LA CONEXION CON LA DB
        conexionSQL conexion = new conexionSQL();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        //FILA SELECCIONADA:
        int fila = tablaUsuarios.getSelectedRow();
        
        int idUsuario = Integer.parseInt(tablaUsuarios.getValueAt(fila, 5).toString()); 
        
        System.out.println(fila);
        //QUERY PARA ACTUALIZAR LA FILA:
        String sql_query = "update usuarios set nombre=?, edad=?, direccion=?, telefono=?, fotografia=? where id=?";
        try {
            String ruta_original = "";
            
            CallableStatement cs = conexion.setConexion().prepareCall(sql_query);
            
            cs.setString(1, label_nombre.getText());
            cs.setInt(2, Integer.parseInt(label_edad.getText()));
            cs.setString(3, label_direccion.getText());
            cs.setString(4, label_telefono.getText());
            if (ruta_imagen == null){
                System.out.println("es null");
                //SE OBTIENE LA RUTA QUE YA SE TENÍA ANTES:              
                try {
                    conn = conexion.setConexion();
                    //CONSULTA PARA OBTENER LA RUTA ACTUAL
                    String sql_select = "select fotografia from usuarios where id = ?";
                    ps = conn.prepareStatement(sql_select);
                    ps.setInt(1, idUsuario);
                    rs = ps.executeQuery();
                    System.out.println("antes");
                    if (rs.next()) {
                        System.out.println("despues");
                        ruta_original = rs.getString("fotografia");
                    }
                    System.out.println(ruta_original);
                    cs.setString(5, ruta_original);
                    
                    //AGREGAR EL RENDERER Y EDITOR A LA COLUMNA "Fotografía"
                    tablaUsuarios.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
                    tablaUsuarios.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

                } catch (Exception e) { //EN CASO DE QUE OCURRA ALGUN ERROR:
                    JOptionPane.showMessageDialog(null, "Ocurrió un error al obtener la ruta original de la imagen" + e.toString());
                }
            }
            else{
                System.out.println("no es null");
                cs.setString(5, ruta_imagen);
            }
            cs.setInt(6, idUsuario);
            
            cs.execute();
            JOptionPane.showMessageDialog(null, "Se modificó correctamente el usuario");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al actualizar el usuario " + e.toString());
        }
    }
    
    //ESTE MÉTODO ES PARA ELIMINAR UN REGISTRO:
    public void eliminarUsuario (JTable tablaUsuarios) {
        //FILA SELECCIONADA:
        int fila = tablaUsuarios.getSelectedRow();
        
        int idUsuario = Integer.parseInt(tablaUsuarios.getValueAt(fila, 5).toString()); 
        
        conexionSQL conexion = new conexionSQL();
        String consulta_sql = "delete from usuarios where id=?";
        try {
            CallableStatement cs = conexion.setConexion().prepareCall(consulta_sql);
            cs.setInt(1, idUsuario);
            cs.execute();
            JOptionPane.showMessageDialog(null, "Se eliminó correctamente el usuario");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar un usuario " + e.toString());
        }
    }
}