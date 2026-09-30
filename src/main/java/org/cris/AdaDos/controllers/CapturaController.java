package org.cris.AdaDos.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.cris.AdaDos.models.Alumno;
import org.cris.AdaDos.TareaTres.GeneradorPDF;
import org.cris.AdaDos.utils.ExportadorCSV;
import org.cris.AdaDos.utils.UIEfectos;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CapturaController {

    @FXML private VBox viewCarga;
    @FXML private VBox viewTabla;
    @FXML private Button btnCargarArchivo;

    @FXML private TableView<Alumno> tablaAlumnos;
    @FXML private TableColumn<Alumno, String> colMatricula;
    @FXML private TableColumn<Alumno, String> colApellido1;
    @FXML private TableColumn<Alumno, String> colApellido2;
    @FXML private TableColumn<Alumno, String> colNombres;
    @FXML private TableColumn<Alumno, String> colCalificacion;

    @FXML private Button btnGenerarPDF;
    @FXML private Button btnGenerarCSV;

    private ObservableList<Alumno> listaAlumnos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        UIEfectos.estilarBotonAzul(btnCargarArchivo);
        UIEfectos.estilarBotonRojo(btnGenerarPDF);

        viewCarga.setVisible(true);
        viewTabla.setVisible(false);

        configurarTabla();
        actualizarEstadoBotonCSV();
    }

    private void configurarTabla() {
        colMatricula.setCellValueFactory(cellData -> cellData.getValue().matriculaProperty());
        colApellido1.setCellValueFactory(cellData -> cellData.getValue().primerApellidoProperty());
        colApellido2.setCellValueFactory(cellData -> cellData.getValue().segundoApellidoProperty());
        colNombres.setCellValueFactory(cellData -> cellData.getValue().nombresProperty());

        // Configuración especial para la columna de calificación
        colCalificacion.setCellValueFactory(cellData -> cellData.getValue().calificacionProperty());

        // USAMOS LA NUEVA CELDA PERSONALIZADA
        colCalificacion.setCellFactory(column -> new CeldaCalificacion());

        // Altura de fila un poco más grande para que quepa el mensaje de error si sale
        tablaAlumnos.setRowFactory(tv -> {
            TableRow<Alumno> row = new TableRow<>();
            row.setPrefHeight(60); // Altura fija para evitar saltos raros
            return row;
        });

        tablaAlumnos.setEditable(true);
    }

    // --- CLASE INTERNA PARA LA CELDA "SIEMPRE VISIBLE" ---
    private class CeldaCalificacion extends TableCell<Alumno, String> {
        private final TextField textField = new TextField();
        private final Label errorLabel = new Label("Ingresa un entero (1-100)");
        private final VBox layout = new VBox(2); // VBox con 2px de espacio

        public CeldaCalificacion() {
            // Estilo del campo: Blanco y redondeado
            textField.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");

            // Estilo del error: Rojo y pequeño
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 9px;");
            errorLabel.setVisible(false); // Oculto por defecto

            layout.setAlignment(Pos.CENTER);
            layout.getChildren().addAll(textField, errorLabel);

            // LOGICA: Guardar al perder el foco (click afuera)
            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    procesarInput();
                }
            });

            // LOGICA: Guardar al dar Enter (opcional, pero buena costumbre)
            textField.setOnAction(e -> {
                procesarInput();
                // Opcional: mover foco a la siguiente celda o fila si quisieras
            });
        }

        private void procesarInput() {
            String texto = textField.getText().trim();
            Alumno alumno = getTableRow().getItem();

            if (alumno == null) return;

            if (texto.isEmpty()) {
                // Caso Vacío -> Valido (S/C)
                errorLabel.setVisible(false);
                textField.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");
                alumno.setCalificacion("");
                actualizarEstadoBotonCSV();
                return;
            }

            try {
                int valor = Integer.parseInt(texto);
                if (valor >= 0 && valor <= 100) {
                    // Caso Válido
                    errorLabel.setVisible(false);
                    textField.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #28a745; -fx-border-radius: 5;"); // Borde verde sutil
                    alumno.setCalificacion(String.valueOf(valor));
                    actualizarEstadoBotonCSV();
                } else {
                    mostrarErrorVisual();
                }
            } catch (NumberFormatException e) {
                mostrarErrorVisual();
            }
        }

        private void mostrarErrorVisual() {
            errorLabel.setVisible(true);
            textField.setStyle("-fx-background-color: #fff0f0; -fx-background-radius: 5; -fx-border-color: red; -fx-border-radius: 5;");
            // No guardamos el dato inválido en el modelo, o lo dejamos como estaba
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                // Siempre mostramos el campo de texto (sin doble click)
                textField.setText(item == null ? "" : item);
                errorLabel.setVisible(false); // Resetear error al reciclar celda
                textField.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");
                setGraphic(layout);
            }
        }
    }
    // -----------------------------------------------------

    private void actualizarEstadoBotonCSV() {
        boolean todoCompleto = true;
        for (Alumno al : listaAlumnos) {
            if (!al.tieneCalificacionValida()) {
                todoCompleto = false;
                break;
            }
        }

        if (todoCompleto && !listaAlumnos.isEmpty()) {
            btnGenerarCSV.setDisable(false);
            UIEfectos.estilarBotonVerde(btnGenerarCSV);
        } else {
            UIEfectos.estilarBotonDeshabilitado(btnGenerarCSV);
        }
    }

    @FXML
    public void cargarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar lista de alumnos");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));

        File file = fileChooser.showOpenDialog(btnCargarArchivo.getScene().getWindow());

        if (file != null) {
            procesarArchivo(file);
        }
    }

    private void procesarArchivo(File file) {
        listaAlumnos.clear();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String linea = br.readLine();

            if (linea == null || linea.split(",").length < 4) {
                mostrarAlerta(Alert.AlertType.ERROR, "Formato Incorrecto", "El archivo debe tener al menos 4 columnas.");
                return;
            }

            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d.length >= 4) {
                    listaAlumnos.add(new Alumno(d[0], d[1], d[2], d[3]));
                }
            }

            tablaAlumnos.setItems(listaAlumnos);
            viewCarga.setVisible(false);
            viewTabla.setVisible(true);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Carga Exitosa", "Se cargaron " + listaAlumnos.size() + " alumnos.");
            actualizarEstadoBotonCSV();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo leer el archivo: " + e.getMessage());
        }
    }

    @FXML
    public void generarPDF() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar PDF");
        fc.setInitialFileName("ReporteCalificaciones.pdf");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File destino = fc.showSaveDialog(tablaAlumnos.getScene().getWindow());

        if (destino != null) {
            try {
                GeneradorPDF generador = new GeneradorPDF();
                generador.generarPdfDesdeLista(listaAlumnos, destino.getAbsolutePath());
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "PDF generado correctamente.");
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Falló el PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void generarCSV() {
        for (Alumno al : listaAlumnos) {
            if (!al.tieneCalificacionValida()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Incompleto", "Faltan calificaciones por asignar.");
                return;
            }
        }

        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar CSV");
        fc.setInitialFileName("CalificacionesFinales.csv");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));

        File destino = fc.showSaveDialog(tablaAlumnos.getScene().getWindow());

        if (destino != null) {
            try {
                ExportadorCSV.generarReporte(listaAlumnos, destino.getAbsolutePath());
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "CSV generado correctamente.");
            } catch (IOException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el CSV: " + e.getMessage());
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        if (tablaAlumnos.getScene() != null) {
            alerta.initOwner(tablaAlumnos.getScene().getWindow());
        } else if (btnCargarArchivo.getScene() != null) {
            alerta.initOwner(btnCargarArchivo.getScene().getWindow());
        }

        alerta.initModality(Modality.APPLICATION_MODAL);
        alerta.showAndWait();
    }
}