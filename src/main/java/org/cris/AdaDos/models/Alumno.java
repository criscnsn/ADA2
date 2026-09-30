package org.cris.AdaDos.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Alumno {
    // Usamos Propiedades de JavaFX para que la tabla se actualice sola
    private final StringProperty matricula;
    private final StringProperty primerApellido;
    private final StringProperty segundoApellido;
    private final StringProperty nombres;
    private final StringProperty calificacion;

    public Alumno(String matricula, String primerApellido, String segundoApellido, String nombres) {
        this.matricula = new SimpleStringProperty(matricula);
        this.primerApellido = new SimpleStringProperty(primerApellido);
        this.segundoApellido = new SimpleStringProperty(segundoApellido);
        this.nombres = new SimpleStringProperty(nombres);
        this.calificacion = new SimpleStringProperty(""); // Inicializamos vacío
    }

    // GETTERS Y SETTERS ESPECIALES PARA JAVAFX
    // Matricula
    public String getMatricula() { return matricula.get(); }
    public StringProperty matriculaProperty() { return matricula; }

    // Primer Apellido
    public String getPrimerApellido() { return primerApellido.get(); }
    public StringProperty primerApellidoProperty() { return primerApellido; }

    // Segundo Apellido
    public String getSegundoApellido() { return segundoApellido.get(); }
    public StringProperty segundoApellidoProperty() { return segundoApellido; }

    // Nombres
    public String getNombres() { return nombres.get(); }
    public StringProperty nombresProperty() { return nombres; }

    // Calificación
    public String getCalificacion() { return calificacion.get(); }
    public void setCalificacion(String calificacion) { this.calificacion.set(calificacion); }
    public StringProperty calificacionProperty() { return calificacion; }

    public String getCalificacionFormateada() {
        if (calificacion.get() == null || calificacion.get().trim().isEmpty()) {
            return "S/C";
        }
        return calificacion.get();
    }

    // VALIDACIÓN
    public boolean tieneCalificacionValida() {
        String val = getCalificacion();
        if (val == null || val.trim().isEmpty()) return false;
        try {
            int nota = Integer.parseInt(val);
            return nota >= 0 && nota <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}