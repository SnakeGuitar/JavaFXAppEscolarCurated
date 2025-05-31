/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolarcurated.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxappescolarcurated.JavaFXAppEscolarCurated;
import javafxappescolarcurated.modelo.ConexionBD;
import javafxappescolarcurated.modelo.dao.InicioSesionDAO;
import javafxappescolarcurated.modelo.pojo.Usuario;
import javafxappescolarcurated.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author snake
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldUsuario;
    @FXML
    private Label labelErrorUsuario;
    @FXML
    private Label labelErrorPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Connection conexionBD = ConexionBD.abrirConexion();
    }

    @FXML
    private void buttonIniciarSesion(ActionEvent event) {
        String username = textFieldUsuario.getText();
        String password = textFieldPassword.getText();
        if (validarCampos(username, password)) {
            Usuario usuarioSesion = validarCredenciales(username, password);
            if (usuarioSesion != null) {
                irPantallaPrincipal(usuarioSesion);
            }
        }
    }

    private boolean validarCampos(String username, String password) {
        labelErrorPassword.setText("");
        labelErrorUsuario.setText("");

        boolean camposValidos = true;
        if (username.isEmpty()) {
            labelErrorUsuario.setText("*");
            camposValidos = false;
        }
        if (password.isEmpty()) {
            labelErrorPassword.setText("*");
            camposValidos = false;
        }
        return camposValidos;
    }

    private Usuario validarCredenciales(String username, String password) {
        try {
            Usuario usuarioSesion
                    = InicioSesionDAO.verificarCredenciales(username, password);
            if (usuarioSesion != null) {
                Utilidades.crearAlerta(
                        Alert.AlertType.INFORMATION,
                        "Credenciales correctas",
                        String.format("%s%S", "Bienvenido(a) ", usuarioSesion.toString()));
                return usuarioSesion; // ACCESO EXITOSO
            } else {
                Utilidades.crearAlerta(
                        Alert.AlertType.WARNING,
                        "Credenciales incorrectas",
                        "El usuario y/o contraseño no coinciden");
                return null;
            }
        } catch (SQLException e) {
            Utilidades.crearAlerta(Alert.AlertType.ERROR,
                    "Problema de conexión",
                    e.getMessage());
            return null;
        }
    }
    
    private void irPantallaPrincipal(Usuario usuarioSesion){
        try {
            Stage escenarioBase = (Stage) textFieldUsuario.getScene().getWindow();
            FXMLLoader cargador =
                    new FXMLLoader(JavaFXAppEscolarCurated.class.getResource("vista/FXMLPrincipal.fxml"));
            Parent vista = cargador.load();
            
            FXMLPrincipalController controlador = cargador.getController();
            controlador.inicializarInformacion(usuarioSesion);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Inicio");
            escenarioBase.show();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    
    

}
