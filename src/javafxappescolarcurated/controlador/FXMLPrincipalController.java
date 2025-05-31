/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolarcurated.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolarcurated.JavaFXAppEscolarCurated;
import javafxappescolarcurated.modelo.pojo.Usuario;
import javafxappescolarcurated.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author snake
 */
public class FXMLPrincipalController implements Initializable {
    private Usuario sesionUsuario;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(Usuario sesionUsuario) {
        this.sesionUsuario = sesionUsuario;
        cargarInformacionUsuario();
    }
    
    public void cargarInformacionUsuario() {
        if (sesionUsuario != null) {
            labelNombre.setText(sesionUsuario.toString());
            labelUsuario.setText(sesionUsuario.getUsername());
        }
    }

    @FXML
    private void buttonCerrarSesion(ActionEvent event) {
        try {
            Stage escenarioLogin = (Stage) Utilidades.getEscenarioComponente(labelNombre);
            FXMLLoader cargador = new FXMLLoader(JavaFXAppEscolarCurated.class.getResource("vista/FXMLInicioSesion.fxml"));
            Parent vista = cargador.load();
            Scene escenaPrincipal = new Scene(vista);
            escenarioLogin.setScene(escenaPrincipal);
            escenarioLogin.setTitle("Inicio");
            escenarioLogin.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void buttonAdminAlumnos(ActionEvent event) {
        try {
            Stage escenarioAdmin = new Stage();
            Parent vista = FXMLLoader.load(
                    JavaFXAppEscolarCurated.class.getResource(
                            "vista/FXMLAdminAlumno.fxml"));
            Scene escena = new Scene(vista);
            escenarioAdmin.setScene(escena);
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.setTitle("Administración de Alumnos");
            escenarioAdmin.showAndWait();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    
}
