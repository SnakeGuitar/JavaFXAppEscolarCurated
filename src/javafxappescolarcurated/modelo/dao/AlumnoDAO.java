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
import javafxappescolarcurated.modelo.pojo.Alumno;
import javafxappescolarcurated.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author snake
 */
public class AlumnoDAO {

    public static ArrayList<Alumno> obtenerAlumnos() throws SQLException {
        ArrayList<Alumno> alumnos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT "
                    + "idAlumno, a.nombre, apellidoPaterno, apellidoMaterno, matricula, email, fechaNacimiento, "
                    + "a.idCarrera, c.nombre AS 'carrera', f.idFacultad, f.nombre AS 'facultad' "
                    + "FROM alumno a "
                    + "INNER JOIN carrera c ON c.idCarrera = a.idCarrera "
                    + "INNER JOIN facultad f ON f.idFacultad = c.idFacultad";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                alumnos.add(convertirRegistroAlumno(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexion en la BD");
        }
        return alumnos;
    }

    public static ResultadoOperacion registrarAlumno(Alumno alumno) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String sentencia = "INSERT INTO "
                    + "alumno (nombre, apellidoPaterno, "
                    + "apellidoMaterno, matricula, email, "
                    + "idCarrera, fechaNacimiento, foto) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
            prepararSentencia.setString(1, alumno.getNombre());
            prepararSentencia.setString(2, alumno.getApellidoPaterno());
            prepararSentencia.setString(3, alumno.getApellidoMaterno());
            prepararSentencia.setString(4, alumno.getMatricula());
            prepararSentencia.setString(5, alumno.getEmail());
            prepararSentencia.setInt(6, alumno.getIdCarrera());
            prepararSentencia.setString(7, alumno.getFechaNacimiento());
            prepararSentencia.setBytes(8, alumno.getFoto());
            int filasAfectadas = prepararSentencia.executeUpdate();
            if (filasAfectadas == 1) {
                resultado.setError(false);
                resultado.setMensaje("Alumno(a) registrado correctamente");
                
            } else {
                resultado.setError(true);
                resultado.setMensaje("Error: No se pudo guardar el alumno(a) en el sistema");   
            }
            prepararSentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Se ha perdido la conexión de la base de datos");
        }
        return resultado;
    }
    
    public static byte[] obtenerFotoAlumno(int idAlumno) throws SQLException {
        // TO DO
        byte[] foto = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT foto "
                    + "FROM alumno "
                    + "WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idAlumno);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                foto = resultado.getBytes("foto");
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        }
        return foto;
    }
    
/*
    public static boolean verificarExistenciaMatricula(String matricula) throws SQLException {
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT matricula "
                    + "FROM alumno "
                    + "WHERE matricula = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return true;
            }
        }
        return false;
    }
*/
    
    // update alumno set nombre = 'Jose', apellidoPaterno = 'Sanchez', apellidoMaterno = 'Martinez' where idAlumno = 21;
    
    public static ResultadoOperacion editarAlumno(Alumno alumno) throws SQLException {
        // TO DO -- RESTRICCIÓN: NO SE PUEDE EDITAR LA MATRÍCULA
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "UPDATE alumno "
                    + "SET nombre = ?, "
                    + "apellidoPaterno = ?, "
                    + "apellidoMaterno = ?, "
                    + "email = ?, "
                    + "idCarrera = ?, "
                    + "fechaNacimiento = ?, "
                    + "foto = ? "
                    + "WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getEmail());
            sentencia.setInt(5, alumno.getIdCarrera());
            sentencia.setString(6, alumno.getFechaNacimiento());
            sentencia.setBytes(7, alumno.getFoto());
            sentencia.setInt(8, alumno.getIdAlumno());
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 1) {
                resultado.setError(false);
                resultado.setMensaje("Alumno(a) actualizado");
            } else {
                resultado.setError(true);
                resultado.setMensaje("Error: No se pudo actualzar el alumno");
            }
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Se ha perdido la conexión con la base de datos");
        }
        return resultado;
    }
    
    public static ResultadoOperacion eliminarAlumno(int idAlumno) throws SQLException {
        // TO DO
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "DELETE FROM alumno WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idAlumno);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 1) {
                resultado.setError(false);
                resultado.setMensaje("Alumno(a) eliminado correctemente");
            } else {
                resultado.setError(true);
                resultado.setMensaje("No se pudo eliminar el alumno");
            }
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Se ha perdido la conexión con la base de datos");
        }
        
        return null;
    }

    private static Alumno convertirRegistroAlumno(ResultSet resultado) throws SQLException {
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(resultado.getInt("idAlumno"));
        alumno.setNombre((resultado.getString("nombre")));
        alumno.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        alumno.setApellidoMaterno(
                (resultado.getString("apellidoMaterno")) != null
                ? resultado.getString("apellidoMaterno") : "");
        alumno.setMatricula(resultado.getString("matricula"));
        alumno.setEmail(resultado.getString("email"));
        alumno.setFechaNacimiento(resultado.getString("fechaNacimiento"));
        alumno.setIdCarrera(resultado.getInt("idCarrera"));
        alumno.setCarrera(resultado.getString("carrera"));
        alumno.setIdFacultad(resultado.getInt("idFacultad"));
        alumno.setFacultad(resultado.getString("facultad"));
        return alumno;
    }
    
    public static boolean verificarMatriculaExiste(String matricula) throws SQLException {
        boolean matriculaExiste = true;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT matricula FROM alumno WHERE matricula = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            matriculaExiste = resultado.next();
            
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error: Se ha perdido la conexión a la base de datos");
        }
        return matriculaExiste;
    }
}