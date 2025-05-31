/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolarcurated.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxappescolarcurated.modelo.ConexionBD;
import javafxappescolarcurated.modelo.pojo.Usuario;

/**
 *
 * @author snake
 */
public class InicioSesionDAO {
    public static Usuario verificarCredenciales(String username, String password) throws SQLException{
        Usuario sesionUsuario = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT idUsuario, nombre, apellidoPaterno, apellidoMaterno, username " +
                                "FROM usuario " +
                                "WHERE username = ? AND password = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, username);
            sentencia.setString(2, password);
            ResultSet resultado = sentencia.executeQuery();
            if(resultado.next()){
                sesionUsuario = convertirRegistroUsuario(resultado);
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Error: Se ha perdido la conexión con la base de datos");
        }
        return sesionUsuario;
    }
    
    private static Usuario convertirRegistroUsuario(ResultSet resultado) throws SQLException{
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultado.getInt("idUsuario"));
        usuario.setNombre(resultado.getString("nombre"));
        usuario.setApellidoPaterno(
                resultado.getString("apellidoPaterno"));
        usuario.setApellidoMaterno(
                (resultado.getString("apellidoMaterno")) != null ?
                        resultado.getString("apellidoMaterno") : "");
        usuario.setUsername(resultado.getString("username"));
        return usuario;
    }
}
