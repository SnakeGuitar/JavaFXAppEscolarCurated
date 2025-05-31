/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolarcurated.controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafxappescolarcurated.interfaz.INotificacion;
import javafxappescolarcurated.modelo.dao.AlumnoDAO;
import javafxappescolarcurated.modelo.dao.CatalogoDAO;
import javafxappescolarcurated.modelo.pojo.Alumno;
import javafxappescolarcurated.modelo.pojo.Carrera;
import javafxappescolarcurated.modelo.pojo.Facultad;
import javafxappescolarcurated.modelo.pojo.ResultadoOperacion;
import javafxappescolarcurated.utilidades.Utilidades;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author snake
 */
public class FXMLFormularioAlumnoController implements Initializable {

    @FXML
    private ImageView imageViewFotoAlumno;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApPaterno;
    @FXML
    private TextField textFieldApMaterno;
    @FXML
    private TextField textFieldMatricula;
    @FXML
    private TextField textFieldEmail;
    private DatePicker datePickerfechaNacimiento;
    @FXML
    private ComboBox<Facultad> comboBoxFacultad;
    @FXML
    private ComboBox<Carrera> comboBoxCarrera;

    ObservableList<Facultad> facultades;
    ObservableList<Carrera> carreras;
    File archivoFoto;
    INotificacion observador;
    Alumno alumnoEdicion;
    boolean esEdicion;
    @FXML
    private Label labelNombreError;
    @FXML
    private Label labelApPaternoError;
    @FXML
    private Label labelApMaternoError;
    @FXML
    private Label labelMatriculaError;
    @FXML
    private Label labelEmailError;
    @FXML
    private Label labelFechaNacError;
    @FXML
    private Label labelFacultadError;
    @FXML
    private Label labelCarreraError;
    @FXML
    private DatePicker datePickerFechaNacimiento;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarFacultades();
        seleccionarFacultad();
    }

    public void inicializarInformacion(boolean esEdicion, Alumno alumnoEdicion, INotificacion observador) {
        this.esEdicion = esEdicion;
        this.alumnoEdicion = alumnoEdicion;
        this.observador = observador;
        if (esEdicion) {
            cargarInformacionEdicion();
        }
    }

    private void cargarInformacionEdicion() {
        textFieldNombre.setText(alumnoEdicion.getMatricula());
        textFieldApPaterno.setText(alumnoEdicion.getApellidoPaterno());
        textFieldApMaterno.setText(alumnoEdicion.getApellidoMaterno());
        textFieldEmail.setText(alumnoEdicion.getEmail());
        textFieldMatricula.setText(alumnoEdicion.getMatricula());

        if (!alumnoEdicion.getFechaNacimiento().isEmpty()) {
            datePickerFechaNacimiento.setValue(LocalDate.parse(alumnoEdicion.getFechaNacimiento()));
        }
        int indiceFacultad = obtenerPosicionFacultad(alumnoEdicion.getIdFacultad());
        comboBoxFacultad.getSelectionModel().select(indiceFacultad);

        int indiceCarrera = obtenerPosicionCarrera(alumnoEdicion.getIdCarrera());
        comboBoxCarrera.getSelectionModel().select(indiceCarrera);

        try {
            byte[] foto = AlumnoDAO.obtenerFotoAlumno(alumnoEdicion.getIdAlumno());
            alumnoEdicion.setFoto(foto);
            ByteArrayInputStream input = new ByteArrayInputStream(foto);
            Image image = new Image(input);
            imageViewFotoAlumno.setImage(image);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    private Alumno obtenerAlumnoEdicion() throws IOException {
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(alumnoEdicion.getIdAlumno());
        alumno.setNombre(textFieldNombre.getText());
        alumno.setApellidoPaterno(textFieldApPaterno.getText());
        alumno.setApellidoMaterno(textFieldApMaterno.getText());
        alumno.setEmail(textFieldEmail.getText());
        alumno.setMatricula(textFieldMatricula.getText());
        alumno.setFechaNacimiento(datePickerFechaNacimiento.getValue().toString());
        Carrera carrera = comboBoxCarrera.getSelectionModel().getSelectedItem();
        alumno.setIdCarrera(carrera.getIdCarrera());
        if (archivoFoto != null) {
            byte[] foto = Files.readAllBytes(archivoFoto.toPath());
            alumno.setFoto(foto);
        } else {
            alumno.setFoto(alumnoEdicion.getFoto());
        }
        return alumno;
    }

    private void cargarFacultades() {
        facultades = FXCollections.observableArrayList();
        try {
            List<Facultad> facultadesDAO = CatalogoDAO.obtenerFacultades();
            facultades.addAll(facultadesDAO);
            comboBoxFacultad.setItems(facultades);
        } catch (SQLException ex) {
            // TODO
            ex.printStackTrace();
            Utilidades.crearAlerta(Alert.AlertType.ERROR, "Error al cargar", "No se pudieron guardar las facultades");
        }
    }

    private void seleccionarFacultad() {
        comboBoxFacultad.valueProperty()
                .addListener(new ChangeListener<Facultad>() {
                    @Override
                    public void changed(ObservableValue<? extends Facultad> observable, Facultad oldValue, Facultad newValue) {
                        if (newValue != null) {
                            cargarCarreras(newValue.getIdFacultad());
                        }
                    }
                });
    }

    private void cargarCarreras(int idFacultad) {
        carreras = FXCollections.observableArrayList();
        try {
            List<Carrera> carrerasDAO = CatalogoDAO.obtenerCarrerasPorFacultad(idFacultad);
            carreras.addAll(carrerasDAO);
            comboBoxCarrera.setItems(carreras);
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidades.crearAlerta(Alert.AlertType.ERROR, "Error al cargar", "No se pudieron mostrar las carreras");
        }
    }

    @FXML
    private void buttonSeleccionarFoto(ActionEvent event) {
        mostrarDialogoSeleccionFoto();
    }

    @FXML
    private void buttonGuardar(ActionEvent event) {
        if (validarCampos()) {
            try {
                if (!esEdicion) {
                    Alumno alumno = obtenerAlumnoNuevo();
                    guardarAlumno(alumno);
                } else {
                    Alumno alumno = obtenerAlumnoEdicion();
                    modificarAlumno(alumno);
                }
            } catch (IOException e) {
                Utilidades.crearAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el alumno");
            }
        } else {
            Utilidades.crearAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Ingrese todos los campos");
        }
    }

    @FXML
    private void buttonCancelar(ActionEvent event) {
    }

    private void mostrarDialogoSeleccionFoto() {
        FileChooser dialogoSeleccion = new FileChooser();
        dialogoSeleccion.setTitle("Selecciona una foto");

        FileChooser.ExtensionFilter filtroImagen
                = new FileChooser.ExtensionFilter("Archivos JPG (.jpg)", "*.jpg");
        dialogoSeleccion.getExtensionFilters().add(filtroImagen);

        // TODO Filtro
        archivoFoto
                = dialogoSeleccion.showOpenDialog(
                        Utilidades.getEscenarioComponente(textFieldNombre));
        if (archivoFoto != null) {
            mostrarFotoPerfil(archivoFoto);
        }
    }

    private void mostrarFotoPerfil(File archivoFoto) {
        try {
            BufferedImage bufferImagen = ImageIO.read(archivoFoto);
            Image imagen = SwingFXUtils.toFXImage(bufferImagen, null);
            imageViewFotoAlumno.setImage(imagen);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validarCampos() {
        boolean valido = true;
        labelNombreError.setText("");
        labelApPaternoError.setText("");
        labelApMaternoError.setText("");
        labelMatriculaError.setText("");
        labelEmailError.setText("");
        labelFechaNacError.setText("");
        if (textFieldNombre.getText().isEmpty()) {
            labelNombreError.setText("*");
            valido = false;
        }
        if (textFieldApPaterno.getText().isEmpty()) {
            labelApPaternoError.setText("*");
            valido = false;
        }
        if (textFieldEmail.getText().isEmpty()) {
            labelEmailError.setText("*");
            valido = false;
        }
        if (textFieldMatricula.getText().isEmpty()) {
            labelMatriculaError.setText("*");
            valido = false;
        }
        if (datePickerFechaNacimiento.getValue() == null) {
            labelFechaNacError.setText("*");
            valido = false;
        }
        return valido;
    }

    private Alumno obtenerAlumnoNuevo() throws IOException {
        Alumno alumno = new Alumno();
        alumno.setNombre(textFieldNombre.getText());
        alumno.setApellidoPaterno(textFieldApPaterno.getText());
        alumno.setApellidoMaterno(textFieldApMaterno.getText());
        alumno.setEmail(textFieldEmail.getText());
        alumno.setMatricula(textFieldMatricula.getText());
        alumno.setFechaNacimiento(datePickerFechaNacimiento.getValue().toString());
        Carrera carrera = comboBoxCarrera.getSelectionModel().getSelectedItem();
        alumno.setIdCarrera(carrera.getIdCarrera());
        byte[] foto = Files.readAllBytes(archivoFoto.toPath());
        alumno.setFoto(foto);
        return alumno;

    }

    private void guardarAlumno(Alumno alumno) {
        try {
            ResultadoOperacion resultadoInsertar = AlumnoDAO.registrarAlumno(alumno);
            if (!resultadoInsertar.isError()) {
                Utilidades.crearAlerta(Alert.AlertType.INFORMATION, "Alumno(a) registrado", "El alumno ha sido registrado");
                Utilidades.getEscenarioComponente(textFieldNombre).close();
                observador.operacionExitosa("Insertar", alumno.getNombre());
            } else {
                Utilidades.crearAlerta(Alert.AlertType.ERROR, "Error al insertar", resultadoInsertar.getMensaje());
            }
        } catch (SQLException e) {
            Utilidades.crearAlerta(Alert.AlertType.ERROR, "Error", "Se ha perdido la conexión con la base de datos");
        }
    }

    private void modificarAlumno(Alumno alumno) {
        try {
            ResultadoOperacion resultadoModificar = AlumnoDAO.editarAlumno(alumno);
            if (!resultadoModificar.isError()) {
                Utilidades.crearAlerta(Alert.AlertType.INFORMATION, "Alumno(a) registrado", "El alumno ha sido registrado");
                Utilidades.getEscenarioComponente(textFieldNombre).close();
                observador.operacionExitosa("Insertar", alumno.getNombre());
            } else {
                Utilidades.crearAlerta(Alert.AlertType.ERROR, "Error al modificar", resultadoModificar.getMensaje());
            }
        } catch (SQLException e) {
            Utilidades.crearAlerta(Alert.AlertType.ERROR, "Error", "Se ha perdido la conexión con la base de datos");
        }
    }

    private int obtenerPosicionFacultad(int idFacultad) {
        for (int i = 0; i < facultades.size(); i++) {
            if (facultades.get(i).getIdFacultad() == idFacultad) {
                return i;
            }
        }
        return 0;
    }

    private int obtenerPosicionCarrera(int idCarrera) {
        for (int i = 0; i < carreras.size(); i++) {
            if (carreras.get(i).getIdCarrera() == idCarrera) {
                return 1;
            }
        }
        return 0;
    }

}

/*
Casi todos los diálogos tienen los siguientes pasos:
1. Inicializar
2. Configurar
3. Mostrar
 */
