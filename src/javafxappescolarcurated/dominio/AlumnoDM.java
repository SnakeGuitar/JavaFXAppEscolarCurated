/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolarcurated.dominio;

import java.sql.SQLException;
import javafxappescolarcurated.modelo.dao.AlumnoDAO;
import javafxappescolarcurated.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author snake
 */
public class AlumnoDM {
    public static ResultadoOperacion verificarEstadoMatricula(String matricula) {
        ResultadoOperacion resultado = new ResultadoOperacion();
        if (matricula.startsWith("s")) {
            try {
                boolean existe = AlumnoDAO.verificarMatriculaExiste(matricula);
                resultado.setError(existe);
            } catch (SQLException ex) {
                resultado.setError(true);
                resultado.setMensaje("Por el momento no se puede validar la matrícula, "
                        + "por favot inténtelo más tarde");
            }
        } else {
            resultado.setError(true);
            resultado.setMensaje("La matrícula no tiene el formato correcto");
        }
        return resultado;
    }
}
