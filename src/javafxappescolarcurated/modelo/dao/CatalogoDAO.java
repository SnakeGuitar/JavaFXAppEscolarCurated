/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolarcurated.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxappescolarcurated.modelo.ConexionBD;
import javafxappescolarcurated.modelo.pojo.Carrera;
import javafxappescolarcurated.modelo.pojo.Facultad;

/**
 *
 * @author snake
 */
public class CatalogoDAO {
    public static ArrayList<Facultad> obtenerFacultades() throws SQLException {
        ArrayList<Facultad> facultades = new ArrayList<Facultad>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "select * from facultad";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
               facultades.add(convertirRegistroFacultad(resultado)); 
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }
        return facultades;
    }
    
    public static ArrayList<Carrera> obtenerCarrerasPorFacultad(int idFacultad) throws SQLException {
        ArrayList<Carrera> carreras = new ArrayList<Carrera>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            
            /*
            SELECT
    ->     c.idCarrera,
    ->     c.nombre AS nombreCarrera,
    ->     c.idFacultad,
    ->     a.nombre AS nombreAlumno,
    ->     a.matricula
    -> FROM alumno a
    -> INNER JOIN carrera c ON a.idCarrera = c.idCarrera;
            */
            
            String consulta = "SELECT idCarrera, nombre, codigo, idFacultad "
                    + "FROM carrera"
                    + "WHERE idFacultad = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idFacultad);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                carreras.add(convertirRegistroCarrera(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Se ha perdido la conexión con la base de datos");
        }
        return carreras;
    }
    
    /*
    private int idFacultad;
    private String nombre; 
    */
    
    private static Facultad convertirRegistroFacultad(ResultSet resultado) throws SQLException {
        Facultad facultad = new Facultad();
        facultad.setIdFacultad(resultado.getInt("idFacultad"));
        facultad.setNombre(resultado.getString("nombre"));
        return facultad;
    }
    
    private static Carrera convertirRegistroCarrera(ResultSet resultado) throws SQLException  {
        Carrera carreras = new Carrera();
        carreras.setIdCarrera(resultado.getInt("idCarrera"));
        carreras.setNombre(resultado.getString("nombreCarrera")); // nombreCarrera
        return carreras;
    }
}
