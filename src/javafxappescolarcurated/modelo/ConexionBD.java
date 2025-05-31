/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolarcurated.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author snake
 */
public class ConexionBD {
    private static final String IP = "localhost";
    private static final String PUERTO = "3306";
    private static final String NOMBRE_BD = "escolar";
    private static final String USUARIO = "";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    public static Connection abrirConexion() {
        Connection conexionBD = null;
        String urlConexion = 
                String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                IP, PUERTO, NOMBRE_BD);
        
        try {
            Class.forName(DRIVER);
            conexionBD = DriverManager.getConnection(urlConexion, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Clase no encontrada. Log: " + e.getMessage());
        } catch (SQLException s) {
            System.err.println("Error: No se pudo conectar con la base de datos. Log: " + s.getMessage());
        }
        return conexionBD;
    }
}
