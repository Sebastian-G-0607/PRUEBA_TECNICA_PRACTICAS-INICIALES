/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javacrud;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**

 @author sebas
 */
public class conexionSQL {
    //Crea la conexión a la DDBB
    Connection conexion = null;
    
    //CREDENCIALES DE LA BASE DE DATOS, SE DEBE CREAR UN USUARIO EN SQL SERVER PARA ACCEDER A LA BASE DE DATOS
    String usuarioDB = "practicas";
    String contraseniaDB = "practicasiniciales";
    String nombreDB = "usuariosDB";
    String IP_DB = "localhost";
    String port = "1433";
    
    public Connection setConexion () {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String cadenasql = "jdbc:sqlserver://"+IP_DB+":"+port+";"+"databaseName="+nombreDB+";"+"encrypt=true;trustServerCertificate=true";
            conexion = DriverManager.getConnection(cadenasql, usuarioDB, contraseniaDB);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la database");
        }
        return conexion;
    }
}
