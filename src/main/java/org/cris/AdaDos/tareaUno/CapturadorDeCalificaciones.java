package org.cris.AdaDos.tareaUno;

import org.cris.AdaDos.TareaTres.GeneradorPDF;

import java.io.File;
import java.util.InputMismatchException;

/**
 * Clase independiente que encapsula el método generacionCalificaciones
 * Las variables temporales (nuevaRuta, calificacionTemp, opcion, rutaTemporal, rutaFinalPDF)
 * son atributos de esta clase, permitiendo extraer submétodos sin pasar parámetros.
 */
public class CapturadorDeCalificaciones {

    // Referencia al objeto de contexto
    private tareaUno tarea;

    // Variables temporales convertidas en atributos de la clase
    private String nuevaRuta;
    private int calificacionTemp;
    private int opcion;
    private String rutaTemporal;
    private String rutaFinalPDF;

    public CapturadorDeCalificaciones(tareaUno tarea) {
        this.tarea = tarea;
    }

    public CapturadorDeCalificaciones() {
        this(new tareaUno());
    }

    /**
     * Método principal que orquesta el flujo de captura y generación.
     */
    public void ejecutar() {
        if (!seleccionarRuta()) {
            return;
        }
        capturarCalificacionesEnConsola();
        seleccionarYGenerarFormato();
    }

    /**
     * Lectura y validación del archivo de entrada.
     */
    public boolean seleccionarRuta() {
        //Seleccionar archivo
        System.out.println("Ruta actual: " + tarea.getRutaEntrada());
        System.out.print("Presione Enter para usarla o ingrese una nueva ruta:");
        nuevaRuta = tarea.getScanner().nextLine();
        if (!nuevaRuta.isEmpty()) {
            tarea.setRutaEntrada(nuevaRuta);
        }
        //*************+VALIDADOR ARCHIVO***********
        if (!tarea.getRutaEntrada().endsWith(".csv")) {
            System.err.println("Error: El archivo debe ser .csv");
            return false;
        }
        /******NO BORRAR!!!!!!!!*******/
        if (!tarea.cargarDatos()) {
            return false;
        }
        return true;
    }

    /**
     * Bucle para capturar calificaciones en consola de todos los alumnos.
     */
    public void capturarCalificacionesEnConsola() {
        //***********Captura de calificaciones******
        System.out.println("CAPTURA DE CALIFICACIONES (Disenio de Sofatware)");
        for (tareaUno.Alumno alumno : tarea.getAlumnos()) {
            boolean calificacionValida = false;
            while (!calificacionValida) {
                System.out.print("Ingresa la calificación de " + alumno.getNombreAlumno() + " (" + alumno.getMatricula() + "): ");

                String entrada = tarea.getScanner().nextLine();

                if (entrada.isEmpty()) {
                    alumno.calificacion = null;
                    calificacionValida = true;
                } else {
                    try {
                        calificacionTemp = Integer.parseInt(entrada);
                        if (calificacionTemp >= 1 && calificacionTemp <= 100) {
                            alumno.calificacion = calificacionTemp;
                            calificacionValida = true;
                        } else {
                            System.out.println("La calificación debe ser entre 1 y 100.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Debes ingresar un número entero o presionar enter.");
                    }
                }
            }
        }
    }

    /**
     * Menú para seleccionar el formato de salida y delegar la generación.
     */
    public void seleccionarYGenerarFormato() {
        //******Confirmacion de salida****** PDF CSV O NADOTA
        System.out.print("\nIngrese 1 si quiere generar el archivo CSV o 2 si desea generar el PDF: ");
        try {
            opcion = tarea.getScanner().nextInt();
            switch (opcion) {
                case 1:
                    generarCSV();
                    break;
                case 2:
                    generarPDF();
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Debe ingresar un número entero.");
            tarea.getScanner().nextLine();
        }
    }

    /**
     * Generación de archivo de salida en formato CSV sin requerir parámetros.
     */
    public void generarCSV() {
        tarea.generarArchivoSalida(tarea.getRutaSalida(), false);
    }

    /**
     * Generación de archivo de salida en formato PDF utilizando rutaTemporal y rutaFinalPDF.
     */
    public void generarPDF() {
        rutaTemporal = tarea.getRutaSalida().replace(".csv", "_temp.csv");
        rutaFinalPDF = tarea.getRutaSalida().replace(".csv", ".pdf");
        tarea.generarArchivoSalida(rutaTemporal, true);

        GeneradorPDF generador = new GeneradorPDF();
        generador.convertirCsvAPdf(rutaTemporal, rutaFinalPDF);

        new File(rutaTemporal).delete();
    }

    // Getters y Setters
    public tareaUno getTarea() {
        return tarea;
    }

    public void setTarea(tareaUno tarea) {
        this.tarea = tarea;
    }

    public String getNuevaRuta() {
        return nuevaRuta;
    }

    public void setNuevaRuta(String nuevaRuta) {
        this.nuevaRuta = nuevaRuta;
    }

    public int getCalificacionTemp() {
        return calificacionTemp;
    }

    public void setCalificacionTemp(int calificacionTemp) {
        this.calificacionTemp = calificacionTemp;
    }

    public int getOpcion() {
        return opcion;
    }

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }

    public String getRutaTemporal() {
        return rutaTemporal;
    }

    public void setRutaTemporal(String rutaTemporal) {
        this.rutaTemporal = rutaTemporal;
    }

    public String getRutaFinalPDF() {
        return rutaFinalPDF;
    }

    public void setRutaFinalPDF(String rutaFinalPDF) {
        this.rutaFinalPDF = rutaFinalPDF;
    }
}
