/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolarcurated.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolarcurated.JavaFXAppEscolarCurated;
import javafxappescolarcurated.interfaz.INotificacion;
import javafxappescolarcurated.modelo.dao.AlumnoDAO;
import javafxappescolarcurated.modelo.pojo.Alumno;
import javafxappescolarcurated.modelo.pojo.ResultadoOperacion;
import javafxappescolarcurated.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author snake
 */
public class FXMLAdminAlumnoController implements Initializable, INotificacion {

    private ObservableList<Alumno> alumnos;

    @FXML
    private TableColumn columnMatricula;
    @FXML
    private TableColumn columnApPaterno;
    @FXML
    private TableColumn columnApMaterno;
    @FXML
    private TableColumn columnNombre;
    @FXML
    private TableColumn columnFacultad;
    @FXML
    private TableColumn columnCarrera;
    @FXML
    private TextField textFieldBuscar;
    @FXML
    private TableView<Alumno> tableViewAlumnos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabla();
        cargarInformacion();
    }

    private void configurarTabla() {
        columnMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        columnApPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        columnApMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        columnNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        columnFacultad.setCellValueFactory(new PropertyValueFactory("facultad"));
        columnCarrera.setCellValueFactory(new PropertyValueFactory("carrera"));
    }

    private void cargarInformacion() {
        try {
            alumnos = FXCollections.observableArrayList();
            ArrayList<Alumno> alumnosDAO = AlumnoDAO.obtenerAlumnos();
            alumnos.addAll(alumnosDAO);
            tableViewAlumnos.setItems(alumnos);
        } catch (SQLException ex) {
            Utilidades.crearAlerta(
                    Alert.AlertType.ERROR,
                    "Error de carga",
                    "Error: No se pudo recuperar la informacion de los alumnos"
            );
            cerrarVentana();
        }
    }

    private void cerrarVentana() {
        ((Stage) textFieldBuscar.getScene().getWindow()).close();
    }

    @FXML
    private void buttonAgregar(ActionEvent event) {
        irFormularioAlumno(false, null);
    }

    @FXML
    private void buttonActualizar(ActionEvent event) {
        Alumno alumno = tableViewAlumnos.getSelectionModel().getSelectedItem();
        if (alumno != null) {
            irFormularioAlumno(true, alumno);
        } else {
            Utilidades.crearAlerta(
                    Alert.AlertType.INFORMATION, 
                    "Selecciona un alumno", 
                    "Para modificar la información de un alumno debes seleccionarlo");
        }
    }

    @FXML
    private void buttonEliminar(ActionEvent event) {
        int posicion = tableViewAlumnos.getSelectionModel().getSelectedIndex();
        if (posicion >= 0) {
            Alumno alumno = alumnos.get(posicion);
            String mensaje = String.format(
                    "¿Estás seguro de eliminar al alumno(a) %a?\nEsta acción no se puede deshacer", 
                    alumno.toString());
            if (Utilidades.crearAlertaConfirmacion("Eliminar alumno", mensaje)) {
                eliminarAlumno(alumno.getIdAlumno());
            }
        } else {
            Utilidades.crearAlerta(Alert.AlertType.WARNING, "Error", "Antes debe seleccionar un alumno");
        }
    }
    
    private void irFormularioAlumno(boolean esEdicion, Alumno alumnoEdicion) {
        try {
            Stage escenarioFormulario = new Stage();
            FXMLLoader loader =  new FXMLLoader(JavaFXAppEscolarCurated.class.getResource(
                    "vista/FXMLFormularioAlumno.fxml"));
            Parent vista = loader.load();
            // TODO paso de parametros
            FXMLFormularioAlumnoController controlador = loader.getController();
            controlador.inicializarInformacion(esEdicion, alumnoEdicion, this);
            Scene escena = new Scene(vista);
            escenarioFormulario.setScene(escena);
            escenarioFormulario.setTitle("Formulario de Alumno");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void operacionExitosa(String tipo, String nombreAlumno) {
        System.out.println("Operación: " + tipo + ", con el alumno(a): " + nombreAlumno);
        cargarInformacion();
    }
    
    public void eliminarAlumno(int idAlumno) {
        try {
            ResultadoOperacion resultado = AlumnoDAO.eliminarAlumno(idAlumno);
            if (!resultado.isError()) {
                Utilidades.crearAlerta(
                        Alert.AlertType.INFORMATION, 
                        "Alumno(a) eliminado", 
                        "El alumno fue eliminado correctamente");
            }
        } catch (SQLException e) {
            Utilidades.crearAlerta(
                    Alert.AlertType.ERROR, 
                    "Problemas al eliminar", 
                    "Lo sentimos, por el momento no se pudo realizar la operación seleccionada");
        }
    }
}