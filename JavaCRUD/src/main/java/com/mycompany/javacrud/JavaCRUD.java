/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.javacrud;
/**
 *
 * @author sebas
 */

//COMANDO PARA EJECUTAR EN SQL SERVER PARA CONFIGURAR LA BASE DE DATOS:
/*
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'usuariosDB')
BEGIN
    CREATE DATABASE usuariosDB;
END;
GO

USE usuariosDB;
GO

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'usuarios')
BEGIN
    CREATE TABLE usuarios (
	nombre nvarchar(60),
	edad int,
	direccion nvarchar(100),
	telefono varchar(9),
	fotografia nvarchar(500)
	id int IDENTITY(1,1) PRIMARY KEY;
    );
END;
GO
*/
public class JavaCRUD {
    //LA APLICACIÓN UTILIZA MICROSOFT SQL SERVER COMO BASE DE DATOS 
    public static void main(String[] args) {
        InterfazFORM mainForm = new InterfazFORM();
        mainForm.setVisible(true);
    }
}
